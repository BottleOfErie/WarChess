package ck.ckrc.erie.warchess.net;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.PreMain;
import ck.ckrc.erie.warchess.game.Chess;
import ck.ckrc.erie.warchess.game.ChessClassInvoker;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.game.Player;
import ck.ckrc.erie.warchess.ui.GameScene;
import ck.ckrc.erie.warchess.ui.Play;
import ck.ckrc.erie.warchess.ui.Setting;
import ck.ckrc.erie.warchess.utils.DataPackage;
import ck.ckrc.erie.warchess.utils.ResourceSerialization;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * 这个类用于进行多人游戏时双方信息同步
 */
public class MapSyncThread extends Thread{

    /*
     * disconnect
     * load length|<byte[] of Class>
     * sync x y length|<byte[] of DataPackage Object>
     * chat from str
     * round number
     * res {require|send} name length|<byte[] of Resource>
     * active {true|false} name...
     * syncP teamFlag length|<byte[] of Player Object>
     * repaint
     */

    private Socket socket=null;
    private DataInputStream input=null;
    private DataOutputStream output=null;
    private boolean working;

    /**
     * 开始同步数据
     * @param socket 已经连接好的对方Socket
     * @throws IOException 如果无法建立连接
     */
    public MapSyncThread(Socket socket) throws IOException {
        this.socket=socket;
        input=new DataInputStream(socket.getInputStream());
        output=new DataOutputStream(socket.getOutputStream());
        Main.log.addLog("SyncThread ready",this.getClass());
    }

    /**
     * 将字节数组转为Java对象
     * @param arr 将被转化的字节数组
     * @return 解码得到的对象
     * @throws IOException 如果无法读取字节数组
     * @throws ClassNotFoundException 如果对象对应的类没有被加载
     */
    public Object fromByteArray(byte[] arr) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis=new ByteArrayInputStream(arr);
        ObjectInputStream ois=new ObjectInputStream(bis);
        return ois.readObject();
    }

    /**
     * 将对象转为字节数组
     * @param obj 被编码的对象
     * @return 编码结果
     * @throws IOException 如果编码失败
     */
    public byte[] toByteArray(Object obj) throws IOException {
        ByteArrayOutputStream bis=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(bis);
        oos.writeObject(obj);
        return bis.toByteArray();
    }

    /**
     * 处理对方发送的消息
     */
    @Override
    public void run() {
        working=true;
        while(working){
            try {
                String command=input.readUTF();
                //System.out.println(command);
                var strings=command.split(" ");
                switch(strings[0]){
                    case "disconnect":
                        socket.close();
                        Platform.runLater(GameScene::showDisconnect);
                        return;
                    case "sync":
                        var x=Integer.parseInt(strings[1]);
                        var y=Integer.parseInt(strings[2]);
                        var length=Integer.parseInt(strings[3]);
                        byte[] arr=input.readNBytes(length);
                        var dataPack=(DataPackage) fromByteArray(arr);
                        var chess=Main.currentGameEngine.getChess(x,y);
                        if(chess!=null)
                            chess.syncDataPackage(dataPack);
                        else{
                            var clazz=Main.chessClassLoader.getClassByName((String)dataPack.get("className"));
                            if(clazz==null){
                                Main.log.addLog("No such class:"+dataPack.get("className"),this.getClass());
                            }else {
                                Main.currentGameEngine.setChess(x, y, ChessClassInvoker.getNewInstance(clazz, Player.getNewPlayer(-1), x, y));
                                Main.currentGameEngine.getChess(x, y).syncDataPackage(dataPack);
                            }
                        }
                        break;
                    case "chat":
                        var fromFlag=Integer.parseInt(strings[1]);
                        var message=command.substring(command.indexOf(strings[1])+strings[1].length()+1);
                        GameScene.showchat(message, GameScene.textArea,fromFlag);
                        break;
                    case "load":
                        var len=Integer.parseInt(strings[1]);
                        byte[] a=input.readNBytes(len);
                        var clazz0=Main.chessClassLoader.loadChessClassFromByteArray(a);
                        if(clazz0==null){
                            var dialog=new Alert(Alert.AlertType.WARNING);
                            dialog.setContentText("从网络流解析类失败");
                            dialog.show();
                        }else
                            Platform.runLater(Setting::initClass);
                        break;
                    case "round":
                        if(!Director.GetDirector().isGameStarted()) {
                            Main.currentGameEngine=new Engine();
                            Setting.checkloadedclass();
                            Platform.runLater(()->Director.GetDirector().gameStart());
                        }
                        var flg=Integer.parseInt(strings[1]);
                        Main.currentGameEngine.nextRound(flg);
                        break;
                    case "res":
                        var option=strings[1];
                        var name=strings[2];
                        if("send".equals(option)){
                            var leng=Integer.parseInt(strings[3]);
                            byte[] ba=input.readNBytes(leng);
                            ResourceSerialization.addResourceWithName(name,ba);
                        }else{
                            try{
                                var res=ResourceSerialization.getResourceByNameWithoutNetwork(name);
                                if(res!=null)
                                    sendResSend(name, res);
                                else
                                    Main.log.addLog("failed to send resource:"+name+" as it's not exist",this.getClass());
                            } catch (IOException e) {
                                Main.log.addLog("Failed to Send Resource:"+name,this.getClass());
                                Main.log.addLog(e,this.getClass());
                            }
                        }
                        break;
                    case "active":
                        var mode="true".equals(strings[1]);
                        for (int i = 2; i < strings.length; i++) {
                            var clazz=Main.chessClassLoader.getClassByName(strings[i]);
                            if(clazz!=null)
                                Setting.loadornot.put(clazz,mode);
                            else
                                Main.log.addLog("failed to active:"+strings[i]+" as it's not exist",this.getClass());
                        }
                        Platform.runLater(Setting::initClass);
                        break;
                    case "syncP":
                        var teamFlag=Integer.parseInt(strings[1]);
                        var lengt=Integer.parseInt(strings[2]);
                        byte[] arr1=input.readNBytes(lengt);
                        Main.currentGameEngine.setPlayer(teamFlag,(Player)fromByteArray(arr1));
                        break;
                    case "repaint":
                        Platform.runLater(Play::drawAllChess);
                        break;
                    default:
                        Main.log.addLog("Unknown command:"+command,this.getClass());
                }
            } catch (SocketException e){
                Main.log.addLog("connection closed",this.getClass());
                if(working)
                    Platform.runLater(GameScene::showDisconnect);
                return;
            } catch (IOException e) {
                Main.log.addLog("connection failed",this.getClass());
                Main.log.addLog(e,this.getClass());
                Platform.runLater(GameScene::showDisconnect);
                return;
            } catch (ClassNotFoundException e) {
                Main.log.addLog("no such class!",this.getClass());
                Main.log.addLog(e,this.getClass());
            } catch (Exception e){
                Main.log.addLog("Unexpected Exception!",this.getClass());
                Main.log.addLog(e,this.getClass());
            }
        }
    }

    /**
     * 发送聊天消息
     * @param str 消息
     * @param teamFlag 发送者队伍标签
     * @throws IOException 如果发送失败
     */
    public void sendChat(String str,int teamFlag) throws IOException {
        output.writeUTF("chat "+teamFlag+" "+str);
    }

    /**
     * 同步棋子信息
     * @param x 被同步棋子x坐标
     * @param y 被同步棋子y坐标
     * @param chess 被同步棋子对象
     * @throws IOException 如果发送失败
     */
    public void sendSync(int x,int y,Chess chess) throws IOException {
        if(chess==null)return;
        var pack=chess.getDataPackage();
        if(pack==null)return;
        var arr=toByteArray(pack);
        output.writeUTF("sync "+x+" "+y+" "+arr.length);
        output.write(arr);
    }

    /**
     * 发送断开连接信息，关闭输出流
     * @throws IOException 如果发送失败
     */
    public void sendDisconnect() throws IOException {
        output.writeUTF("disconnect");
        output.flush();
        output.close();
        working=false;
    }

    /**
     * 发送切换回合信息
     * @param roundFlag 下一个队伍的队伍标签
     * @throws IOException 如果发送失败
     */
    public void sendRound(int roundFlag) throws IOException {
        output.writeUTF("round "+roundFlag);
    }

    /**
     * 发送类数据用于加载
     * @param name 被发送的类名
     * @throws IOException 如果发送失败
     */
    public void sendLoad(String name) throws IOException {
        var arr= PreMain.transformer.map.get(name);
        output.writeUTF("load "+arr.length);
        output.write(arr);
    }

    /**
     * 请求指定资源文件
     * @param name 被请求文件名
     * @throws IOException 如果发送失败
     */
    public void sendResRequire(String name) throws IOException {
        output.writeUTF("res require "+name);
    }

    /**
     * 发送资源文件
     * @param name 被发送文件名
     * @param res 被发送数据的字节数组
     * @throws IOException 如果发送失败
     */
    public void sendResSend(String name,byte[] res) throws IOException {
        if(res==null)return;
        output.writeUTF("res send "+name+" "+res.length);
        output.write(res);
    }

    /**
     * 发送同步类启用/禁用
     * @param mode 如果为true，启用这些类。如果为false，禁用这些类
     * @param names 被改变状态的类名
     * @throws IOException 如果发送失败
     */
    public void sendActive(boolean mode,String... names) throws IOException {
        StringBuilder sb=new StringBuilder("active "+mode);
        for (var x: names)
            sb.append(" ").append(x);
        output.writeUTF(sb.toString());
    }

    /**
     * 发送同步玩家数据
     * @param teamFlag 被同步玩家队伍标签
     * @param p 被同步玩家对象
     * @throws IOException 如果发送失败
     */
    public void sendSyncP(int teamFlag,Player p) throws IOException {
        var arr=toByteArray(p);
        output.writeUTF("syncP "+teamFlag+" "+arr.length);
        output.write(arr);
    }

    /**
     * 请求重新绘制棋盘
     * @throws IOException 如果发送失败
     */
    public void sendRepaint() throws IOException {
        output.writeUTF("repaint");
    }

}
