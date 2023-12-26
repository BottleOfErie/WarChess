package ck.ckrc.erie.warchess.Controller;

import ck.ckrc.erie.warchess.Director;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class GameOverController {

    @FXML
    void PlayAgain(ActionEvent event) throws Exception{
        Director.GetDirector().BackToStartFrame();
    }

    @FXML
    void QuikGame(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "你确定要退出游戏吗？", ButtonType.YES, ButtonType.NO);
        alert.setTitle("退出游戏");
        alert.setHeaderText(null);
        alert.setContentText("是否退出游戏?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
        }
    }

}
