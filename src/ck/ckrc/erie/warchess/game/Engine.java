package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.Main;

import java.util.*;

public class Engine {

    public static final int playerNum=2;

    public Integer getCurrentTeam() {
        return currentTeam;
    }

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

    private static final Comparator<ListenerNode> listenerComparator=new Comparator<ListenerNode>() {
        @Override
        public int compare(ListenerNode o1, ListenerNode o2) {
            return o2.priority-o1.priority;
        }
    };

    private Map currentMap=null;
    private Integer currentTeam=0;
    private ArrayList<ListenerNode>[][] damageListeners;
    private Queue<DamageEvent> damageQueue;
    private HashMap<Integer,Player> players;

    public Engine(){
        Main.log.addLog("Creating Engine",this.getClass());
        currentMap=new Map();
        damageListeners= new ArrayList[Map.MapSize][Map.MapSize];
        for(int i=0;i<Map.MapSize;i++)
            for(int j=0;j<Map.MapSize;j++)
                damageListeners[i][j]=new ArrayList<>();
        damageQueue=new LinkedList<>();
        players=new HashMap<>();
        Main.log.addLog("Engine initialized",this.getClass());
    }

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
                var iter=damageListeners[i][j].iterator();
                while(iter.hasNext()){
                    var current=iter.next();
                    if(!current.parent.checkListener(current.listener))iter.remove();
                }
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
                if(currentMap.getChessMap()[i][j]!=null&&currentMap.getChessMap()[i][j].hp<=0)
                    currentMap.setChess(i,j,null);

        currentTeam=nextTeam;

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
        if(x<0||x>Map.MapSize||y<0||y> Map.MapSize)return null;
        return currentMap.getChessMap()[x][y];
    }

    public void commitDamageEvent(DamageEvent evt){
        damageQueue.add(evt);
    }

    public void registerDamageListener(Chess parent,int priority,DamageListener listener,int x,int y){
        damageListeners[x][y].add(new ListenerNode(parent,priority,listener));
    }

    public Player getPlayer(int teamFlag){
        return players.get(teamFlag);
    }

    public void setPlayer(int teamFlag,Player player){
        players.put(teamFlag,player);
    }

}
