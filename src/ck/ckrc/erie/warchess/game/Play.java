package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.example.GunTower;
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
import javafx.scene.layout.GridPane;

import java.awt.*;

public class Play {
    private GraphicsContext graphicsContext;

    private int edgelength=GameScene.edgelength;
    private int nodeboxheight= Director.height/Map.MapSize;

    boolean[][] haschess = new boolean[10][10];
    boolean[][] isshoweddetail=new boolean[10][10];

    boolean isshowedchoosechess=false;

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
                    for(int i = 1;i<=showeddetailchesscount;i++){
                        anchorPane.getChildren().remove(anchorPane.lookup("#root"+String.valueOf(i)));
                    }
                    initshowchess();showeddetailchesscount=0;
                    if(!isshowedchoosechess){
                        showchoosechess(x, y);
                        isshowedchoosechess=true;
                    }
                    else{
                        removechoosechess();
                        isshowedchoosechess=false;
                    }

                }
                else{
                    if(showeddetailchesscount<=9 && !isshoweddetail[x][y] && !isshowedchoosechess){
                        showchessdetails(x,y );
                    }
                }
            }
            if(event.getButton().name().equals(MouseButton.SECONDARY.name())){
                graphicsContext.clearRect(x*edgelength+10, y*edgelength+10, 40, 40);
                haschess[x][y]=false;
            }
        }
    }
    private void initshowchess(){
        for(int i=0; i<10;i++){
            for(int j=0;j<10;j++){
                isshoweddetail[i][j]=false;
            }
        }
    }
    private void showchoosechess(int x, int y){
        AnchorPane root=new AnchorPane();
        root.setPrefSize(200, 600);
        Label guntowername=new Label("guntower");
        Button guntowerbutton=new Button("choose");
        guntowerbutton.setPrefSize(30, 20);
        guntowername.setPrefSize(150, 40);
        Label guntowerdamage=new Label("damage:");
        guntowerdamage.setPrefSize(150, 40);
        Label guntowercost=new Label("cost:");
        guntowercost.setPrefSize(150, 40);
        Label guntowerhp=new Label("hp:");
        guntowerhp.setPrefSize(150, 40);
        Label minername=new Label("miner");
        minername.setPrefSize(150, 40);
        Label minerdamage=new Label("damage:");
        minerdamage.setPrefSize(150, 40);
        Label minercost=new Label("cost:");
        minercost.setPrefSize(150, 40);
        Label minerhp=new Label("hp:");
        minerhp.setPrefSize(150, 40);
        Button minerbutton=new Button("choose");
        minerbutton.setPrefSize(30, 20);
        root.getChildren().addAll(guntowername,guntowerbutton,guntowerdamage,guntowercost,guntowerhp,minername,minerbutton,minerdamage,minercost,minerhp);
        guntowername.setLayoutX(10);guntowername.setLayoutY(10);
        guntowerbutton.setLayoutX(165);guntowerbutton.setLayoutY(10);
        guntowerdamage.setLayoutX(10);guntowerdamage.setLayoutY(60);
        guntowercost.setLayoutX(10);guntowercost.setLayoutY(110);
        guntowerhp.setLayoutX(10);guntowerhp.setLayoutY(160);
        minername.setLayoutX(10);minername.setLayoutY(210);
        minerbutton.setLayoutX(165);minerbutton.setLayoutY(210);
        minerdamage.setLayoutX(10);minerdamage.setLayoutY(260);
        minercost.setLayoutX(10);minercost.setLayoutY(310);
        minerhp.setLayoutX(10);minerhp.setLayoutY(360);
        guntowerbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                removechoosechess();
                Image image=new Image("/Images/OIP.jpg");
                drawchess(image, x,y);
                isshowedchoosechess=false;
            }
        });
        minerbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                removechoosechess();
                Image image=new Image("/Images/R.jpg");
                drawchess(image, x,y);
                isshowedchoosechess=false;
            }
        });
        root.setId("choosechessroot");
        anchorPane.getChildren().add(root);
        root.setLayoutX(700);root.setLayoutY(0);
    }
    private void removechoosechess(){
        try{
            anchorPane.getChildren().remove(2);
        }catch (IndexOutOfBoundsException e){}
    }
    private void drawchess(Image image,int x, int y){
        graphicsContext.drawImage(image,x*60+10,y*60+10,40,40);
        haschess[x][y]=true;
    }

    private void removechessdetails(int x,int y, int number){
        anchorPane.getChildren().remove(number+1);
        for (int i = number+1; i <= showeddetailchesscount; i++) {
            Node element = anchorPane.lookup("#root" + String.valueOf(i));
            Node bt=anchorPane.lookup("#button"+String.valueOf(i));
            element.setId("root" + String.valueOf(i - 1));
            bt.setId("button"+String.valueOf(i-1));
            element.setLayoutY(70 * (i - 2));
        }
        showeddetailchesscount--;
        isshoweddetail[x][y]=false;
    }


    private void showchessdetails(int x, int y){


        AnchorPane root = new AnchorPane();
        root.setPrefHeight(nodeboxheight);
        root.setPrefWidth(200);
        Label type=new Label("chess type:");
        type.setPrefSize(100, 10);
        Label position=new Label("坐标:(" + String.valueOf(x) + "," + String.valueOf(y)+")");
        position.setPrefSize(100, 10);
        Button button=new Button("隐藏");
        button.setPrefSize(50, 20);
        root.getChildren().addAll(type,position,button);
        type.setLayoutX(10);type.setLayoutY(0);
        position.setLayoutX(10);position.setLayoutY(20);
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
                removechessdetails(x,y,  number);
            }
        });
    }
}
