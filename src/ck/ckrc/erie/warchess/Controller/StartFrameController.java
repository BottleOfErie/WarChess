package ck.ckrc.erie.warchess.Controller;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.ui.Play;
import ck.ckrc.erie.warchess.ui.Setting;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class StartFrameController {

    @FXML
    private Button MultiPlayer;

    @FXML
    private Button SingalPlayer;

    @FXML
    private AnchorPane StartController;
    @FXML
    public void ChooseOneSide(MouseEvent event) throws Exception{
        Engine.playerNum=2;
        Director.GetDirector().ChooseOneSide();
    }

    @FXML
    void SingalPlayerStart(MouseEvent event) {
        Play.gamemodel=0;
        //TODO change player Num
        Engine.playerNum=1;
        Setting.makesetting();
    }

}

