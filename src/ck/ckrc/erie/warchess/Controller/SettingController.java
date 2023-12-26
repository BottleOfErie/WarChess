package ck.ckrc.erie.warchess.Controller;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.ui.GameScene;
import ck.ckrc.erie.warchess.ui.Setting;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

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
        Main.currentGameEngine=new Engine();
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
    @FXML
    void loadclassfromfile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请从example文件夹中选择类");
        File initialDirectory = new File(".");
        fileChooser.setInitialDirectory(initialDirectory);
        var lst = fileChooser.showOpenMultipleDialog(GameScene.stage);
        if(lst==null)return;
        var files=new ArrayList<>(lst);
        files.sort(Comparator.comparingInt(o -> (o.getName().contains("$") ? 0 : 1)));
        for (var file:files) {
            var clazz=Main.chessClassLoader.loadChessClassFromFile(file);
            if(Main.syncThread!=null) {
                try {
                    Main.syncThread.sendLoad(clazz.getName());
                } catch (IOException e) {
                    Main.log.addLog("Cannot sync this class:"+clazz,this.getClass());
                    Main.log.addLog(e,this.getClass());
                }
            }
            Setting.initClass();
        }
    }
}
