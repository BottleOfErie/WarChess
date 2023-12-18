package ck.ckrc.erie.warchess.example;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.*;
import ck.ckrc.erie.warchess.utils.DataPackage;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Objects;

public class Shield extends Chess {


    public static final int max_shield=100,regeneration=10, build_cost =10,max_hp=20,listener_priority=19;
    public static final String className="ck.ckrc.erie.warchess.example.Shield";
    public static final int[] deltaX={1,1,1,0,0,-1,-1,-1,0};
    public static final int[] deltaY={1,1,1,0,0,-1,-1,-1,0};
    private int shield=0;
    private DamageListener[] listeners=new DamageListener[9];
    private DamageListener selfListener;
    private int drawcount=0;

    public Shield(int x, int y, Player p){
        this.x=x;this.y=y;
        this.hp=max_hp;
        shield=0;
        for (int i = 0; i < 8; i++) {
            listeners[i]= damage -> {
                if (shield>=damage){
                    shield= (int) (shield-damage);
                    return 0;
                }else{
                    double tmp=damage-shield;
                    shield=0;
                    return tmp;
                }
            };
            Main.currentGameEngine.registerDamageListener(this,listener_priority,listeners[i],x+deltaX[i],y+deltaY[i]);
        }
        selfListener= damage -> {
            if(hp>damage){
                hp-=(int)damage;
                return 0;
            }else{
                double tmp=damage-hp;
                hp=0;
                return tmp;
            }
        };
        Main.currentGameEngine.registerDamageListener(this,1,selfListener,x,y);
        p.setStatus(Miner.energyKey,(int)p.getStatus(Miner.energyKey)-build_cost);
    }

    @Override
    public void roundBegin() {
        if(!Objects.equals(Main.currentGameEngine.getCurrentTeam(), teamFlag))return;
        shield+=regeneration;
        if(shield>max_shield)shield=max_shield;
    }

    @Override
    public Node showPanel() {
        GridPane pane=new GridPane();
        Label title=new Label("防御塔");
        pane.addRow(0,title);
        Label status1=new Label("HP:"+hp+'('+max_hp+')');
        Label status2=new Label("护盾:"+shield+'('+max_shield+')');
        pane.addRow(1,status1,status2);
        return pane;
    }

    public static Node showData(){
        GridPane pane=new GridPane();
        Label title=new Label("能量塔");
        title.setPrefWidth(100);
        Label status=new Label("maxShield:"+max_shield);
        pane.addRow(1, status);
        Label cost=new Label("build cost:"+build_cost);
        pane.addRow(2, cost);
        Label product=new Label("regeneration:"+regeneration);
        pane.setPrefSize(200, 25);
        return pane;
    }
    @Override
    public void drawSpecialEffect(GraphicsContext context, long delta){
        drawcount+=1;
        context.setFill(Color.YELLOW);
        double opacity=0.01*drawcount;
        if(opacity>1){opacity=1;drawcount=0;}
        context.getCanvas().setOpacity(opacity);
        int width=180,height=180;
        if(x==0||x==Map.MapSize)width-=60;if(y==0||y==Map.MapSize)height-=60;
        context.fillRect(java.lang.Math.max(0, (x-1)*60), Math.max(0,(y-1)*60), width,height);
    }

    @Override
    public boolean checkEvent(DamageEvent evt) {
        return false;
    }

    @Override
    public boolean checkListener(DamageListener listener) {
        for(var item:listeners)
            if(item==listener)return true;
        return listener==selfListener;
    }

    @Override
    public void syncDataPackage(DataPackage pack) {
        super.syncDataPackage(pack);
        shield= (int) pack.get("s");
    }

    @Override
    public DataPackage getDataPackage() {
        DataPackage pack=new DataPackage();
        pack.put("hp",hp);
        pack.put("teamFlag",teamFlag);
        pack.put("s",shield);
        return pack;
    }

    @Override
    public Image paint(long delta) {
        //TODO add Image
        return null;
    }

    public static boolean checkPlaceRequirements(Player player,int x,int y){return (int)player.getStatus(Miner.energyKey)>= build_cost;}
}
