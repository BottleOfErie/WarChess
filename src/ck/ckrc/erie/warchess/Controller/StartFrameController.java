package ck.ckrc.erie.warchess.Controller;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.ui.Play;
import ck.ckrc.erie.warchess.ui.Setting;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * 这个类用于控制设置界面
 */
public class StartFrameController {

    /**
     * 选择多人模式的按钮事件
     */
    @FXML
    public void ChooseOneSide(MouseEvent event) throws Exception{
        Engine.playerNum=2;
        Director.GetDirector().ChooseOneSide();
    }

    /**
     * 选择单人模式的按钮事件
     */
    @FXML
    void SingalPlayerStart(MouseEvent event) {
        Play.gamemodel=0;
        Setting.makesetting();
    }

}

