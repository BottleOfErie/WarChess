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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.*;

public class Play {
    private static GraphicsContext graphicsContext;
    public static GraphicsContext spgraphicsContext;
    private static final Object threadLock=new Object();

    private static int edgelength=GameScene.edgelength;
    private static int nodeboxheight= Director.height/ Map.MapSize;
    static List<Chess> detailedChess =new LinkedList<>();
    private static Queue<ChessClickEvent> clickEvents=new LinkedList<>();

    public static AnchorPane anchorPane;
    public static Collection<Class<?>> classlist=new ArrayList<>();

    public static int gamemodel;
    private static int lastX=-1,lastY=-1;
    public static long lastRepaintTime= System.currentTimeMillis();
    public static int teamflag;

    public Play(GraphicsContext graphicsContext1,GraphicsContext graphicsContext2){
        graphicsContext=graphicsContext1;
        spgraphicsContext=graphicsContext2;
    }

    public SetChessAction setChessAction=new SetChessAction();

    private static VBox chessdetails;
    public class SetChessAction implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent event){
            if(gamemodel==0 || (gamemodel==1&&teamflag==Main.currentGameEngine.getCurrentTeam())) {
                int x = (int) (event.getX() / edgelength);
                int y = (int) (event.getY() / edgelength);

                if(!clickEvents.isEmpty()){
                    var evt=clickEvents.remove();
                    evt.handle(x,y);
                    return;
                }

                syncLastEvent();
                lastX=x;lastY=y;
                if (event.getButton().name().equals(MouseButton.PRIMARY.name())) {
                    removechoosechess();
                    if (Main.currentGameEngine.getChess(x, y) == null) {
                        chessdetails.getChildren().clear();
                        detailedChess.clear();
                        showchoosechess(x, y);
                    } else {
                        if (!detailedChess.contains(Main.currentGameEngine.getChess(x,y)))
                            showchessdetails(x, y);
                    }
                }
                if (event.getButton().name().equals(MouseButton.SECONDARY.name())) {
                    Main.currentGameEngine.setChess(x, y, null);
                    drawAllChess();
                }
            }
        }
    }
    private void showchoosechess(int x, int y){
        VBox root=new VBox();
        ScrollPane scrollPane=new ScrollPane(root);
        scrollPane.setPrefSize(200, 600);
        root.setPrefSize(200, 600);
        for(Class<?> clazz : classlist){
            GridPane pane=new GridPane();
            Button button=new Button("choose");
            button.setOnAction(actionEvent -> {
                Chess chess= ChessClassInvoker.getNewInstance(clazz,Main.currentGameEngine.getPlayer(Main.currentGameEngine.getCurrentTeam()),x,y);
                Main.currentGameEngine.setChess(x, y, chess);
                removechoosechess();
                Play.drawAllChess();
            });
            button.setDisable(!ChessClassInvoker.invokeCheckPlaceRequirements(clazz,Main.currentGameEngine.getPlayer(Main.currentGameEngine.getCurrentTeam()),x,y));
            pane.addRow(0,ChessClassInvoker.invokeShowData(clazz));
            pane.addRow(1,button);
            pane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,null,null)));
            root.getChildren().add(pane);
        }
        scrollPane.setId("choosechessroot");
        anchorPane.getChildren().add(scrollPane);
        scrollPane.setLayoutX(800);scrollPane.setLayoutY(0);
    }
    private static void removechoosechess(){
        try{
            Node node=anchorPane.lookup("#choosechessroot");
            anchorPane.getChildren().remove(node);
        }catch (IndexOutOfBoundsException e){}
    }
    public static void drawAllChess(){
        synchronized (threadLock){
            spgraphicsContext.clearRect(0,0,600,600);
            var now=System.currentTimeMillis();
            for(int i=0;i<Map.MapSize;i++)
                for(int j=0;j<Map.MapSize;j++) {
                    var chess = Main.currentGameEngine.getChess(i, j);
                    graphicsContext.clearRect(i * edgelength + 10, j * edgelength + 10, 40, 40);
                    if (chess != null) {
                        chess.drawSpecialEffect(spgraphicsContext,now - lastRepaintTime);
                        graphicsContext.drawImage(chess.paint(now - lastRepaintTime), i * 60 + 10, j * 60 + 10, 40, 40);
                    }
                }
            lastRepaintTime=now;
        }
    }

    private static void removechessdetails(int x,int y){
        Node pane=chessdetails.lookup("#root"+','+ x +','+ y);
        chessdetails.getChildren().remove(pane);
        detailedChess.remove(Main.currentGameEngine.getChess(x,y));
    }

    protected static void clearRightSideBar(){
        chessdetails.getChildren().clear();
        detailedChess.clear();
        removechoosechess();
    }

    private static void showchessdetails(int x, int y){
        GridPane root= new GridPane();
        var node=Main.currentGameEngine.getMap().getChessMap()[x][y].showPanel();
        GridPane gp=new GridPane();
        gp.add(node,0,0);
        gp.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,null,null)));
        gp.setPrefWidth(200);
        root.addRow(0,gp);
        Button button=new Button("隐藏");
        root.addRow(1, button);
        button.setPrefSize(50, 20);
        chessdetails.getChildren().addAll(root);
        root.setLayoutX(800);
        root.setId("root"+','+ x +','+ y);
        detailedChess.add(Main.currentGameEngine.getChess(x,y));
        button.setOnAction(actionEvent -> removechessdetails(x, y));
    }
    public static void updatechessdetails(){
        for(Chess chess: detailedChess){
            int x=chess.x,y=chess.y;
            GridPane element = (GridPane) chessdetails.lookup("#root" + ',' + x + ',' + y);
            int index=chessdetails.getChildren().indexOf(element);
            if (element != null) {
                GridPane newnode = (GridPane) chess.showPanel();
                newnode.setPrefSize(200, nodeboxheight);
                newnode.setLayoutX(800);
                Button button=new Button("隐藏");
                newnode.addRow(0, button);
                newnode.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,null,null)));
                chessdetails.getChildren().set(index, newnode);
                button.setOnAction(actionEvent -> removechessdetails(x,y));
                newnode.setId("root" + ',' + x + ',' + y);
            }
        }
    }

    public static void syncLastEvent(){
        if(Main.syncThread!=null&&lastX>=0&&lastY>=0){
            try {
                Main.syncThread.sendSync(lastX,lastY,Main.currentGameEngine.getChess(lastX,lastY));
                Main.syncThread.sendSyncP(0,Main.currentGameEngine.getPlayer(0));
                Main.syncThread.sendSyncP(1,Main.currentGameEngine.getPlayer(1));
                Main.syncThread.sendRepaint();
            } catch (IOException e) {
                Main.log.addLog("Failed to sync chess at:"+lastX+","+lastY,Play.class);
                Main.log.addLog(e,Play.class);
            }
        }
        lastX=lastY=-1;
    }
    public static void initchessdetailvbox(){
        ScrollPane scrollPane = new ScrollPane();
        chessdetails=new VBox();
        scrollPane.setContent(chessdetails);
        anchorPane.getChildren().add(scrollPane);
        scrollPane.setPrefSize(200, 600);chessdetails.setPrefSize(200, 600);
        scrollPane.setLayoutX(800);scrollPane.setLayoutY(0);
    }

    public static void submitClickEvent(ChessClickEvent event){
        clickEvents.add(event);
    }


}
