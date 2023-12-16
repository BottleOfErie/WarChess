package ck.ckrc.erie.warchess.game;

import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.Serializable;

public abstract class Chess implements Serializable {

    public Integer hp=0;
    public Integer x=0,y=0;
    public Integer teamFlag=-1;
    public static final String className="null";
    public boolean allowPlayerBuild=false;

    /**
     * 显示该Chess对应侧边栏
     * @return
     */
    public abstract Node showPanel();
    public static Node showData(){return null;};

    public abstract Image paint(long delta);

    /**
     *检查伤害事件是否仍然有效
     * @param evt 伤害事件
     * @return 伤害事件有效
     */
    public abstract boolean checkEvent(DamageEvent evt);

    /**
     * 检查伤害监听器是否仍然有效
     *
     * @param listener 监听器
     * @return 监听器有效
     */
    public abstract boolean checkListener(DamageListener listener);

    /**
     * 回合开始初始化
     */
    public void roundBegin(){

    }

    /**
     * 回合结束
     */
    public void roundEnd(){

    }

    /**
     * 检查放置条件
     */
    public static boolean checkPlaceRequirements(Player player,int x,int y){return true;}


    public static void playerInit(Player player){}

}
