package ck.ckrc.erie.warchess.Controller;
import ck.ckrc.erie.warchess.Director;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StartFrameController {

    @FXML
    private Button MultiPlayer;

    @FXML
    private Button SingalPlayer;

    @FXML
    private AnchorPane StartController;
    @FXML
    public void ChooseOneSide(MouseEvent event) throws Exception{
        Director.GetDirector().ChooseOneSide();
    }

    @FXML
    void SingalPlayerStart(MouseEvent event) {
        Director.GetDirector().gameStart();
    }

}

