package ck.ckrc.erie.warchess;

import ck.ckrc.erie.warchess.example.GunTower;
import ck.ckrc.erie.warchess.example.Miner;
import ck.ckrc.erie.warchess.game.ChessClassLoader;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.game.Map;
import ck.ckrc.erie.warchess.game.Player;
import ck.ckrc.erie.warchess.net.Client;
import ck.ckrc.erie.warchess.net.MapSyncThread;
import ck.ckrc.erie.warchess.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Main {

    public static Engine currentGameEngine;
    public static ChessClassLoader chessClassLoader;

    public static Logger log;
    public static MapSyncThread syncThread=null;

    public static void main(String... args){
        try {
            log=new Logger();
            log.debug=true;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        currentGameEngine=new Engine();
        chessClassLoader=new ChessClassLoader();

        Scanner input=new Scanner(System.in);
        System.out.println("1 as host,2 as client");
        if(input.nextInt()==1){
            chessClassLoader.addClazz(GunTower.class);
            chessClassLoader.addClazz(Miner.class);
            try {
                Client client=new Client();
                client.start();
                System.out.println("waiting");
                input.nextInt();
                syncThread.start();
                currentGameEngine.setPlayer(0,Player.getNewPlayer(0));
                currentGameEngine.setPlayer(1,Player.getNewPlayer(1));
                currentGameEngine.nextRound(0);
                while(true){

                    while(currentGameEngine.getCurrentTeam()!=0) {
                        System.out.println("waiting");
                        input.nextInt();
                    }

                    for(int i=0;i< Map.MapSize;i++) {
                        for (int j = 0; j < Map.MapSize; j++)
                            if(currentGameEngine.getChess(i, j)!=null)
                                System.out.print(currentGameEngine.getChess(i, j).paint());
                            else
                                System.out.print('_');
                        System.out.println();
                    }

                    int x=input.nextInt(),y=input.nextInt();
                    do{
                        if(currentGameEngine.getChess(x,y)==null){
                            System.out.println("following options");
                            if(GunTower.checkPlaceRequirements(currentGameEngine.getPlayer(0)))
                                System.out.println("1:GunTower");
                            if(Miner.checkPlaceRequirements(currentGameEngine.getPlayer(0)))
                                System.out.println("2:Miner");
                            if(input.nextInt()==1){
                                if(GunTower.checkPlaceRequirements(currentGameEngine.getPlayer(0))) {
                                    GunTower.afterPlacement(currentGameEngine.getPlayer(0));
                                    currentGameEngine.setChess(x,y,new GunTower(x,y,currentGameEngine.getPlayer(0)));
                                }
                            }else{
                                if(Miner.checkPlaceRequirements(currentGameEngine.getPlayer(0))) {
                                    Miner.afterPlacement(currentGameEngine.getPlayer(0));
                                    currentGameEngine.setChess(x,y,new Miner(x,y,currentGameEngine.getPlayer(0)));
                                }
                            }
                        }else
                            System.out.println(x+","+y+":"+currentGameEngine.getChess(x,y));

                        syncThread.sendSync(x,y,currentGameEngine.getChess(x,y));
                        x=input.nextInt();
                        y=input.nextInt();

                    }while (x!=-1);

                    syncThread.sendRound(1);
                    currentGameEngine.nextRound(1);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            chessClassLoader.addClazz(GunTower.class);
            chessClassLoader.addClazz(Miner.class);
            try {
                Client client=new Client();
                client.connectTo("127.0.0.1");
                System.out.println("waiting");
                input.nextInt();
                syncThread=new MapSyncThread(client.getSocket());
                syncThread.start();
                currentGameEngine.setPlayer(0,Player.getNewPlayer(0));
                currentGameEngine.setPlayer(1,Player.getNewPlayer(1));
                while(true){
                    while(currentGameEngine.getCurrentTeam()!=1) {
                        System.out.println("waiting");
                        input.nextInt();
                    }

                    for(int i=0;i< Map.MapSize;i++) {
                        for (int j = 0; j < Map.MapSize; j++)
                            if(currentGameEngine.getChess(i, j)!=null)
                                System.out.print(currentGameEngine.getChess(i, j).paint());
                            else
                                System.out.print('_');
                        System.out.println();
                    }

                    int x=input.nextInt(),y=input.nextInt();
                    do{
                        if(currentGameEngine.getChess(x,y)==null){
                            System.out.println("following options");
                            if(GunTower.checkPlaceRequirements(currentGameEngine.getPlayer(1)))
                                System.out.println("1:GunTower");
                            if(Miner.checkPlaceRequirements(currentGameEngine.getPlayer(1)))
                                System.out.println("2:Miner");
                            if(input.nextInt()==1){
                                if(GunTower.checkPlaceRequirements(currentGameEngine.getPlayer(1))) {
                                    GunTower.afterPlacement(currentGameEngine.getPlayer(1));
                                    currentGameEngine.setChess(x,y,new GunTower(x,y,currentGameEngine.getPlayer(1)));
                                }
                            }else{
                                if(Miner.checkPlaceRequirements(currentGameEngine.getPlayer(1))) {
                                    Miner.afterPlacement(currentGameEngine.getPlayer(1));
                                    currentGameEngine.setChess(x,y,new Miner(x,y,currentGameEngine.getPlayer(1)));
                                }
                            }
                        }else
                            System.out.println(x+","+y+":"+currentGameEngine.getChess(x,y));

                        syncThread.sendSync(x,y,currentGameEngine.getChess(x,y));
                        x=input.nextInt();
                        y=input.nextInt();
                    }while (x!=-1);

                    syncThread.sendRound(0);
                    currentGameEngine.nextRound(0);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }
}
