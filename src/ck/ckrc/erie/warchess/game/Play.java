package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.example.GunTower;
import ck.ckrc.erie.warchess.example.Life;
import ck.ckrc.erie.warchess.example.Miner;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.lang.reflect.Field;
import java.util.Collection;

public class Play {
    private static GraphicsContext graphicsContext;

    private static int edgelength=GameScene.edgelength;
    private static int nodeboxheight= Director.height/Map.MapSize;

    static boolean[][] haschess = new boolean[10][10];
    boolean[][] isshoweddetail=new boolean[10][10];

    static boolean isshowedchoosechess=false;

    private static int teamflag=1;

    private static AnchorPane anchorPane=GameScene.anchorPane;
    private Collection<Class<?>> classlist=Main.chessClassLoader.getChessClass();

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
                        anchorPane.getChildren().remove(anchorPane.lookup("#button"+String.valueOf(i)));
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
                removechess(x, y);
            }
        }
    }
    public static void removechess(int x,int y){
        graphicsContext.clearRect(x*edgelength+10, y*edgelength+10, 40, 40);
        haschess[x][y]=false;
    }
    private void initshowchess(){
        for(int i=0; i<10;i++){
            for(int j=0;j<10;j++){
                isshoweddetail[i][j]=false;
            }
        }
    }
    private void showchoosechess(int x, int y){
        VBox root=new VBox();
        root.setPrefSize(200, 600);
        for(Class<?> clazz : classlist){
            //TODO 调整位置，避免不显示最上面的Node
            GridPane pane=new GridPane();
            Button button=new Button("choose");
            button.setOnAction(actionEvent -> {
                Chess chess=ChessClassInvoker.getNewInstance(clazz,Main.currentGameEngine.getPlayer(teamflag),x,y);
                Play.drawchess(chess.paint(),x,y);
                Main.currentGameEngine.setChess(x, y, chess);
                removechoosechess();
            });
            pane.addRow(0,ChessClassInvoker.invokeShowData(clazz));
            pane.addRow(1,button);
            root.getChildren().add(pane);
        }
        root.setId("choosechessroot");
        anchorPane.getChildren().add(root);
        root.setLayoutX(700);root.setLayoutY(0);
    }
    private static void removechoosechess(){
        try{
            Node node=anchorPane.lookup("#choosechessroot");
            anchorPane.getChildren().remove(node);
        }catch (IndexOutOfBoundsException e){}
    }
    public static void drawchess(Image image,int x, int y){
        if(image==null)return;
        Main.currentGameEngine.getMap().setChess(x, y, new GunTower(x, y, Main.currentGameEngine.getPlayer(teamflag)));
        graphicsContext.drawImage(image,x*60+10,y*60+10,40,40);
        haschess[x][y]=true;
        isshowedchoosechess=false;
    }

    private void removechessdetails(int x,int y, int number){
        Node pane=anchorPane.lookup("#root"+String.valueOf(number));
        Node button=anchorPane.lookup("#button"+String.valueOf(number));
        anchorPane.getChildren().removeAll(pane,button);
        for (int i = number+1; i <= showeddetailchesscount; i++) {
            Node element = anchorPane.lookup("#root" + String.valueOf(i));
            Node bt=anchorPane.lookup("#button"+String.valueOf(i));
            element.setId("root" + String.valueOf(i - 1));
            bt.setId("button"+String.valueOf(i-1));
            element.setLayoutY(70 * (i - 2));bt.setLayoutY(70 * (i - 2)+5);
        }
        showeddetailchesscount--;
        isshoweddetail[x][y]=false;
    }


    private void showchessdetails(int x, int y){
        GridPane root=(GridPane) Main.currentGameEngine.getMap().getChessMap()[x][y].showPanel();
        root.setPrefSize(200, nodeboxheight);
        Button button=new Button("隐藏");
        root.getChildren().add(button);
        button.setPrefSize(50, 20);
        anchorPane.getChildren().addAll(root,button);
        root.setLayoutX(700);root.setLayoutY(70*showeddetailchesscount);
        button.setLayoutX(830);button.setLayoutY(70*showeddetailchesscount+5);
        showeddetailchesscount++;
        button.setId("button"+String.valueOf(showeddetailchesscount));
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
