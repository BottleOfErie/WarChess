package ck.ckrc.erie.warchess;

import javafx.application.Application;
import javafx.stage.Stage;

public class FXMain extends Application {
    public static void FXMain(String[] arg){launch(arg);}

    @Override
    public void start(Stage stage) throws Exception {
        Director.GetDirector().Init(stage);
    }
}