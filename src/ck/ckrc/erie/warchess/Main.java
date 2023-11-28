package ck.ckrc.erie.warchess;

import ck.ckrc.erie.warchess.example.GunTower;
import ck.ckrc.erie.warchess.example.Miner;
import ck.ckrc.erie.warchess.game.ChessClassLoader;
import ck.ckrc.erie.warchess.game.ClassDecompilerWrapper;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.net.MapSyncThread;
import ck.ckrc.erie.warchess.ui.CLIUserInterface;
import ck.ckrc.erie.warchess.utils.Logger;
import org.jd.core.v1.ClassFileToJavaSourceDecompiler;
import org.jd.gui.util.decompiler.ClassPathLoader;
import org.jd.gui.util.decompiler.StringBuilderPrinter;

import java.io.*;

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
        }

        currentGameEngine=new Engine();
        chessClassLoader=new ChessClassLoader();

        try {
            File f=new File("WarChess\\ck\\ckrc\\erie\\warchess\\example\\GunTower.class");
            FileInputStream fis=new FileInputStream(f);
            var arr=fis.readAllBytes();
            ClassDecompilerWrapper wrapper=new ClassDecompilerWrapper(arr,"ck.ckrc.erie.warchess.example.GunTower");
            System.out.println(wrapper.decompile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //CLIUserInterface.cli_main();

    }
}
