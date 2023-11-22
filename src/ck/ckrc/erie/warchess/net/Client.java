package ck.ckrc.erie.warchess.net;

import ck.ckrc.erie.warchess.Main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Client extends Thread{

    public static final int port=34569;


    private ServerSocket serverSocket=null;
    private Socket socket=null;
    private int teamFlag=-1;
    private boolean isCurrentTeam=false;


    public Client() throws IOException {

    }

    public void setCurrentTeam(int teamFlag){
        isCurrentTeam = teamFlag == this.teamFlag;
    }

    @Override
    public void run() {
        try {
            serverSocket=new ServerSocket(port);
        } catch (IOException e) {
            Main.log.addLog("failed to start client server-side:",this.getClass());
            Main.log.addLog(e,this.getClass());
        }
        while(true){
            Main.log.addLog("waiting for connection",this.getClass());
            try {
                var skt=serverSocket.accept();
                Main.log.addLog("received connection from:"+skt.getInetAddress(),this.getClass());
                if(socket!=null){
                    var os=new DataOutputStream(skt.getOutputStream());
                    os.writeUTF("disconnected");
                    skt.close();
                    continue;
                }
                socket=skt;
                Main.syncThread=new MapSyncThread(socket);
            } catch (IOException e) {
                Main.log.addLog("exception in client server-side:",this.getClass());
                Main.log.addLog(e,this.getClass());
            }
        }
    }

    public void connectTo(String ip) throws IOException {
        Main.log.addLog("connecting to:"+ip,this.getClass());
        if(socket!=null) {
            Main.log.addLog("refused:"+ip+":a connection already exists",this.getClass());
            return;
        }
        socket=new Socket(ip,port);
        Main.log.addLog("connected to:"+ip,this.getClass());
    }

    public Socket getSocket(){
        return socket;
    }

}
