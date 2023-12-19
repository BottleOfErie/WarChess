package ck.ckrc.erie.warchess.example2;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.Chess;
import ck.ckrc.erie.warchess.game.DamageEvent;
import ck.ckrc.erie.warchess.game.DamageListener;
import ck.ckrc.erie.warchess.game.Player;
import ck.ckrc.erie.warchess.ui.Setting;
import ck.ckrc.erie.warchess.utils.DataPackage;
import ck.ckrc.erie.warchess.utils.ResourceSerialization;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.util.Objects;

public class Gomoku extends Chess {

    public static final String className="ck.ckrc.erie.warchess.example2.Gomoku";
    public static final int black=0,white=1;

    public Gomoku(int x, int y, Player player){
        this.x=x;this.y=y;
        this.teamFlag=player.getTeamFlag();
        if(teamFlag<2) this.hp=1;
    }

    @Override
    public Node showPanel() {
        GridPane pane=new GridPane();
        Label title=new Label("棋子");
        Button whiteButton=new Button("设为白色");
        whiteButton.setOnAction(actionEvent -> teamFlag=white);
        Button blackButton=new Button("设为黑色");
        blackButton.setOnAction(actionEvent -> teamFlag=black);
        Button checkButton=new Button("检查");
        Label result=new Label("");
        checkButton.setOnAction(actionEvent -> {
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
            //TODO add game end
            result.setText((a==4||b==4||c==4||d==4)?"Win!":"NotYet.");
        });
        pane.addRow(0, title);
        pane.addRow(1, whiteButton, blackButton);
        pane.addRow(2, checkButton, result);
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

    public static Node showData(){
        Label title=new Label("棋子");
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

    public static boolean checkPlaceRequirements(Player player,int x,int y){return true;}
}
