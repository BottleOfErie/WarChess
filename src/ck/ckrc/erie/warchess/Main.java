package ck.ckrc.erie.warchess;

import ck.ckrc.erie.warchess.game.ChessClassLoader;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.utils.Logger;

import java.io.IOException;

public class Main {

    public static Engine currentGameEngine;
    public static ChessClassLoader chessClassLoader;

    public static Logger log;

    public static void main(String... args){
        currentGameEngine=new Engine();
        chessClassLoader=new ChessClassLoader();
        try {
            log=new Logger();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
