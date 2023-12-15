package ck.ckrc.erie.warchess.example;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.*;
import ck.ckrc.erie.warchess.utils.Math;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public class GunTower extends Chess {

    public static final int attRadius=5,attDamage=10, build_cost =5,shot_cost=1,max_hp=50;
    private DamageEvent myDmgEvt;
    private DamageListener myDmgListener;
    private int target_x=0,target_y=0;
    public static final String className="ck.ckrc.erie.warchess.example.GunTower";

    public GunTower(int x,int y,Player player){
        this.x=x;
        this.y=y;
        this.teamFlag=player.getTeamFlag();
        this.hp=max_hp;
        myDmgListener=new DamageListener() {

            @Override
            public double takeDamage(double damage) {
                if(hp>damage){
                    hp-=(int)damage;
                    return 0;
                }else{
                    double tmp=damage-hp;
                    hp=0;
                    return tmp;
                }
            }
        };

        Main.currentGameEngine.registerDamageListener(this,1,myDmgListener,x,y);

        player.setStatus(Miner.energyKey,(int)player.getStatus(Miner.energyKey)-build_cost);

    }

    @Override
    public Node showPanel(int x,int y) {
        GridPane pane=new GridPane();
        Label title=new Label("火枪塔");
        Label position=new Label("position:"+'('+x+','+y+')');
        Label team=new Label("team:"+teamFlag);
        pane.addRow(0,title);
        Label status1=new Label("HP:"+hp+'('+max_hp+')');
        status1.setPrefWidth(80);
        Label status2=new Label("能量:"+Main.currentGameEngine.getPlayer(teamFlag).getStatus(Miner.energyKey));
        pane.addRow(1,status1,status2);
        pane.addRow(2, position,team);
        Label x_label=new Label("目标x:");
        TextField x_input=new TextField(String.valueOf(target_x+1));
        x_input.setPrefWidth(100);
        x_input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try{
                    int i=Integer.parseInt(s);
                    if(i<=0||i>=Map.MapSize)
                        x_input.setText(t1);
                    else
                        target_x=i-1;
                }catch(NumberFormatException e){
                    x_input.setText(t1);
                }
            }
        });
        pane.addRow(3,x_label,x_input);
        Label y_label=new Label("目标y:");
        TextField y_input=new TextField(String.valueOf(target_y+1));
        y_input.setPrefWidth(100);
        y_input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try{
                    int i=Integer.parseInt(s);
                    if(i<=0||i>=Map.MapSize)
                        y_input.setText(t1);
                    else
                        target_y=i-1;
                }catch(NumberFormatException e){
                    y_input.setText(t1);
                }
            }
        });
        pane.addRow(4,y_label,y_input);
        pane.setPrefSize(200, 200);
        return pane;
    }


    public static Node showData(){
        GridPane pane=new GridPane();
        Label title=new Label("火枪塔");
        title.setPrefWidth(100);
        pane.addRow(0, title);
        Label status=new Label("maxHP:"+max_hp);
        pane.addRow(1, status);
        Label cost1=new Label("build cost:"+build_cost);
        Label cost2=new Label("shot cost:"+shot_cost);
        pane.addRow(2, cost1,cost2);
        Label attdg=new Label("attack damage:"+attDamage);
        Label attrd=new Label("attack radius"+attRadius);
        pane.addRow(3, attdg,attrd);
        pane.setPrefSize(200, 40);
        return pane;
    }

    @Override
    public boolean checkEvent(DamageEvent evt) {
        if(hp<=0)return false;
        return evt==myDmgEvt;
    }

    @Override
    public boolean checkListener(DamageListener listener) {
        if(hp<=0)return false;
        return myDmgListener==listener;
    }

    @Override
    public void roundBegin() {
        //generate auto target

        if((int)Main.currentGameEngine.getPlayer(teamFlag).getStatus(Miner.energyKey)<shot_cost)return;
        Main.currentGameEngine.getPlayer(teamFlag).setStatus(Miner.energyKey,(int)Main.currentGameEngine.getPlayer(teamFlag).getStatus(Miner.energyKey)-shot_cost);
        var engine= Main.currentGameEngine;
        int tx=0,ty=0,flg=0;
        for(int i=0;i<Map.MapSize;i++)
            for(int j=0;j<Map.MapSize;j++) {
                if(engine.getChess(i, j)==null)continue;
                if (Math.distanceOfEuclid(x, y, i, j) < attRadius && !Objects.equals(engine.getChess(i, j).teamFlag, this.teamFlag)) {
                    if (flg == 0) {
                        tx = i;
                        ty = j;
                        flg = 1;
                    } else if (Math.distanceOfEuclid(x, y, tx, ty) > Math.distanceOfEuclid(x, y, i, j)) {
                        tx = i;
                        ty = j;
                    }
                }
            }
        target_x=tx;target_y=ty;
    }

    @Override
    public void roundEnd() {
        myDmgEvt=new DamageEvent(target_x,target_y,attDamage,this);
        Main.currentGameEngine.commitDamageEvent(myDmgEvt);
    }

    public static boolean checkPlaceRequirements(Player player,int x,int y) {
        return (int)player.getStatus(Miner.energyKey)>= build_cost;
    }

    @Override
    public Image paint() {
        return null;
    }
}
