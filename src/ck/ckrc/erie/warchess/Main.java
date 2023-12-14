package ck.ckrc.erie.warchess;

import ck.ckrc.erie.warchess.example.GunTower;
import ck.ckrc.erie.warchess.example.Miner;
import ck.ckrc.erie.warchess.example.Life;
import ck.ckrc.erie.warchess.game.*;
import ck.ckrc.erie.warchess.net.MapSyncThread;
import ck.ckrc.erie.warchess.utils.Logger;
import javafx.scene.control.Alert;

import java.io.IOException;

public class Main {

    public static Engine currentGameEngine;
    public static ChessClassLoader chessClassLoader;

    public static Logger log=null;
    public static MapSyncThread syncThread=null;

    public static void main(String... args){
        try {
            log=new Logger();
            log.debug=true;
        } catch (IOException e) {
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Failed to initialize the logger");
            alert.show();
        }

        currentGameEngine=new Engine();
        chessClassLoader=new ChessClassLoader();
        chessClassLoader.addChessClass(Miner.class);
        chessClassLoader.addChessClass(GunTower.class);
        chessClassLoader.addChessClass(Life.class);
        FXMain.FXMain(args);

    }
}
