package ck.ckrc.erie.warchess.Controller;
import ck.ckrc.erie.warchess.ui.GameScene;
import ck.ckrc.erie.warchess.ui.Setting;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class SettingController {
    @FXML
    private Button back;

    @FXML
    private Button decompile;

    @FXML
    private Button loadclass;

    @FXML
    void Back(MouseEvent event) {
        GameScene.gameScene.setRoot(GameScene.anchorPane);
        Setting.checkloadedclass();
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
