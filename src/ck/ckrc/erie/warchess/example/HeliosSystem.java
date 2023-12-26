package ck.ckrc.erie.warchess.example;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.*;
import ck.ckrc.erie.warchess.utils.DataPackage;
import ck.ckrc.erie.warchess.utils.ResourceSerialization;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.lang.invoke.MethodHandles;
import java.util.Random;

/**
 * <a href="https://scp-wiki-cn.wikidot.com/helios-system">Idea from this</a>
 */
public class HeliosSystem extends Chess {

    public static final String className="ck.ckrc.erie.warchess.example.HeliosSystem";
    public static final String imageData0="iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAABg2lDQ1BJQ0MgcHJvZmlsZQAAKJF9kT1Iw0AcxV9TpSIVh2YQcchQnSyIijhKFYtgobQVWnUwufQLmjQkKS6OgmvBwY/FqoOLs64OroIg+AHi6uKk6CIl/i8ptIjx4Lgf7+497t4BQrPKNKtnAtB020wn4lIuvyqFXhFGBCKCiMjMMpKZxSx8x9c9Any9i/Es/3N/jgG1YDEgIBHPMcO0iTeIZzZtg/M+scjKskp8Tjxu0gWJH7muePzGueSywDNFM5ueJxaJpVIXK13MyqZGPE0cVTWd8oWcxyrnLc5atc7a9+QvDBf0lQzXaY4ggSUkkYIEBXVUUIWNGK06KRbStB/38Q+7/hS5FHJVwMixgBo0yK4f/A9+d2sVpya9pHAc6H1xnI9RILQLtBqO833sOK0TIPgMXOkdf60JzH6S3uho0SNgcBu4uO5oyh5wuQMMPRmyKbtSkKZQLALvZ/RNeSByC/Sveb2193H6AGSpq+Ub4OAQGCtR9rrPu/u6e/v3TLu/H0iScpbKpMTxAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAABAUlEQVRYw9WXyw3CMBBEZ6IgwYEiuNENBSDRACVwTgk0gEQBdMMtRXAAiUjmkI/AhGS9tpRlT5FlzXtOHH8IfTn3vAAAONsAADUhmRYubBstauHt6Lug+i0EZzIFPEaCqeBaCaaEayQyKbwoq/fgQXhRVuKJSSm8rcMqH5wDft8xDkPgQxI+XCrBUHifxC+4RIIauB8c0tdnMgbuHvs6ZH5USzAW3gUpJZgCHiNB7SaSqnIAWJyvos737Rpu16ifAHdrnpf4aA/JyzBxTf4JTExCE7+hiYXIxFJsYjMysR2bOJB8SQzBfQkJ3MSh9G+O5SYuJiauZr0SWnjM7ZipBvMCfxLPjnOhtYgAAAAASUVORK5CYII=";
    public static final Image image0= ResourceSerialization.getImageFromByteArray(ResourceSerialization.toByteArray(imageData0));
    public static final String imageData1="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";
    public static final Image image1= ResourceSerialization.getImageFromByteArray(ResourceSerialization.toByteArray(imageData1));
    public static final String imageData2="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";
    public static final Image iconImage= ResourceSerialization.getImageFromByteArray(ResourceSerialization.toByteArray(imageData2));
    public static final String[] parts={"Elvis-Graham空间张量调谐器","Darius-Semiz驱动器","Ortiz-Hannigan传送场聚焦器","Wüthrich流变路由器"};
    public static final String[] words={"","知识","知识就是","知识就是力量"};
    public static final int build_cost =50,max_hp=10;
    public static final int initTime=4,attTime=5,attCost=10,stopCost=5;
    public static final double damage=1000;
    private static int next_target_x=-5, next_target_y,countdown;
    private static Random random=new Random();
    private static DamageEvent[][] events=new DamageEvent[Map.MapSize][Map.MapSize];
    private AnimationTimer timer=null;
    private static boolean roundEndExecuted=false;
    private DamageListener myDmgListener;
    private int target_x, target_y, sync_count;
    private static final int animTime=2000;//200 600*3
    private static int drawer_x=Map.MapSize,drawer_y=Map.MapSize,animTimer=-1,anim_x,anim_y;

    public HeliosSystem(int x, int y, Player player){
        this.x=x;this.y=y;
        hp=max_hp;
        this.teamFlag=player.getTeamFlag();
        if(next_target_x==-5) {
            next_target_x = -initTime;
            countdown = -1;
        }
        myDmgListener= damage -> {
            if(hp==Integer.MAX_VALUE)return damage;
            if(hp>damage){
                hp-=(int)damage;
                return 0;
            }else{
                double tmp=damage-hp;
                hp=0;
                if(animTimer>0&&drawer_x==x&&drawer_y==y)hp=Integer.MAX_VALUE;
                return tmp;
            }
        };

        var currentEnergy=(Integer)player.getStatus(Miner.energyKey);
        player.setStatus(Miner.energyKey, currentEnergy - build_cost);
        Main.currentGameEngine.registerDamageListener(this,1,myDmgListener,x,y);
    }

    @Override
    public Node showPanel() {
        GridPane gp=new GridPane();
        LinearGradient grad=new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE, new Stop(0,Color.DARKBLUE),new Stop(0.11,Color.GRAY),new Stop(1,Color.WHITE));
        var bg=new Background(new BackgroundFill(grad, CornerRadii.EMPTY, Insets.EMPTY));
        gp.setBackground(bg);
        ImageView iv=new ImageView(image1);
        iv.setFitWidth(30);
        iv.setFitHeight(150);
        GridPane pane=new GridPane();
        pane.setPrefWidth(150);
        Label title=new Label("赫利俄斯系统");
        title.setTextFill(Color.WHITE);
        Label status=new Label("");
        pane.addRow(0,title);
        pane.addRow(1,status);
        gp.addRow(0,pane,iv);
        if(next_target_x <0){
            status.setText("("+(next_target_x +4)+"/3)正在连接\n"+parts[next_target_x +4]);
            ImageView iv1=new ImageView(iconImage);
            iv1.setFitWidth(100);
            iv1.setFitHeight(33*(next_target_x+4));
            iv1.setViewport(new Rectangle2D(0,0,200,33*(next_target_x+4)));
            Label loading=new Label();
            timer = new AnimationTimer() {
                    @Override
                    public void handle(long l) {
                        if(next_target_x>=0)return;
                        StringBuilder sb = new StringBuilder(words[(next_target_x + 4)]);
                        for (int i = 0; i > next_target_x; i--) {
                            sb.append((char) (random.nextInt(26) + (random.nextBoolean() ? (int) 'a' : (int) 'A')));
                            sb.append((char) (random.nextInt(26) + (random.nextBoolean() ? (int) 'a' : (int) 'A')));
                        }
                        loading.setText(sb.toString());
                    }
                };
            timer.start();
            if(next_target_x!=-initTime)
                pane.addRow(2,iv1);
            pane.addRow(3,loading);
            return gp;
        }
        Label target=new Label("当前目标："+ next_target_x +','+ next_target_y);
        Button button = null;
        if(countdown==-1) {
            status.setText("巡航中");
            button=getButton(status,attCost,"引导\n攻击术式","正在引导术式，剩余"+attTime+"时间单位",attTime);
        }
        else {
            status.setText("正在引导术式，剩余" + countdown + "时间单位");
            button=getButton(status,stopCost,"干扰\n术式成型","巡航中",-1);
        }
        pane.addRow(2,target);
        pane.addRow(3, button);
        return gp;
    }

    private Button getButton(Label status,int cost,String text,String text1,int newCD){
        Button button=new Button(text);
        button.setFont(new Font(16));
        button.setOnAction(event->{
            button.setBackground(new Background(new BackgroundFill(Color.color(0,0,0,0),null,null)));
            Timeline tl=new Timeline(new KeyFrame(Duration.millis(200),ae->button.setBackground(new Background(new BackgroundImage(iconImage,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(0.9,0.9,true,true,false,false))))));
            tl.setCycleCount(1);
            tl.play();
            var currentEnergy=(Integer)Main.currentGameEngine.getPlayer(teamFlag).getStatus(Miner.energyKey);
            if(currentEnergy<cost)
                status.setText("没有足够的能量");
            else {
                Main.currentGameEngine.getPlayer(teamFlag).setStatus(Miner.energyKey, currentEnergy - cost);
                countdown = newCD;
                status.setText(text1);
                button.setText("构建奇术");
            }
        });
        button.setPrefSize(100,100);
        button.setBorder(Border.EMPTY);
        button.setBackground(new Background(new BackgroundImage(iconImage,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(0.9,0.9,true,true,false,false))));
        return button;
    }

    public static Node showData(){
        GridPane gp=new GridPane();
        LinearGradient grad=new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE, new Stop(0,Color.DARKBLUE),new Stop(0.11,Color.GRAY),new Stop(1,Color.WHITE));
        var bg=new Background(new BackgroundFill(grad, CornerRadii.EMPTY, Insets.EMPTY));
        gp.setBackground(bg);
        Border bd=new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT));
        gp.setBorder(bd);
        Label title=new Label("赫利俄斯系统");
        title.setTextFill(Color.WHITE);
        Label introduce=new Label("利用奇术法阵将太阳大气传送到地球上任意目标的天基特征武器。\n由于奇术压平流层霰射爆破弹的作用，无法控制赫利俄斯系统的GRT-2类传送法阵，但是仍可引导赫利俄斯系统攻击。");
        introduce.setMaxWidth(150);
        introduce.setWrapText(true);
        ImageView iv=new ImageView(image1);
        iv.setFitWidth(30);
        iv.setFitHeight(150);
        gp.addRow(0,title);
        gp.addRow(1,introduce,iv);
        gp.setPrefWidth(200);
        return gp;
    }

    @Override
    public void roundBegin() {
        roundEndExecuted=false;
        next_target_x++;
        if(next_target_x<0) return;
        if(timer!=null) {
            timer.stop();
            timer=null;
        }
        next_target_x =random.nextInt(Map.MapSize);
        next_target_y =random.nextInt(Map.MapSize);
    }

    @Override
    public void roundEnd() {
        target_x = next_target_x;
        target_y = next_target_y;
        if(roundEndExecuted)return;
        roundEndExecuted=true;
        if(countdown==-1)return;
        if(countdown==0){
            for (int i = 0; i < Map.MapSize; i++) {
                for (int j = 0; j < Map.MapSize; j++) {
                    events[i][j]=new DamageEvent(i,j, damage/ Math.exp(Math.sqrt((i-target_x)*(i-target_x)+(j-target_y)*(j-target_y))),this);
                    Main.currentGameEngine.commitDamageEvent(events[i][j]);
                }
            }
            animTimer=animTime;
            anim_x=target_x;
            anim_y=target_y;
        }
        countdown--;
    }

    @Override
    public void syncDataPackage(DataPackage pack) {
        super.syncDataPackage(pack);
        DataPackage.processDataPackage(this,this.getClass(),pack);
        countdown= sync_count;
        next_target_x = target_x;
        next_target_y = target_y;
    }

    @Override
    public DataPackage getDataPackage() {
        target_x = next_target_x;
        target_y = next_target_y;
        sync_count=countdown;
        return DataPackage.generateDataPackage(this, this.getClass());
    }

    @Override
    public void drawSpecialEffect(GraphicsContext context, long delta) {
        if(random.nextInt(10-(next_target_x<0?next_target_x:(countdown==-1?0:7-countdown)))==0){
            double a= random.nextDouble(), b=random.nextDouble(),c=random.nextDouble(),d=random.nextDouble();
            c*=(1-a);d*=(1-b);
            context.setFill(Color.VIOLET);
            context.fillRect(x*60+60*a,y*60+60*b,60*c,60*d);
        }
        if(countdown!=-1){
            double prop=1.0*(attTime-countdown)/attTime;
            var grad=new RadialGradient(0,0,0.5,0.5,0.5,true,CycleMethod.NO_CYCLE,new Stop(0,Color.LIGHTSKYBLUE),new Stop(0.5*prop,Color.SKYBLUE),new Stop(prop,Color.TRANSPARENT));
            context.setFill(grad);
            context.fillOval(x*60+10,y*60+10,40,40);
            context.setStroke(Color.RED);
            context.strokeOval(next_target_x*60,next_target_y*60,60,60);
        }
        if(x<=drawer_x&&y<=drawer_y) {
            drawer_x = x;
            drawer_y = y;
        }
        if(x!=drawer_x||y!=drawer_y)return;
        if(countdown!=-1){
            context.setStroke(Color.RED);
            context.strokeOval(next_target_x*60,next_target_y*60,60,60);
        }
        if(animTimer>0){
            double prop=0;
            if(animTimer>1900){
                context.setFill(Color.WHITE);
                context.fillRect(0,0,Map.MapSize*60,Map.MapSize*60);
            }
            if(animTimer>1800){
                prop=1.0*(2000-animTimer)/200;
                var grad=new RadialGradient(0,0,0.5,0.5,0.5,true,CycleMethod.NO_CYCLE,new Stop(0,Color.YELLOW),new Stop(1,Color.TRANSPARENT));
                context.setFill(grad);
                context.fillOval(anim_x*60+30-prop*60*3,anim_y*60+30-prop*60*3,prop*60*6,prop*60*6);
            } else if (animTimer>1200) {
                prop=1.0*(1800-animTimer)/600;
                var grad=new RadialGradient(0,0,0.5,0.5,0.5,true,CycleMethod.NO_CYCLE,new Stop(0,Color.BLACK),new Stop(prop,Color.YELLOW),new Stop(1,Color.TRANSPARENT));
                context.setFill(grad);
                context.fillOval(anim_x*60+30-60*3,anim_y*60+30-60*3,60*6,60*6);
            } else if (animTimer>600) {
                prop=1.0*(1200-animTimer)/600;
                var grad=new RadialGradient(0,0,0.5,0.5,0.5,true,CycleMethod.NO_CYCLE,new Stop(0,Color.TRANSPARENT),new Stop(prop,Color.BLACK),new Stop(1,Color.YELLOW));
                context.setFill(grad);
                context.fillOval(anim_x*60+30-60*3,anim_y*60+30-60*3,60*6,60*6);
            } else {
                prop=1.0*(600-animTimer)/600;
                var grad=new RadialGradient(0,0,0.5,0.5,0.5,true,CycleMethod.NO_CYCLE,new Stop(0,Color.TRANSPARENT),new Stop(prop,Color.TRANSPARENT),new Stop(1,Color.BLACK));
                context.setFill(grad);
                context.fillOval(anim_x*60+30-60*3,anim_y*60+30-60*3,60*6,60*6);
            }
            animTimer-= (int) delta;
            if(animTimer<0&&hp==Integer.MAX_VALUE) {
                Main.currentGameEngine.setChess(x, y, null);
                drawer_x=Map.MapSize;
                drawer_y=Map.MapSize;
            }
        }
    }

    @Override
    public Image paint(long delta) {
        return hp==Integer.MAX_VALUE?null:image0;
    }

    @Override
    public boolean checkEvent(DamageEvent evt) {
        return true;
    }

    @Override
    public boolean checkListener(DamageListener listener) {
        return listener==myDmgListener;
    }

    public static boolean checkPlaceRequirements(Player player,int x,int y){return true;}

}
