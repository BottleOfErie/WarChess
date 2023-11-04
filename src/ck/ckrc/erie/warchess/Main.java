package ck.ckrc.erie.warchess;

import ck.ckrc.erie.warchess.example.GunTower;
import ck.ckrc.erie.warchess.example.Miner;
import ck.ckrc.erie.warchess.game.ChessClassLoader;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.utils.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class Main {

    public static Engine currentGameEngine;
    public static ChessClassLoader chessClassLoader;

    public static Logger log;

    public static void main(String... args){
        try {
            log=new Logger();
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentGameEngine=new Engine();
        chessClassLoader=new ChessClassLoader();

        chessClassLoader.addClazz(GunTower.class);
        chessClassLoader.addClazz(Miner.class);
    }
}
