package ck.ckrc.erie.warchess.ui;

import ck.ckrc.erie.warchess.Controller.SettingController;
import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Setting {
    private static Stage stage= Director.GetDirector().stage;
    private static AnchorPane anchorPane=GameScene.anchorPane;

    public static boolean candecompile=false;

    public static Map<Class<?>, Boolean> loadornot=new HashMap<>();
    public static VBox notloadlist,loadedlist;
    public static LabelWithChessClass highlightedLabel=null;
    public static Label classnamelabel=null;
    private static Scene settingscene=null;
    private static Parent root;

    public static void makesetting() {
        //TODO change to TreeView
        if(settingscene==null) {
            try {
                root = FXMLLoader.load(Setting.class.getResource("/Fxml/Setting.fxml"));
                root.setId("setting");
                if(Play.gamemodel==1) {
                    root.lookup("#teamhbox").setVisible(false);
                }else{root.lookup("#teamhbox").setVisible(true);}
                settingscene=new Scene(root);
                stage.setScene(settingscene);
            } catch (IOException | NullPointerException e) {
                Main.log.addLog("Cannot get this FX resource:" + "/Fxml/Setting.fxml", Setting.class);
                Main.log.addLog(e, Setting.class);
            }
        }else{
            if(Play.gamemodel==1) {
                root.lookup("#teamhbox").setVisible(false);
            }else{root.lookup("#teamhbox").setVisible(true);}
            stage.setScene(settingscene);
        }
    }

    public static void checkloadedclass(){
        Play.classlist=new ArrayList<>();
        for(var clazz:loadornot.keySet()){
            if(loadornot.get(clazz)){Play.classlist.add(clazz);}
        }
    }
    public static void initClass(){
        notloadlist = (VBox) stage.getScene().lookup("#NotLoadClassList");
        loadedlist = (VBox) stage.getScene().lookup("#LoadedClassList");
        classnamelabel = (Label) stage.getScene().lookup("#classdatalabel");
        notloadlist.getChildren().clear();
        loadedlist.getChildren().clear();
        for(var clazz:Main.chessClassLoader.getAllChessClass()){
            LabelWithChessClass label=new LabelWithChessClass(clazz.getName(),clazz.getSimpleName());
            label.setPrefSize(200, 50);
            label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,null,null)));
            if(!loadornot.containsKey(clazz))loadornot.put(clazz, false);
            if(loadornot.get(clazz)){
                loadedlist.getChildren().add(label);
                setupDragAndDrop(label, notloadlist);
            }
            else{
                notloadlist.getChildren().add(label);
                setupDragAndDrop(label, loadedlist);
            }
        }
        initlabel();
    }
    public static void setupDragAndDrop(LabelWithChessClass label, VBox targetVBox) {
        label.setOnDragDetected(event -> {
            Dragboard db = label.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(label.getClazzSimple() + "<split>" + label.getClazz());
            db.setContent(content);
            event.consume();
        });
        label.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    LabelWithChessClass newlabel=new LabelWithChessClass(label.getClazz(),label.getClazzSimple());
                    if (label.getParent() == notloadlist) {
                        shownewlabel(newlabel, loadedlist);
                        setupDragAndDrop(newlabel, notloadlist);
                    } else {
                        shownewlabel(newlabel, notloadlist);
                        setupDragAndDrop(newlabel, loadedlist);
                    }
                    ((VBox) label.getParent()).getChildren().remove(label);
                } else if (event.getClickCount() == 1) {
                    if (highlightedLabel != null) {
                        highlightedLabel.setStyle("");
                        classnamelabel.setText("");
                    }
                    highlightedLabel = label;
                    classnamelabel.setText(Main.chessClassLoader.getClassByName(highlightedLabel.getClazz()).getName());
                    label.setStyle("-fx-background-color: yellow;");
                }
            }
        });

        targetVBox.setOnDragOver(event -> {
            if (event.getGestureSource() != targetVBox && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        targetVBox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                var splitStrs=db.getString().split("<split>");
                Main.log.addLog("Selected Label:"+db.getString(),Setting.class);
                LabelWithChessClass newlabel=new LabelWithChessClass(splitStrs[1],splitStrs[0]);
                shownewlabel(newlabel, targetVBox);
                if(targetVBox==notloadlist){setupDragAndDrop(newlabel, loadedlist);}
                else{setupDragAndDrop(newlabel, notloadlist);}
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        label.setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                VBox list=(VBox) label.getParent();
                list.getChildren().remove(label);
            }
            event.consume();
        });
    }
    public static void initlabel(){
        ObservableList<Node> notloadclass = notloadlist.getChildren();
        ObservableList<Node> loadedclass = loadedlist.getChildren();
        for(var item:notloadclass){
            item.setStyle("");
        }
        for(var item:loadedclass){
            item.setStyle("");
        }
        highlightedLabel=null;
        classnamelabel.setText("");
    }
    public static void shownewlabel(LabelWithChessClass newlabel, VBox targetVBox){
        newlabel.setPrefSize(200, 50);
        newlabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,null,null)));
        targetVBox.getChildren().add(newlabel);
        Class<?> clazz=Main.chessClassLoader.getClassByName(newlabel.getClazz());
        if(targetVBox==notloadlist){
            loadornot.put(clazz,false);
            if(Main.syncThread!=null) {
                try {
                    Main.syncThread.sendActive(false,newlabel.getClazz());
                } catch (IOException e) {
                    Main.log.addLog("failed to send active!",Setting.class);
                }
            }
        }
        else{
            loadornot.put(clazz,true);
            if(Main.syncThread!=null) {
                try {
                    Main.syncThread.sendActive(true,newlabel.getClazz());
                } catch (IOException e) {
                    Main.log.addLog("failed to send active!",Setting.class);
                }
            }
        }
        initlabel();
    }
}
