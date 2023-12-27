package ck.ckrc.erie.warchess;

import ck.ckrc.erie.warchess.game.ChessClassLoader;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.net.MapSyncThread;
import ck.ckrc.erie.warchess.utils.Logger;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;

public class Main {

    public static final File rootFile=new File("./erie");
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

        chessClassLoader=new ChessClassLoader();

        FXMain.FXMain(args);

    }
}
