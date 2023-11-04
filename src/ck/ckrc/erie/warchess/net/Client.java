package ck.ckrc.erie.warchess.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Client{

    public static final int port=34569;


    private ServerSocket serverSocket=null;
    private Socket socket=null;
    private int teamFlag=-1;
    private Boolean isCurrentTeam=false;


    public Client() throws IOException {
        serverSocket=new ServerSocket(port);

    }

    public void setCurrentTeam(int teamFlag){
        isCurrentTeam = teamFlag == this.teamFlag;
    }

}
