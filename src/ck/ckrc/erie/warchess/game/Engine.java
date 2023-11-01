package ck.ckrc.erie.warchess.game;

import java.util.*;

public class Engine {

    public static final int playerNum=2;

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

    private static Comparator<ListenerNode> listenerComparator=new Comparator<ListenerNode>() {
        @Override
        public int compare(ListenerNode o1, ListenerNode o2) {
            return o2.priority-o1.priority;
        }
    };

    private Map currentMap=null;
    private Integer currentTeam=0;
    private ArrayList<ListenerNode>[][] damageListeners;
    private Queue<DamageEvent> damageQueue;
    private Vector<Player> players;

    public Engine(){
        currentMap=new Map();
        damageListeners= (ArrayList<ListenerNode>[][]) new Object[Map.MapSize][Map.MapSize];
        for(int i=0;i<Map.MapSize;i++)
            for(int j=0;j<Map.MapSize;j++)
                damageListeners[i][j]=new ArrayList<>();
        damageQueue=new LinkedList<>();
        players=new Vector<>();
    }

    public void nextRound(Integer nextTeam){//开始回合结算

        //execute Chess roundEnd method
        for(int i=0;i<Map.MapSize;i++)
            for(int j=0;j<Map.MapSize;j++)
                currentMap.getChessMap()[i][j].roundEnd();
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

        currentTeam=nextTeam;

        //execute Chess roundBegin method
        for(int i=0;i<Map.MapSize;i++)
            for(int j=0;j<Map.MapSize;j++)
                currentMap.getChessMap()[i][j].roundBegin();
    }

    public Map getMap(){
        return currentMap;
    }

    public Chess getChess(int x,int y){
        if(x<0||x>Map.MapSize||y<0||y> Map.MapSize)return null;
        return currentMap.getChessMap()[x][y];
    }

    public void commitDamageEvent(DamageEvent evt){
        damageQueue.add(evt);
    }

}
