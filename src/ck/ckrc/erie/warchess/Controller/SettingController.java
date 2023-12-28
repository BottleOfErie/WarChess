package ck.ckrc.erie.warchess.Controller;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.ui.GameScene;
import ck.ckrc.erie.warchess.ui.LabelWithChessClass;
import ck.ckrc.erie.warchess.ui.Setting;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class SettingController {
    @FXML
    private VBox LoadedClassList;

    @FXML
    private VBox NotLoadClassList;

    @FXML
    private Button decompile;

    @FXML
    private Button launch;

    @FXML
    private Button loadclass;
    @FXML
    void back(ActionEvent event) throws Exception{
        Director.GetDirector().Init(Director.GetDirector().stage);
    }

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
        LabelWithChessClass label=Setting.highlightedLabel;
        if(label==null){return;}
        Main.log.addLog("Selected Label:"+label.getText(),Setting.class);
        LabelWithChessClass newlabel=new LabelWithChessClass(label.getClazz(),label.getClazzSimple());
        VBox list=(VBox) label.getParent();
        if(list==Setting.notloadlist) {
            Setting.shownewlabel(newlabel, Setting.loadedlist);
            Setting.setupDragAndDrop(newlabel, Setting.notloadlist);
        }
        else{
            Setting.shownewlabel(newlabel, Setting.notloadlist);
            Setting.setupDragAndDrop(newlabel, Setting.loadedlist);
        }
        list.getChildren().remove(label);
    }
    @FXML
    void loadclassfromfile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择类");
        File initialDirectory = new File(".");
        fileChooser.setInitialDirectory(initialDirectory);
        var lst = fileChooser.showOpenMultipleDialog(GameScene.stage);
        if(lst==null)return;
        var files=new ArrayList<>(lst);
        files.sort(Comparator.comparingInt(o -> (o.getName().contains("$") ? 0 : 1)));
        for (var file:files) {
            var clazz=Main.chessClassLoader.loadChessClassFromFile(file);
            if(clazz==null){
                var dialog=new Alert(Alert.AlertType.WARNING);
                dialog.setContentText("非法的类文件:"+file);
                dialog.show();
                continue;
            }
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
