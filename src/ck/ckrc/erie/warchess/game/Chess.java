package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.utils.DataPackage;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * 所有棋子类的统一父类，定义棋子类规范
 */
public abstract class Chess {

    /**
     * 血量，如果为0会被{@link Engine}在下一回合移除
     */
    public Integer hp=0;
    /**
     * 棋子自身坐标
     */
    public Integer x=0,y=0;
    /**
     * 棋子自身所属队伍的标签
     */
    public Integer teamFlag=-1;
    /**
     * 棋子所属类的类名，用于同步
     */
    public static final String className="null";

    /**
     * 显示该Chess对应侧边栏
     * @return 侧边栏对象
     */
    public abstract Node showPanel();

    /**
     * 显示该棋子类的信息
     * @return 棋子信息JavaFX节点
     */
    public static Node showData(){return null;}

    /**
     * 返回棋子的贴图
     * @param delta 距上次请求贴图时间间隔
     * @return 贴图对象
     */
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
     * 回合开始处理
     */
    public void roundBegin(){

    }

    /**
     * 回合结束处理
     */
    public void roundEnd(){

    }

    /**
     * 检查该玩家是否符合放置这个棋子的条件
     * @param player 玩家对象
     * @param x 被放置位置x坐标
     * @param y 被放置位置y坐标
     * @return 检查结果，为true则可以放置
     */
    public static boolean checkPlaceRequirements(Player player,int x,int y){return true;}

    /**
     * 初始化玩家
     * @param player 被初始化的玩家
     */
    public static void playerInit(Player player){}

    /**
     * 同步棋子状态，处理数据包
     * @param pack 接收的同步数据包
     */
    public void syncDataPackage(DataPackage pack){
        hp= (Integer) pack.get("hp");
        teamFlag= (Integer) pack.get("teamFlag");
        x=(Integer) pack.get("x");
        y=(Integer) pack.get("y");
    }

    /**
     * 获取棋子数据包，用于同步
     * @return 数据包
     */
    public abstract DataPackage getDataPackage();

    /**
     * 绘制特效
     * @param context 特效层画布
     * @param delta 距上次请求绘制时间
     */
    public void drawSpecialEffect(GraphicsContext context,long delta){}

}
