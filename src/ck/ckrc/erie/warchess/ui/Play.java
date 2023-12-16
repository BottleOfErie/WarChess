package ck.ckrc.erie.warchess.ui;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.Chess;
import ck.ckrc.erie.warchess.game.ChessClassInvoker;
import ck.ckrc.erie.warchess.game.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

public class Play {
    private static GraphicsContext graphicsContext;

    private static int edgelength=GameScene.edgelength;
    private static int nodeboxheight= Director.height/ Map.MapSize;

    static boolean[][] isshoweddetail=new boolean[Map.MapSize][Map.MapSize];

    static boolean isshowedchoosechess=false;

    public static AnchorPane anchorPane;
    public static Collection<Class<?>> classlist=new ArrayList<>();

    public static int gamemodel;
    public static long lastRepaintTime= System.currentTimeMillis();
    public static int teamflag;

    private static int showeddetailchesscount=0;
    public Play(GraphicsContext graphicsContext){ this.graphicsContext=graphicsContext; }

    public SetChessAction setChessAction=new SetChessAction();
    public class SetChessAction implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent event){
            if(gamemodel==0 || (gamemodel==1&&teamflag==Main.currentGameEngine.getCurrentTeam())) {
                int x = (int) (event.getX() / edgelength);
                int y = (int) (event.getY() / edgelength);
                if (event.getButton().name().equals(MouseButton.PRIMARY.name())) {
                    if (Main.currentGameEngine.getChess(x, y) == null) {
                        for (int i = 1; i <= showeddetailchesscount; i++) {
                            anchorPane.getChildren().remove(anchorPane.lookup("#root" + String.valueOf(i)+','+String.valueOf(x)+','+String.valueOf(y)));
                            anchorPane.getChildren().remove(anchorPane.lookup("#button" + String.valueOf(i)));
                        }
                        initshowchess();
                        showeddetailchesscount = 0;
                        if (!isshowedchoosechess) {
                            showchoosechess(x, y);
                            isshowedchoosechess = true;
                        } else {
                            removechoosechess();
                            isshowedchoosechess = false;
                        }

                    } else {
                        if (showeddetailchesscount <= 9 && !isshoweddetail[x][y] && !isshowedchoosechess) {
                            showchessdetails(x, y);
                        }
                    }
                }
                if (event.getButton().name().equals(MouseButton.SECONDARY.name())) {
                    Main.currentGameEngine.setChess(x, y, null);
                    drawAllChess();
                }
            }
        }
    }
    private void initshowchess(){
        for(int i=0; i<Map.MapSize;i++){
            for(int j=0;j<Map.MapSize;j++){
                isshoweddetail[i][j]=false;
            }
        }
    }
    private void showchoosechess(int x, int y){
        VBox root=new VBox();
        root.setPrefSize(200, 600);
        for(Class<?> clazz : classlist){
            GridPane pane=new GridPane();
            Button button=new Button("choose");
            button.setOnAction(actionEvent -> {
                Chess chess= ChessClassInvoker.getNewInstance(clazz,Main.currentGameEngine.getPlayer(Main.currentGameEngine.getCurrentTeam()),x,y);
                Main.currentGameEngine.setChess(x, y, chess);
                removechoosechess();
                Play.drawAllChess();
                isshowedchoosechess=false;
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
    public static void drawAllChess(){
        synchronized (graphicsContext){
            //TODO 特效层画布
            var now=System.currentTimeMillis();
            for(int i=0;i<Map.MapSize;i++)
                for(int j=0;j<Map.MapSize;j++){
                    var chess=Main.currentGameEngine.getChess(i,j);
                    if(chess==null)graphicsContext.clearRect(i*edgelength+10, j*edgelength+10, 40, 40);
                    else graphicsContext.drawImage(chess.paint(),i*60+10,j*60+10,40,40);
                }
            lastRepaintTime=now;
        }
    }

    private static void removechessdetails(int x,int y, int number){
        Node pane=anchorPane.lookup("#root"+String.valueOf(number)+','+String.valueOf(x)+','+String.valueOf(y));
        Node button=anchorPane.lookup("#button"+String.valueOf(number));
        anchorPane.getChildren().removeAll(pane,button);
        for (int i = number+1; i <= showeddetailchesscount; i++) {
            Node element = anchorPane.lookup("#root" + String.valueOf(i));
            Node bt=anchorPane.lookup("#button"+String.valueOf(i));
            element.setId("root" + String.valueOf(i - 1)+','+String.valueOf(x)+','+String.valueOf(y));
            bt.setId("button"+String.valueOf(i-1));
            element.setLayoutY(70 * (i - 2));bt.setLayoutY(70 * (i - 2)+5);
        }
        showeddetailchesscount--;
        isshoweddetail[x][y]=false;
    }


    private static void showchessdetails(int x, int y){
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
        root.setId("root"+String.valueOf(showeddetailchesscount)+','+String.valueOf(x)+','+String.valueOf(y));
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
    public static void updatechessdetails(){
        for(int i=0;i< Map.MapSize;i++){
            for(int j=0;j<Map.MapSize;j++){
                if(isshoweddetail[i][j]){
                    for(int k=1;k<=showeddetailchesscount;k++){
                        Node element = anchorPane.lookup("#root" + String.valueOf(k)+','+String.valueOf(i)+','+String.valueOf(j));
                        anchorPane.getChildren().remove(element);
                        if(element !=null){
                            String[] position=element.getId().split("[,]");
                            int x=(int)position[1].charAt(0)-(int)'0',y=(int)position[2].charAt(0)-(int)'0';
                            element=Main.currentGameEngine.getChess(x,y).showPanel();
                            anchorPane.getChildren().add(element);
                            element.setLayoutX(700);element.setLayoutY(70*(k-1));
                            element.setId("#root" + String.valueOf(k)+','+String.valueOf(i)+','+String.valueOf(j));
                        }
                    }
                }
            }
        }
    }
}
