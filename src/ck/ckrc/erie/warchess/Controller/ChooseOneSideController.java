package ck.ckrc.erie.warchess.Controller;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.example.GunTower;
import ck.ckrc.erie.warchess.example.Miner;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.game.Player;
import ck.ckrc.erie.warchess.net.Client;
import ck.ckrc.erie.warchess.net.MapSyncThread;
import ck.ckrc.erie.warchess.ui.CLIUserInterface;
import ck.ckrc.erie.warchess.ui.Play;
import ck.ckrc.erie.warchess.ui.StartFrame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ChooseOneSideController {

    @FXML
    private Button BackToStartButton;

    @FXML
    private Button ClientButton;

    @FXML
    private Button ServerButton;

    public static Node node;

    public static Client client;

    @FXML
    void BackToStartFrame(MouseEvent event) throws Exception{
        Director.GetDirector().BackToStartFrame();
    }

    @FXML
    void StartGameAsClient(MouseEvent event) {
        Play.gamemodel=1;
        try {
            Client client = new Client();
            this.client=client;
            Stage stage=Director.GetDirector().stage;
            //TODO change IP
            try {
                Node root = FXMLLoader.load(StartFrame.class.getResource("/Fxml/ClientIP.fxml"));
                this.node=root;
                Scene scene=new Scene((Parent) root);
                stage.setScene(scene);

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("waiting");
        }catch (IOException e) {
            return;
        }
    }
    @FXML
    void StartGameAsServer(MouseEvent event) {
        Play.gamemodel=1;
        try {
            Client client=new Client();
            client.start();
            //TODO better waiting scene
            while(Main.syncThread==null)Thread.sleep(10);
            Main.syncThread.start();
            Main.currentGameEngine.setPlayer(0, Player.getNewPlayer(0));
            Play.teamflag=0;

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Director.GetDirector().gameStart();
    }

}

