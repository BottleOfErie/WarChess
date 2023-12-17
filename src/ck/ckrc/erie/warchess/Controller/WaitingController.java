package ck.ckrc.erie.warchess.Controller;

import ck.ckrc.erie.warchess.Director;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class WaitingController {

    @FXML
    void Back(ActionEvent event) throws Exception{
        ChooseOneSideController.client.interrupt();
        if(ChooseOneSideController.client.getServerSocket()!=null){ChooseOneSideController.client.getServerSocket().close();}
        ChooseOneSideController.checkConnect.interrupt();
        Director.GetDirector().ChooseOneSide();
    }

}
