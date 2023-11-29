package ck.ckrc.erie.warchess;

import ck.ckrc.erie.warchess.game.ChessClassLoader;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.net.MapSyncThread;
import ck.ckrc.erie.warchess.ui.CLIUserInterface;
import ck.ckrc.erie.warchess.utils.Logger;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

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
        FXMain.FXMain(args);
        CLIUserInterface.cli_main();

    }


}
