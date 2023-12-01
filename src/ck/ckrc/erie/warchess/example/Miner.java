package ck.ckrc.erie.warchess.example;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.Chess;
import ck.ckrc.erie.warchess.game.DamageEvent;
import ck.ckrc.erie.warchess.game.DamageListener;
import ck.ckrc.erie.warchess.game.Player;
import ck.ckrc.erie.warchess.utils.ResourceSerialization;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public class Miner extends Chess {
    public static final int productions=10, build_cost =10,max_hp=20;
    public static final String imageData="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";
    public static final Image image=ResourceSerialization.getImageFromByteArray(ResourceSerialization.toByteArray(imageData));
    public static final String energyKey="example.red-stoneFlux";
    private DamageListener myDmgListener;
    private ImageView imageView;
    public static final String className="ck.ckrc.erie.warchess.example.Miner";

    public Miner(int x,int y,Player p){
        this.x=x;
        this.y=y;
        this.teamFlag=p.getTeamFlag();
        this.hp=max_hp;
        this.imageView=new ImageView(image);
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
    public Node paint() {
        return imageView;
    }
}
