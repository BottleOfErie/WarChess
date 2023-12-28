package ck.ckrc.erie.warchess.example;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.*;
import ck.ckrc.erie.warchess.ui.Play;
import ck.ckrc.erie.warchess.utils.DataPackage;
import ck.ckrc.erie.warchess.utils.Math;
import ck.ckrc.erie.warchess.utils.ResourceSerialization;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Objects;

public class Tank extends Chess {

    public static final int attRadius=5,attDamage=10, build_cost =20,init_energy=15,shot_cost=1,move_cost=2,max_hp=30,max_anim=700,move_radius=3;
    private static final String imageData="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";
    private static final Image image= ResourceSerialization.getImageFromByteArray(ResourceSerialization.toByteArray(imageData));
    private DamageEvent myDmgEvt;
    private DamageListener myDmgListener;
    private int target_x=-1,target_y=-1,animationTimer=-1,ltx=-1,lty=-1,storagedEnergy=init_energy;
    private int moveTarget_x=-1,moveTarget_y=-1,ltmx=-1,ltmy=-1;
    private boolean roundEndExecuted=false;
    public static final String className="ck.ckrc.erie.warchess.example.Tank";

    public Tank(int x,int y,Player player){
        this.x=x;
        this.y=y;
        this.teamFlag=player.getTeamFlag();
        this.hp=max_hp;
        myDmgListener= damage -> {
            if(hp>damage){
                hp-=(int)damage;
                return 0;
            }else{
                double tmp=damage-hp;
                hp=0;
                return tmp;
            }
        };

        Main.currentGameEngine.registerDamageListener(this,1,myDmgListener,x,y);

        player.setStatus(Miner.energyKey,(int)player.getStatus(Miner.energyKey)-build_cost);

    }

    @Override
    public Node showPanel() {
        GridPane pane=new GridPane();
        pane.setPrefSize(200, 270);
        Label illegal=new Label("");
        Label title=new Label("坦克");
        Label position=new Label("position:"+'('+(x+1)+','+(y+1)+')');
        Label team=new Label("team:"+teamFlag);
        pane.addRow(0,title);
        Label status1=new Label("HP:"+hp+'('+max_hp+')');
        status1.setPrefWidth(80);
        Label status2=new Label("储能:"+storagedEnergy+'('+init_energy+')');
        pane.addRow(1,status1,status2);
        pane.addRow(2, position,team);
        if(!Objects.equals(Main.currentGameEngine.getCurrentTeam(), teamFlag)){
            return pane;
        }
        Label target_label=new Label("攻击目标:"+(target_x<0?"None":(target_x+1)+","+(target_y+1)));
        pane.addRow(3,target_label);
        Button cursor1=new Button("从棋盘选择");
        cursor1.setOnAction(actionEvent -> Play.submitClickEvent((x, y) -> {
            if(Math.distanceOfEuclid(x,y,this.x,this.y)>attRadius){
                illegal.setText("非法位置");
                return;
            }
            illegal.setText("");
            target_x=x;
            target_y=y;
            target_label.setText("攻击目标:"+(x + 1) +","+ (y + 1));
        }));
        pane.addRow(4,cursor1);
        Label moveTarget_label=new Label("目的地:"+(moveTarget_x<0?"None":(moveTarget_x+1)+","+(moveTarget_y+1)));
        pane.addRow(5,moveTarget_label);
        Button cursor2=new Button("从棋盘选择");
        cursor2.setOnAction(actionEvent -> Play.submitClickEvent((x, y) -> {
            if(Main.currentGameEngine.getChess(x,y)!=null||Math.distanceOfEuclid(x,y,this.x,this.y)>move_radius){
                illegal.setText("非法位置");
                return;
            }
            illegal.setText("");
            moveTarget_x=x;
            moveTarget_y=y;
            moveTarget_label.setText("目的地:"+(x + 1) +","+ (y + 1));
        }));
        pane.addRow(6,cursor2);
        pane.addRow(7,illegal);
        return pane;
    }

    public static Node showData(){
        GridPane pane=new GridPane();
        Label title=new Label("坦克");
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
    public void drawSpecialEffect(GraphicsContext context,long delta){
        if(animationTimer<0) {
            ltx=lty=ltmx=ltmy=-1;
            context.drawImage(image, x * 60 + 10, y * 60 + 10, 40, 40);
            return;
        }
        if(ltx>=0) {
            double xx=x,yy=y;
            if(ltmx>=0){
                xx=ltmx;
                yy=ltmy;
            }
            double speedX = (ltx - xx) * 60.0 / max_anim, speedY = (lty - yy) * 60.0 / max_anim;
            context.setFill(Color.CORNFLOWERBLUE);
            context.fillOval(xx * 60 + 30 + speedX * (max_anim - animationTimer), yy * 60 + 30 + speedY * (max_anim - animationTimer), 5, 5);
        }
        if(ltmx>=0){
            double speedX = (x-ltmx) * 60.0 / max_anim, speedY = (y-ltmy) * 60.0 / max_anim;
            context.drawImage(image, ltmx * 60 + 10 + speedX * (max_anim - animationTimer), ltmy * 60 + 10 + speedY * (max_anim - animationTimer), 40, 40);
        }else{
            context.drawImage(image, x * 60 + 10, y * 60 + 10, 40, 40);
        }
        animationTimer -= (int) delta;
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
        roundEndExecuted=false;
        var engine= Main.currentGameEngine;
        int tx=-1,ty=-1,flg=0;
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
        moveTarget_x=-1;moveTarget_y=-1;
    }

    @Override
    public void roundEnd() {
        if(roundEndExecuted)return;
        roundEndExecuted=true;
        if((target_x<0||target_y<0)&&(moveTarget_x<0||moveTarget_y<0))return;
        if(target_x>=0&&storagedEnergy>=shot_cost) {
            storagedEnergy-=shot_cost;
            myDmgEvt = new DamageEvent(target_x, target_y, attDamage, this);
            Main.currentGameEngine.commitDamageEvent(myDmgEvt);
            animationTimer = max_anim;
            ltx = target_x;
            lty = target_y;
        }
        if(moveTarget_x>=0&&storagedEnergy>=move_cost) {
            storagedEnergy-=move_cost;
            ltmx = x;
            ltmy = y;
            Main.currentGameEngine.setChess(x,y,null);
            x=moveTarget_x;
            y=moveTarget_y;
            Main.currentGameEngine.setChess(x,y,this);
            myDmgListener= damage -> {
                if(hp>damage){
                    hp-=(int)damage;
                    return 0;
                }else{
                    double tmp=damage-hp;
                    hp=0;
                    return tmp;
                }
            };
            Main.currentGameEngine.registerDamageListener(this,1,myDmgListener,x,y);
            animationTimer = max_anim;
        }
    }

    public static boolean checkPlaceRequirements(Player player,int x,int y) {
        return (int)player.getStatus(Miner.energyKey)>= build_cost;
    }

    @Override
    public void syncDataPackage(DataPackage pack) {
        super.syncDataPackage(pack);
        DataPackage.processDataPackage(this,this.getClass(),pack);
    }

    @Override
    public DataPackage getDataPackage() {
        return DataPackage.generateDataPackage(this,this.getClass());
    }

    @Override
    public Image paint(long delta) {
        return null;
    }
}
