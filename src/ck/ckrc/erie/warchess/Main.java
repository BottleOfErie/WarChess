package ck.ckrc.erie.warchess;

import ck.ckrc.erie.warchess.example.GunTower;
import ck.ckrc.erie.warchess.example.Miner;
import ck.ckrc.erie.warchess.example.Life;
import ck.ckrc.erie.warchess.game.*;
import ck.ckrc.erie.warchess.net.MapSyncThread;
import ck.ckrc.erie.warchess.utils.Logger;
import javafx.scene.control.Alert;
import javafx.scene.media.AudioClip;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    public static final File rootFile=new File("D:\\erie\\");
    public static Engine currentGameEngine;
    public static ChessClassLoader chessClassLoader;

    public static Logger log=null;
    public static MapSyncThread syncThread=null;

    public static void main(String... args){
        if(!rootFile.exists())
            rootFile.mkdirs();
        try {
            log=new Logger();
            log.debug=true;
        } catch (IOException e) {
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Failed to initialize the logger");
            alert.show();
        }

//        try {
//            File f=new File("E:\\CloudMusic\\ARForest - Farewell.mp3");
//            URL url=f.toURI().toURL();
//            AudioClip ac=new AudioClip(url.toExternalForm());
//            ac.play();
//            Thread.sleep(10*1000);
//        } catch (MalformedURLException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        currentGameEngine=new Engine();
        chessClassLoader=new ChessClassLoader();
        chessClassLoader.addChessClass(Miner.class);
        chessClassLoader.addChessClass(GunTower.class);
        chessClassLoader.addChessClass(Life.class);
        FXMain.FXMain(args);

    }
}
