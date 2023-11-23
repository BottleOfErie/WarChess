package ck.ckrc.erie.warchess.ui;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.example.GunTower;
import ck.ckrc.erie.warchess.example.Miner;
import ck.ckrc.erie.warchess.game.Map;
import ck.ckrc.erie.warchess.game.Player;
import ck.ckrc.erie.warchess.net.Client;
import ck.ckrc.erie.warchess.net.MapSyncThread;

import java.io.IOException;
import java.util.Scanner;

public class CLIUserInterface {

    public static void cli_main(){

        Scanner input=new Scanner(System.in);
        System.out.println("1 as host,2 as client");
        if(input.nextInt()==1){
            Main.chessClassLoader.addChessClass(GunTower.class);
            Main.chessClassLoader.addChessClass(Miner.class);
            try {
                Client client=new Client();
                client.start();
                System.out.println("waiting");
                input.nextInt();
                Main.syncThread.start();
                Main.syncThread.sendLoad(GunTower.className);
                Main.syncThread.sendLoad(Miner.className);
                Main.currentGameEngine.setPlayer(0, Player.getNewPlayer(0));
                Main.currentGameEngine.setPlayer(1,Player.getNewPlayer(1));
                Main.currentGameEngine.nextRound(0);
                while(true){

                    while(Main.currentGameEngine.getCurrentTeam()!=0) {
                        System.out.println("waiting");
                        input.nextInt();
                    }

                    for(int i = 0; i< Map.MapSize; i++) {
                        for (int j = 0; j < Map.MapSize; j++)
                            if(Main.currentGameEngine.getChess(i, j)!=null)
                                System.out.print(Main.currentGameEngine.getChess(i, j).paint());
                            else
                                System.out.print('_');
                        System.out.println();
                    }

                    int x=input.nextInt(),y=input.nextInt();
                    do{
                        if(Main.currentGameEngine.getChess(x,y)==null){
                            System.out.println("following options");
                            if(GunTower.checkPlaceRequirements(Main.currentGameEngine.getPlayer(0)))
                                System.out.println("1:GunTower");
                            if(Miner.checkPlaceRequirements(Main.currentGameEngine.getPlayer(0)))
                                System.out.println("2:Miner");
                            if(input.nextInt()==1){
                                if(GunTower.checkPlaceRequirements(Main.currentGameEngine.getPlayer(0))) {
                                    GunTower.afterPlacement(Main.currentGameEngine.getPlayer(0));
                                    Main.currentGameEngine.setChess(x,y,new GunTower(x,y,Main.currentGameEngine.getPlayer(0)));
                                }
                            }else{
                                if(Miner.checkPlaceRequirements(Main.currentGameEngine.getPlayer(0))) {
                                    Miner.afterPlacement(Main.currentGameEngine.getPlayer(0));
                                    Main.currentGameEngine.setChess(x,y,new Miner(x,y,Main.currentGameEngine.getPlayer(0)));
                                }
                            }
                        }else
                            System.out.println(x+","+y+":"+Main.currentGameEngine.getChess(x,y));

                        Main.syncThread.sendSync(x,y,Main.currentGameEngine.getChess(x,y));
                        x=input.nextInt();
                        y=input.nextInt();

                    }while (x!=-1);

                    Main.syncThread.sendRound(1);
                    Main.currentGameEngine.nextRound(1);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                Client client=new Client();
                client.connectTo("127.0.0.1");
                System.out.println("waiting");
                input.nextInt();
                Main.syncThread=new MapSyncThread(client.getSocket());
                Main.syncThread.start();
                Main.currentGameEngine.setPlayer(0,Player.getNewPlayer(0));
                Main.currentGameEngine.setPlayer(1,Player.getNewPlayer(1));
                while(true){
                    while(Main.currentGameEngine.getCurrentTeam()!=1) {
                        System.out.println("waiting");
                        input.nextInt();
                    }

                    for(int i=0;i< Map.MapSize;i++) {
                        for (int j = 0; j < Map.MapSize; j++)
                            if(Main.currentGameEngine.getChess(i, j)!=null)
                                System.out.print(Main.currentGameEngine.getChess(i, j).paint());
                            else
                                System.out.print('_');
                        System.out.println();
                    }

                    int x=input.nextInt(),y=input.nextInt();
                    do{
                        if(Main.currentGameEngine.getChess(x,y)==null){
                            System.out.println("following options");
                            if(GunTower.checkPlaceRequirements(Main.currentGameEngine.getPlayer(1)))
                                System.out.println("1:GunTower");
                            if(Miner.checkPlaceRequirements(Main.currentGameEngine.getPlayer(1)))
                                System.out.println("2:Miner");
                            if(input.nextInt()==1){
                                if(GunTower.checkPlaceRequirements(Main.currentGameEngine.getPlayer(1))) {
                                    GunTower.afterPlacement(Main.currentGameEngine.getPlayer(1));
                                    Main.currentGameEngine.setChess(x,y,new GunTower(x,y,Main.currentGameEngine.getPlayer(1)));
                                }
                            }else{
                                if(Miner.checkPlaceRequirements(Main.currentGameEngine.getPlayer(1))) {
                                    Miner.afterPlacement(Main.currentGameEngine.getPlayer(1));
                                    Main.currentGameEngine.setChess(x,y,new Miner(x,y,Main.currentGameEngine.getPlayer(1)));
                                }
                            }
                        }else
                            System.out.println(x+","+y+":"+Main.currentGameEngine.getChess(x,y));

                        Main.syncThread.sendSync(x,y,Main.currentGameEngine.getChess(x,y));
                        x=input.nextInt();
                        y=input.nextInt();
                    }while (x!=-1);

                    Main.syncThread.sendRound(0);
                    Main.currentGameEngine.nextRound(0);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
