package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.ui.GameScene;
import ck.ckrc.erie.warchess.ui.Play;

import java.util.*;

/**
 * 游戏主引擎
 */
public class Engine {

    /**
     * 玩家数量
     */
    public static int playerNum=2;

    /**
     * 用于排序伤害监听器
     */
    private static class ListenerNode {
        public Chess parent;
        public int priority;
        public DamageListener listener;

        public ListenerNode(Chess parent,int priority,DamageListener listener){
            this.parent=parent;
            this.priority=priority;
            this.listener=listener;
        }

    }

    /**
     * 监听器排序算子
     */
    private static final Comparator<ListenerNode> listenerComparator= (o1, o2) -> o2.priority-o1.priority;

    /**
     * 当前地图
     */
    private Map currentMap=null;
    /**
     * 当前正在操作的队伍标签
     */
    private Integer currentTeam=0;
    /**
     * 游戏结束监听器表
     */
    private List<GameOverListener> gameOverListeners;
    /**
     * 伤害监听器表
     */
    private ArrayList<ListenerNode>[][] damageListeners;
    /**
     * 伤害事件队列
     */
    private Queue<DamageEvent> damageQueue;
    /**
     * 玩家表
     */
    private HashMap<Integer,Player> players;

    /**
     * 初始化地图、各个表
     */
    public Engine(){
        Main.log.addLog("Creating Engine",this.getClass());
        currentMap=new Map();
        damageListeners= new ArrayList[Map.MapSize][Map.MapSize];
        gameOverListeners=new ArrayList<>();
        for(int i=0;i<Map.MapSize;i++)
            for(int j=0;j<Map.MapSize;j++)
                damageListeners[i][j]=new ArrayList<>();
        damageQueue=new LinkedList<>();
        players=new HashMap<>();
        Main.log.addLog("Engine initialized",this.getClass());
    }

    /**
     * 结算回合，进入下一个回合<br/>
     * 执行各个棋子{@link Chess}的roundEnd方法<br/>
     * 排序并检测伤害监听器{@link DamageListener}有效性<br/>
     * 处理伤害事件队列{@link DamageEvent}<br/>
     * 移除hp==0的棋子{@link Chess}<br/>
     * 检查游戏结束监听器{@link GameOverListener}<br/>
     * 修改当前队伍标签，进入下一个回合<br/>
     * 执行各个棋子{@link Chess}的roundBegin方法
     * @param nextTeam 下一个队伍的队伍标签
     */
    public void nextRound(Integer nextTeam){//开始回合结算

        Main.log.addLog("Concluding Round",this.getClass());
        Main.log.addLog("Executing roundEnd function",this.getClass());
        //execute Chess roundEnd method
        for(int i=0;i<Map.MapSize;i++)
            for(int j=0;j<Map.MapSize;j++)
                if(currentMap.getChessMap()[i][j]!=null)
                    currentMap.getChessMap()[i][j].roundEnd();
        Main.log.addLog("Solving listeners",this.getClass());
        //sort and listener validate
        for(int i=0;i<Map.MapSize;i++)
            for(int j=0;j<Map.MapSize;j++){
                damageListeners[i][j].sort(listenerComparator);
                damageListeners[i][j].removeIf(current -> !current.parent.checkListener(current.listener));
            }
        //damage count
        Main.log.addLog("Solving damage events",this.getClass());
        while(!damageQueue.isEmpty()){
            var currentEvt=damageQueue.remove();
            var damage=currentEvt.getDamage();
            if(!currentEvt.check())continue;
            var iter=damageListeners[currentEvt.getX()][currentEvt.getY()].iterator();
            while(iter.hasNext()&&damage>0){
                var current=iter.next();
                damage=current.listener.takeDamage(damage);
            }
            if(damage>0)currentMap.overDamaged(currentEvt.getX(),currentEvt.getY(),damage);
        }

        //remove died chess
        for(int i=0;i<Map.MapSize;i++)
            for(int j=0;j<Map.MapSize;j++)
                if(currentMap.getChessMap()[i][j]!=null&&currentMap.getChessMap()[i][j].hp<=0) {
                    currentMap.setChess(i, j, null);
                }
        for (var item:
                gameOverListeners)
            if(item.check()) {
                Play.gameEnd(currentTeam);
                return;
            }

        currentTeam=nextTeam;
        Main.log.addLog("current teamFlag:"+currentTeam,this.getClass());
        Main.log.addLog("Executing roundBegin function",this.getClass());
        //execute Chess roundBegin method
        for(int i=0;i<Map.MapSize;i++)
            for(int j=0;j<Map.MapSize;j++)
                if(currentMap.getChessMap()[i][j]!=null)
                    currentMap.getChessMap()[i][j].roundBegin();
    }

    public Map getMap(){
        return currentMap;
    }

    public void setChess(int x,int y,Chess chess){
        currentMap.setChess(x,y,chess);
    }

    public Chess getChess(int x,int y){
        if(x<0||x>=Map.MapSize||y<0||y>= Map.MapSize)return null;
        return currentMap.getChessMap()[x][y];
    }

    /**
     * 提交伤害事件
     * @param evt 伤害事件
     */
    public void commitDamageEvent(DamageEvent evt){
        damageQueue.add(evt);
    }

    /**
     * 注册伤害监听器
     * @param parent 伤害监听器来源
     * @param priority 监听器优先级
     * @param listener 监听器
     * @param x 监听器x坐标
     * @param y 监听器y坐标
     */
    public void registerDamageListener(Chess parent,int priority,DamageListener listener,int x,int y){
        damageListeners[x][y].add(new ListenerNode(parent,priority,listener));
    }

    /**
     * 获取指定队伍标签的玩家
     * @param teamFlag 队伍标签
     * @return 指定队伍标签的玩家
     */
    public Player getPlayer(int teamFlag){
        return players.get(teamFlag);
    }

    public void setPlayer(int teamFlag,Player player){
        players.put(teamFlag,player);
    }

    public Integer getCurrentTeam() {
        return currentTeam;
    }

    /**
     * 提交游戏结束监听器
     * @param listener 游戏结束监听器
     */
    public void commitGameOverListener(GameOverListener listener){
        gameOverListeners.add(listener);
    }

}
