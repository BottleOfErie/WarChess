package ck.ckrc.erie.warchess.net;

import ck.ckrc.erie.warchess.Controller.ChooseOneSideController;
import ck.ckrc.erie.warchess.Main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 这个类用于多人游戏时建立连接
 */
public class Client extends Thread{

    /**
     * 指定游戏端口
     */
    public static final int port=34569;

    private ServerSocket serverSocket=null;
    private Socket socket=null;
    private Exception error;

    public Client() {}

    /**
     * 运行Client对象，意味着本机为服务端，等待客户端连接
     */
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            Main.log.addLog("failed to start client server-side:", this.getClass());
            Main.log.addLog(e, this.getClass());
            return;
        }
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Main.log.addLog("waiting for connection", this.getClass());
                var skt = serverSocket.accept();
                Main.log.addLog("received connection from:" + skt.getInetAddress(), this.getClass());
                if (socket != null) {
                    var os = new DataOutputStream(skt.getOutputStream());
                    os.writeUTF("disconnected");
                    skt.close();
                }
                socket = skt;
                Main.syncThread = new MapSyncThread(socket);
                this.interrupt();
            } catch (IOException e) {
                Main.log.addLog("exception in client server-side:", this.getClass());
                Main.log.addLog(e, this.getClass());

            }
        }
    }

    /**
     * 本机为客户端，主动连接服务端
     * @param ip 服务端ip地址
     */
    public void connectTo(String ip)  {
        Main.log.addLog("connecting to:"+ip,this.getClass());
        if(socket!=null) {
            Main.log.addLog("refused:"+ip+":a connection already exists",this.getClass());
            return;
        }
        try {
            socket = new Socket(ip, port);
        }catch (IOException e){
            this.error=e;
            return;
        }
        Main.log.addLog("connected to:"+ip,this.getClass());
    }

    /**
     * 如果成功连接，获取Socket对象。否则返回null
     * @return Socket对象（如果成功连接）
     */
    public Socket getSocket(){
        return socket;
    }
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * 如果连接失败，获取失败原因
     * @return 失败原因
     */
    public Exception getError() {
        return error;
    }
}
