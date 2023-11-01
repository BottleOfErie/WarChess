package ck.ckrc.erie.warchess;

import ck.ckrc.erie.warchess.game.Engine;

public class Main {

    public static Engine currentGameEngine;

    public static void main(String... args){
        currentGameEngine=new Engine();
    }
}
