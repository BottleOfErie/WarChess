package ck.ckrc.erie.warchess;

import ck.ckrc.erie.warchess.ui.GameScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class FXMain extends Application {
    public static void FxMain(String[] arg){launch(arg);}

    @Override
    public void start(Stage stage) throws Exception {
        Director.GetDirector().Init(stage);
    }

    @Override
    public void stop() {
        GameScene.repainter.repainting=false;
    }
}