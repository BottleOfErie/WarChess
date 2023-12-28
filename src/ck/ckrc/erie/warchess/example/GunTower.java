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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Objects;

public class GunTower extends Chess {

    public static final int attRadius=5,attDamage=10, build_cost =5,shot_cost=1,max_hp=50,max_anim=700;
    private static final String imageData="Qk02DAAAAAAAADYAAAAoAAAAIAAAACAAAAABABgAAAAAAAAMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/AAAAAAAAAAAAAPL/AAAAAPL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/AAAAAPL/AAAAAAAAAPL/APL/AAAAAPL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/AAAAAPL/APL/AAAAAAAAAPL/APL/APL/AAAAAPL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/AAAAAPL/APL/APL/AAAAAAAAAPL/APL/APL/APL/AAAAAPL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/AAAAAPL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/AAAAAPL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/AAAAAPL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/AAAAAPL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/AAAAAPL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAJBztJBztJBztJBztJBztJBztJBztJBztJBztJBztJBztJBztAAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAJBztAAAAJBztJBztJBztJBztJBztJBztJBztJBztJBztJBztAAAAJBztAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAJBztJBztAAAAJBztJBztJBztJBztJBztJBztJBztJBztAAAAJBztJBztAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAJBztJBztJBztAAAAJBztJBztJBztJBztJBztJBztAAAAJBztJBztJBztAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAJBztJBztJBztJBztAAAAJBztJBztJBztJBztAAAAJBztJBztJBztJBztAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAJBztJBztJBztJBztJBztAAAAJBztJBztAAAAJBztJBztJBztJBztJBztAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAJBztJBztJBztJBztJBztJBztAAAAAAAAJBztJBztJBztJBztJBztJBztAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAJBztJBztJBztJBztJBztJBztAAAAAAAAJBztJBztJBztJBztJBztJBztAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAJBztJBztJBztJBztJBztAAAAJBztJBztAAAAJBztJBztJBztJBztJBztAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAJBztJBztJBztJBztAAAAJBztJBztJBztJBztAAAAJBztJBztJBztJBztAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAJBztJBztJBztAAAAJBztJBztJBztJBztJBztJBztAAAAJBztJBztJBztAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAJBztJBztAAAAJBztJBztJBztJBztJBztJBztJBztJBztAAAAJBztJBztAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAJBztAAAAJBztJBztJBztJBztJBztJBztJBztJBztJBztJBztAAAAJBztAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAJBztJBztJBztJBztJBztJBztJBztJBztJBztJBztJBztJBztAAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPL/APL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/APL/AAAAAPL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/AAAAAPL/APL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/APL/AAAAAPL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/AAAAAPL/APL/APL/APL/APL/AAAAAAAAAPL/APL/APL/APL/AAAAAPL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/AAAAAPL/APL/APL/APL/AAAAAAAAAPL/APL/APL/AAAAAPL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/AAAAAPL/APL/APL/AAAAAAAAAPL/APL/AAAAAPL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/AAAAAPL/APL/AAAAAAAAAPL/AAAAAPL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/AAAAAPL/AAAAAAAAAAAAAPL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/APL/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final Image image= ResourceSerialization.getImageFromByteArray(ResourceSerialization.toByteArray(imageData));
    private DamageEvent myDmgEvt;
    private DamageListener myDmgListener;
    private int target_x=-1,target_y=-1,animationTimer=-1,ltx=-1,lty=-1;
    public static final String className="ck.ckrc.erie.warchess.example.GunTower";

    public GunTower(int x,int y,Player player){
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
        Label title=new Label("火枪塔");
        Label position=new Label("position:"+'('+(x+1)+','+(y+1)+')');
        Label team=new Label("team:"+teamFlag);
        pane.addRow(0,title);
        Label status1=new Label("HP:"+hp+'('+max_hp+')');
        status1.setPrefWidth(80);
        Label status2=new Label("能量:"+Main.currentGameEngine.getPlayer(teamFlag).getStatus(Miner.energyKey));
        pane.addRow(1,status1,status2);
        pane.addRow(2, position,team);
        Label x_label=new Label("目标x:");
        TextField x_input=new TextField(target_x<0?"":String.valueOf(target_x+1));
        x_input.setEditable(Objects.equals(Main.currentGameEngine.getCurrentTeam(), teamFlag));
        x_input.setPrefWidth(100);
        x_input.textProperty().addListener((observableValue, t1, s) -> {

            try{
                int i=Integer.parseInt(s);
                if(i<=0||i>=Map.MapSize)
                    x_input.setText(t1);
                else
                    target_x=i-1;
            }catch(NumberFormatException e){
                if("".equals(s))return;
                x_input.setText(t1);
            }
        });
        pane.addRow(3,x_label,x_input);
        Label y_label=new Label("目标y:");
        TextField y_input=new TextField(target_y<0?"":String.valueOf(target_y+1));
        y_input.setEditable(Objects.equals(Main.currentGameEngine.getCurrentTeam(), teamFlag));
        y_input.setPrefWidth(100);
        y_input.textProperty().addListener((observableValue, t1, s) -> {
            try{
                int i=Integer.parseInt(s);
                if(i<=0||i>=Map.MapSize)
                    y_input.setText(t1);
                else
                    target_y=i-1;
            }catch(NumberFormatException e){
                if("".equals(s))return;
                y_input.setText(t1);
            }
        });
        pane.addRow(4,y_label,y_input);
        Button cursor=new Button("从棋盘选择");
        cursor.setOnAction(actionEvent -> Play.submitClickEvent((x, y) -> {
            target_x=x;
            target_y=y;
            x_input.setText(String.valueOf(x+1));
            y_input.setText(String.valueOf(y+1));
        }));
        cursor.setDisable(!Objects.equals(Main.currentGameEngine.getCurrentTeam(), teamFlag));
        pane.addRow(5,cursor);
        pane.setPrefSize(200, 230);
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
    public void drawSpecialEffect(GraphicsContext context,long delta){
        if(animationTimer<0)return;
        double speedx=(ltx-x)*60.0/max_anim,speedy=(lty-y)*60.0/max_anim;
        context.setFill(Color.CORNFLOWERBLUE);
        context.fillOval(x*60+30+speedx*(max_anim-animationTimer),y*60+30+speedy*(max_anim-animationTimer),5,5);
        animationTimer-= (int) delta;
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
    }

    @Override
    public void roundEnd() {
        if(target_x<0||target_y<0)return;
        if((Integer) Main.currentGameEngine.getPlayer(teamFlag).getStatus(Miner.energyKey)<shot_cost)return;
        Main.currentGameEngine.getPlayer(teamFlag).setStatus(Miner.energyKey,(Integer) Main.currentGameEngine.getPlayer(teamFlag).getStatus(Miner.energyKey)-shot_cost);
        myDmgEvt=new DamageEvent(target_x,target_y,attDamage,this);
        Main.currentGameEngine.commitDamageEvent(myDmgEvt);
        animationTimer=max_anim;
        ltx=target_x;
        lty=target_y;
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
        return image;
    }
}
