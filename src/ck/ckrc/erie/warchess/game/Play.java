package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.ui.GameScene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.awt.*;

public class Play {
    private GraphicsContext graphicsContext;

    private int edgelength=GameScene.edgelength;
    private int nodeboxheight= Director.height/Map.MapSize;

    boolean[][] haschess = new boolean[10][10];
    boolean[][] isshoweddetail=new boolean[10][10];

    private AnchorPane anchorPane=GameScene.anchorPane;

    private int showeddetailchesscount=0;
    public Play(GraphicsContext graphicsContext){ this.graphicsContext=graphicsContext; }

    public SetChessAction setChessAction=new SetChessAction();
    public class SetChessAction implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent event){
            int x=(int)(event.getX() / edgelength);
            int y=(int)(event.getY() / edgelength);
            if(event.getButton().name().equals(MouseButton.PRIMARY.name())){
                if(!haschess[x][y]){
                    drawchess(x, y);
                    haschess[x][y]=true;
                }
                else{
                    if(showeddetailchesscount<=9 && !isshoweddetail[x][y]){showchessdetails(x,y );}
                }
            }
            if(event.getButton().name().equals(MouseButton.SECONDARY.name())){
                graphicsContext.clearRect(x*edgelength+10, y*edgelength+10, 40, 40);
                haschess[x][y]=false;
            }
        }
    }

    private void drawchess(int x, int y){
        Image image=new Image("/Images/OIP.jpg");
        graphicsContext.drawImage(image,x*60+10,y*60+10,40,40);
    }

    private void showchessdetails(int x, int y){
        AnchorPane root = new AnchorPane();
        root.setPrefHeight(nodeboxheight);
        root.setPrefWidth(200);
        Label height=new Label("height");
        height.setPrefSize(100, 20);
        Label position=new Label("坐标:(" + String.valueOf(x) + "," + String.valueOf(y)+")");
        position.setPrefSize(100, 20);
        Button button=new Button("隐藏");
        button.setPrefSize(50, 20);
        root.getChildren().addAll(height,position,button);
        height.setLayoutX(10);height.setLayoutY(40);
        position.setLayoutX(10);height.setLayoutY(10);
        button.setLayoutX(120);button.setLayoutY(10);
        anchorPane.getChildren().add(root);
        root.setLayoutX(700);
        root.setLayoutY(70*showeddetailchesscount);
        showeddetailchesscount++;
        button.setId("button"+String.valueOf(showeddetailchesscount));
        position.setId(String.valueOf(x) + "," + String.valueOf(y));
        root.setId("root"+String.valueOf(showeddetailchesscount));
        isshoweddetail[x][y]=true;
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name=button.getId();
                int number=(int)(name.charAt(name.length()-1))-(int)('0');
                anchorPane.getChildren().remove(number);
                for (int i = number+1; i <= showeddetailchesscount; i++) {
                    Node element = anchorPane.lookup("#root" + String.valueOf(i));
                    Node bt=anchorPane.lookup("#button"+String.valueOf(i));
                    element.setId("root" + String.valueOf(i - 1));
                    bt.setId("button"+String.valueOf(i-1));
                    element.setLayoutY(edgelength * (i - 2));
                }
                showeddetailchesscount--;
                isshoweddetail[x][y]=false;
            }
        });
    }
}
