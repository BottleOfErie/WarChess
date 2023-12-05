package ck.ckrc.erie.warchess.ui;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.PreMain;
import ck.ckrc.erie.warchess.example.Miner;
import ck.ckrc.erie.warchess.game.ChessClassLoader;
import ck.ckrc.erie.warchess.game.ClassDecompilerWrapper;
import ck.ckrc.erie.warchess.game.Map;
import ck.ckrc.erie.warchess.game.Play;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
import java.util.Set;

public class GameScene {
    public static final int canvasize=600,edgelength=canvasize/ Map.MapSize;
    private Canvas canvas = new Canvas(canvasize,canvasize );
    private GraphicsContext graphicsContext=canvas.getGraphicsContext2D();

    public static AnchorPane anchorPane;

    public static Scene gameScene;
    private Stage stage;

    public static boolean canloadclass =false;

    public static boolean candecompile=false;

    private int classcount=5;

    private boolean[] isload=new boolean[10];

    public void Init(Stage stage){
        AnchorPane anchorPane=new AnchorPane(canvas);
        this.anchorPane=anchorPane;
        anchorPane.setPrefWidth(Director.width);
        anchorPane.setPrefHeight(Director.height);
        canvas.setLayoutX(100);
        canvas.setLayoutY(0);
        drawchessmap();
        Scene scene=new Scene(anchorPane,Director.width,Director.height);
        anchorPane.setLayoutX(0);anchorPane.setLayoutY(0);
        canvas.setOnMouseClicked(new Play(graphicsContext).setChessAction);
        makesettingbutton();
        stage.setScene(scene);
        this.stage=stage;
        this.gameScene=stage.getScene();
    }
    private void drawchessmap(){
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(5);
        for(int i = 0; i <= 10; i++){
            graphicsContext.strokeLine(i*edgelength, 0, i*edgelength, canvasize);
            graphicsContext.strokeLine(0, i*edgelength, canvasize, i*edgelength);
        }
    }
    private void makesettingbutton(){
        Button button=new Button("setting");
        button.setPrefSize(75, 30);
        anchorPane.getChildren().add(button);
        button.setLayoutX(10);button.setLayoutY(50);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Parent root=FXMLLoader.load(this.getClass().getResource("/Fxml/Setting.fxml"));
                    root.setId("setting");
                    stage.getScene().setRoot(root);
                    initClass();
                }catch (IOException e){e.printStackTrace();}
            }
        });
    }
    private void initClass(){

        VBox notloadlist=(VBox) stage.getScene().lookup("#NotLoadClassList");
        VBox loadedlist=(VBox) stage.getScene().lookup("#LoadedClassList");
        Set<String> classname= Main.chessClassLoader.getChessClassNames();
        for(var item:classname){
            System.out.println(item);
            String[] it=item.split("[.]");
            Label label=new Label("classname:"+it[it.length-1]);
            label.setPrefSize(200, 50);
            label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,null,null)));
            setdecompile(label);
            notloadlist.getChildren().add(label);
            setupDragAndDrop(label, loadedlist);
        }
    }
    private void setupDragAndDrop(Label label, VBox targetVBox) {
        label.setOnDragDetected(event -> {
            if(canloadclass){
                Dragboard db = label.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(label.getText());
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
                Label newlabel=new Label(db.getString());
                newlabel.setPrefSize(200, 50);
                newlabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,null,null)));
                targetVBox.getChildren().add(newlabel);
                if(targetVBox==notloadlist){setupDragAndDrop(newlabel, loadedlist);}
                else{setupDragAndDrop(newlabel, notloadlist);}
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
    private void setdecompile(Label label){
        label.setOnMouseClicked(event -> {
            if(candecompile) {
                String text = label.getText();
                String[] te = text.split("[:]");
                String name = Main.chessClassLoader.getChessClassName(Main.chessClassLoader.getClassByName("ck.ckrc.erie.warchess.example."+te[1]));
                byte[] arr = PreMain.transformer.map.get(name);
                ClassDecompilerWrapper wrapper = new ClassDecompilerWrapper(arr, name);
                Stage childStage = new Stage();
                childStage.initModality(Modality.WINDOW_MODAL);
                childStage.initOwner(stage);

                // 在子窗口中添加一个标签
                Label decompilelabel = new Label(wrapper.decompile());
                StackPane layout = new StackPane();
                layout.getChildren().add(decompilelabel);

                Scene childScene = new Scene(layout, 400, 200);
                childStage.setScene(childScene);
                childStage.setTitle("decompile");

                // 显示子窗口
                childStage.show();
            }
        });
    }
}
