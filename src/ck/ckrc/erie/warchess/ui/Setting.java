package ck.ckrc.erie.warchess.ui;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.PreMain;
import ck.ckrc.erie.warchess.game.ClassDecompilerWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

//TODO 挪到GameScene外面
public class Setting {
    private static Stage stage= Director.GetDirector().stage;
    private static AnchorPane anchorPane=GameScene.anchorPane;
    public static boolean candecompile=false;

    public static Map<Class<?>, Boolean> loadornot=new HashMap<>();
    public static boolean canloadclass =false;

    public static void makesetting() {
        try {
            Parent root = FXMLLoader.load(Setting.class.getResource("/Fxml/Setting.fxml"));
            root.setId("setting");
            stage.getScene().setRoot(root);
            initclassmap();
            initClass();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkloadedclass(){
        Play.classlist=new ArrayList<>();
        for(var clazz:loadornot.keySet()){
            if(loadornot.get(clazz)){Play.classlist.add(clazz);}
        }
    }
    public static void initclassmap(){
        Collection<Class<?>> classlist=Main.chessClassLoader.getChessClass();
        for(var item:classlist){
            loadornot.put(item, false);
        }
    }
    public static void initClass(){

        VBox notloadlist=(VBox) stage.getScene().lookup("#NotLoadClassList");
        VBox loadedlist=(VBox) stage.getScene().lookup("#LoadedClassList");
        notloadlist.getChildren().clear();
        loadedlist.getChildren().clear();
        for(var clazz:loadornot.keySet()){
            LabelWithChessClass label=new LabelWithChessClass(clazz.getName(),clazz.getSimpleName());
            label.setPrefSize(200, 50);
            label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,null,null)));
            setdecompile(label);
            if(loadornot.get(clazz)){
                loadedlist.getChildren().add(label);
                setupDragAndDrop(label, notloadlist);
            }
            else{
                notloadlist.getChildren().add(label);
                setupDragAndDrop(label, loadedlist);
            }
        }
    }
    private static void setupDragAndDrop(LabelWithChessClass label, VBox targetVBox) {
        label.setOnDragDetected(event -> {
            if(canloadclass){
                Dragboard db = label.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(label.getClazzSimple()+"<split>"+label.getClazz());
                db.setContent(content);
                event.consume();
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
                VBox notloadlist=(VBox) stage.getScene().lookup("#NotLoadClassList");
                VBox loadedlist=(VBox) stage.getScene().lookup("#LoadedClassList");
                var splitStrs=db.getString().split("<split>");
                Main.log.addLog("Selected Label:"+db.getString(),Setting.class);
                LabelWithChessClass newlabel=new LabelWithChessClass(splitStrs[1],splitStrs[0]);
                newlabel.setPrefSize(200, 50);
                newlabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,null,null)));
                targetVBox.getChildren().add(newlabel);
                Class<?> clazz=Main.chessClassLoader.getClassByName(newlabel.getClazz());
                if(targetVBox==notloadlist){
                    setupDragAndDrop(newlabel, loadedlist);
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
                    setupDragAndDrop(newlabel, notloadlist);
                    loadornot.put(clazz,true);
                    if(Main.syncThread!=null) {
                        try {
                            Main.syncThread.sendActive(true,newlabel.getClazz());
                        } catch (IOException e) {
                            Main.log.addLog("failed to send active!",Setting.class);
                        }
                    }
                }
                setdecompile(newlabel);
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
    private static void setdecompile(LabelWithChessClass label){
        label.setOnMouseClicked(event -> {
            if(candecompile) {
                String name = label.getClazz();
                byte[] arr = PreMain.transformer.map.get(name);
                ClassDecompilerWrapper wrapper = new ClassDecompilerWrapper(arr, name);
                Stage childStage = new Stage();
                childStage.initModality(Modality.WINDOW_MODAL);
                childStage.initOwner(stage);
                Label decompilelabel = new Label(wrapper.decompile());
                StackPane layout = new StackPane();
                layout.getChildren().add(decompilelabel);
                Scene childScene = new Scene(layout, 400, 200);
                childStage.setScene(childScene);
                childStage.setTitle("decompile");
                childStage.show();
            }
        });
    }
}
