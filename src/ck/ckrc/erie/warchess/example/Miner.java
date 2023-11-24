package ck.ckrc.erie.warchess.example;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public class Miner extends Chess {
    public static final int productions=10, build_cost =10,max_hp=20;
    public static final String energyKey="example.red-stoneFlux";
    private DamageListener myDmgListener;
    public static final String className="ck.ckrc.erie.warchess.example.Miner";

    public Miner(int x,int y,Player p){
        this.x=x;
        this.y=y;
        this.teamFlag=p.getTeamFlag();
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
        p.setStatus(energyKey,(int)p.getStatus(energyKey)-build_cost);

    }

    @Override
    public Object showPanel() {
        GridPane pane=new GridPane();
        Label title=new Label("能量塔");
        pane.addRow(0,title);
        Label status1=new Label("HP:"+hp+'('+max_hp+')');
        Label status2=new Label("能量:"+Main.currentGameEngine.getPlayer(teamFlag).getStatus(Miner.energyKey));
        pane.addRow(1,status1,status2);

        return pane;
    }

    @Override
    public boolean checkEvent(DamageEvent evt) {
        return false;
    }

    @Override
    public boolean checkListener(DamageListener listener) {
        if(hp<=0)return false;
        return myDmgListener==listener;
    }

    @Override
    public void roundBegin() {
        //generate auto target
        if(!Objects.equals(Main.currentGameEngine.getCurrentTeam(), teamFlag))return;
        Main.currentGameEngine.getPlayer(teamFlag).setStatus(energyKey,(int)Main.currentGameEngine.getPlayer(teamFlag).getStatus(energyKey)+productions);
    }

    public static boolean checkPlaceRequirements(Player player,int x,int y) {
        return (int)player.getStatus(energyKey)>= build_cost;
    }


    public static void playerInit(Player player){
        player.setStatus(energyKey,10);
    }

    @Override
    public char paint() {
        return 'M';
    }
}
