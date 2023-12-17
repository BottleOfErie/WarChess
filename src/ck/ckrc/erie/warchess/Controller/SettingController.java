package ck.ckrc.erie.warchess.Controller;
import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.ui.GameScene;
import ck.ckrc.erie.warchess.ui.Setting;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class SettingController {
    @FXML
    private Button back;

    @FXML
    private Button decompile;

    @FXML
    private Button loadclass;

    @FXML
    void GameStart(MouseEvent event) {
        Setting.checkloadedclass();
        Director.GetDirector().gameStart();
        Main.currentGameEngine.nextRound(0);
        if(Main.syncThread!=null)
            try {
                Main.syncThread.sendRound(0);
            } catch (IOException e) {
                Main.log.addLog("Failed to sendRound,cannot start game!",this.getClass());
                Main.log.addLog(e,this.getClass());
            }
    }

    @FXML
    void DeCompile(MouseEvent event) {
        double opacity=decompile.getOpacity();
        if(opacity==1){decompile.setOpacity(0.5);Setting.candecompile =true;}
        else{decompile.setOpacity(1);Setting.candecompile =false;}
    }

    @FXML
    void LoadClass(MouseEvent event) {
        double opacity=loadclass.getOpacity();
        if(opacity==1){loadclass.setOpacity(0.5);Setting.canloadclass =true;}
        else{loadclass.setOpacity(1);Setting.canloadclass =false;}
    }
}
