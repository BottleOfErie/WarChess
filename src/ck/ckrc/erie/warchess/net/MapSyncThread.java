package ck.ckrc.erie.warchess.net;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.PreMain;
import ck.ckrc.erie.warchess.game.Chess;

import java.io.*;
import java.net.Socket;

public class MapSyncThread extends Thread{

    /**
     * disconnected
     * load {length}|<byte[] of Class>
     * sync {x} {y} {length}|<Chess Object>
     * chat {str}
     * round {number}
     */

    private Socket socket=null;
    private DataInputStream input=null;
    private DataOutputStream output=null;

    public MapSyncThread(Socket socket) throws IOException {
        this.socket=socket;
        input=new DataInputStream(socket.getInputStream());
        output=new DataOutputStream(socket.getOutputStream());
        Main.log.addLog("SyncThread ready",this.getClass());
    }

    public Object fromByteArray(byte[] arr) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis=new ByteArrayInputStream(arr);
        ObjectInputStream ois=new ObjectInputStream(bis);

        return ois.readObject();
    }

    public byte[] toByteArray(Object obj) throws IOException {
        ByteArrayOutputStream bis=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(bis);
        oos.writeObject(obj);
        return bis.toByteArray();
    }

    @Override
    public void run() {
        while(true){
            try {
                String command=input.readUTF();
                //System.out.println(command);
                var strings=command.split(" ");
                switch(strings[0]){
                    case "disconnected":
                        socket.close();
                        return;
                    case "sync":
                        var x=Integer.parseInt(strings[1]);
                        var y=Integer.parseInt(strings[2]);
                        var length=Integer.parseInt(strings[3]);
                        byte[] arr=input.readNBytes(length);
                        Main.currentGameEngine.setChess(x,y,(Chess)fromByteArray(arr));
                        break;
                    case "chat":
                        System.out.println(strings[1]);
                        break;
                    case "load":
                        var len=Integer.parseInt(strings[1]);
                        byte[] a=input.readNBytes(len);
                        Main.chessClassLoader.loadChessClassFromByteArray(a);
                        break;
                    case "round":
                        var flg=Integer.parseInt(strings[1]);
                        Main.currentGameEngine.nextRound(flg);
                        break;
                    default:
                        Main.log.addLog("Unknown command:"+command,this.getClass());
                }
            } catch (IOException e) {
                Main.log.addLog("connection failed",this.getClass());
                Main.log.addLog(e,this.getClass());
                return;
            } catch (ClassNotFoundException e) {
                Main.log.addLog("no such class!",this.getClass());
                Main.log.addLog(e,this.getClass());
            }
        }
    }

    public void sendChat(String str) throws IOException {
        output.writeUTF("chat "+str);
    }

    public void sendSync(int x,int y,Chess chess) throws IOException {
        var arr=toByteArray(chess);
        output.writeUTF("sync "+x+" "+y+" "+arr.length);
        output.write(arr);
    }

    public void sendDisconnected() throws IOException {
        output.writeUTF("disconnected");
    }

    public void sendRound(int roundFlag) throws IOException {
        output.writeUTF("round "+roundFlag);
    }

    public void sendLoad(String name) throws IOException {
        var arr= PreMain.transformer.map.get(name);
        output.writeUTF("load "+arr.length);
        output.write(arr);
    }

}
