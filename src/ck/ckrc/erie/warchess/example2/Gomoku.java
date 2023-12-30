package ck.ckrc.erie.warchess.example2;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.*;
import ck.ckrc.erie.warchess.utils.DataPackage;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * 五子棋<br/>
 * 结局监听器示范类
 */
public class Gomoku extends Chess {

    public static final String className="ck.ckrc.erie.warchess.example2.Gomoku";
    public static final int black=0,white=1;
    public static GameOverListener listener =null;
    public static boolean pushed=false;
    public static boolean gameEnd=false;

    public Gomoku(int x, int y, Player player){
        pushed=true;
        this.x=x;this.y=y;
        this.teamFlag=player.getTeamFlag();
        if(teamFlag<2) this.hp=1;
        if(listener !=null)return;
        listener =()->gameEnd;
        Main.currentGameEngine.commitGameOverListener(listener);
    }

    @Override
    public Node showPanel() {
        GridPane pane=new GridPane();
        Label title=new Label("棋子");
        Button whiteButton=new Button("设为白色");
        whiteButton.setOnAction(actionEvent -> teamFlag=white);
        Button blackButton=new Button("设为黑色");
        blackButton.setOnAction(actionEvent -> teamFlag=black);
        pane.addRow(0, title);
        pane.addRow(1, whiteButton, blackButton);
        return pane;
    }

    @Override
    public void drawSpecialEffect(GraphicsContext context, long delta) {
        if(teamFlag==white){
            context.setStroke(Color.BLACK);
            context.strokeOval(x*60+10,y*60+10,40,40);
        }else if(teamFlag==black){
            context.setFill(Color.BLACK);
            context.fillOval(x*60+10,y*60+10,40,40);
        }
    }

    @Override
    public void roundBegin() {
        pushed=false;
    }

    @Override
    public void roundEnd() {
        int a=0,b=0,c=0,d=0;
        for (int i = 1; i < 5; i++) {
            var chess=Main.currentGameEngine.getChess(x,y+i);
            if(chess!=null&&chess.getClass()==this.getClass()&& Objects.equals(chess.teamFlag, this.teamFlag))a++;
            chess=Main.currentGameEngine.getChess(x+i,y);
            if(chess!=null&&chess.getClass()==this.getClass()&& Objects.equals(chess.teamFlag, this.teamFlag))b++;
            chess=Main.currentGameEngine.getChess(x+i,y+i);
            if(chess!=null&&chess.getClass()==this.getClass()&& Objects.equals(chess.teamFlag, this.teamFlag))c++;
            chess=Main.currentGameEngine.getChess(x-i,y+i);
            if(chess!=null&&chess.getClass()==this.getClass()&& Objects.equals(chess.teamFlag, this.teamFlag))d++;
        }
        if (a==4||b==4||c==4||d==4)
            gameEnd=true;
    }

    public static Node showData(){
        Label title=new Label("棋子:要求玩家数:2");
        title.setPrefWidth(100);
        return title;
    }

    @Override
    public void syncDataPackage(DataPackage pack) {
        super.syncDataPackage(pack);
    }

    @Override
    public DataPackage getDataPackage() {
        return DataPackage.generateDataPackage(this, this.getClass());
    }

    @Override
    public Image paint(long delta) {
        return null;
    }

    @Override
    public boolean checkEvent(DamageEvent evt) {
        return false;
    }

    @Override
    public boolean checkListener(DamageListener listener) {
        return false;
    }

    public static boolean checkPlaceRequirements(Player player,int x,int y){return (player.getTeamFlag()==0||player.getTeamFlag()==1)&&!pushed;}
}
