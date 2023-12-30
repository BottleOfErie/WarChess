package ck.ckrc.erie.warchess.Controller;

import ck.ckrc.erie.warchess.Director;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * 这个类用于控制选择服务端后的等待界面
 */
public class WaitingController {
    /**
     * 点击返回按钮后，中断等待
     */
    @FXML
    void Back(ActionEvent event) throws Exception{
        ChooseOneSideController.client.interrupt();
        if(ChooseOneSideController.client.getServerSocket()!=null){ChooseOneSideController.client.getServerSocket().close();}
        ChooseOneSideController.checkConnect.interrupt();
        Director.GetDirector().ChooseOneSide();
        ChooseOneSideController.isserverchoose=false;
    }

}
