package ck.ckrc.erie.warchess.example;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.*;
import ck.ckrc.erie.warchess.utils.Math;

import java.util.Objects;

public class GunTower extends Chess {

    public static final int attRadius=5,attDamage=10, build_cost =5,shot_cost=1;
    private DamageEvent myDmgEvt;
    private DamageListener myDmgListener;
    private int target_x,target_y;
    public static final String clazzName="example.GunTower";

    public GunTower(int x,int y,Player player){
        this.x=x;
        this.y=y;
        this.teamFlag=player.getTeamFlag();
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

        Main.currentGameEngine.getPlayer(teamFlag).setStatus(Miner.energyKey,(int)Main.currentGameEngine.getPlayer(teamFlag).getStatus(Miner.energyKey)-build_cost);

    }

    @Override
    public Object showPanel() {
        return null;
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
        int tx=0,ty=0,flg=0;
        for(int i=0;i<Map.MapSize;i++)
            for(int j=0;j<Map.MapSize;j++)
                if(Math.distanceOfEuclid(x,y,i,j)<attRadius&& !Objects.equals(engine.getChess(i, j).teamFlag, this.teamFlag)){
                    if(flg==0) {
                        tx = i; ty = j; flg = 1;
                    }else if(Math.distanceOfEuclid(x,y,tx,ty)> Math.distanceOfEuclid(x,y,i,j)){
                        tx = i; ty = j;
                    }
                }
        target_x=tx;target_y=ty;
    }

    @Override
    public void roundEnd() {
        myDmgEvt=new DamageEvent(target_x,target_y,attDamage,this);
        Main.currentGameEngine.commitDamageEvent(myDmgEvt);
    }

    public static boolean checkPlaceRequirements(Player player) {
        return (int)player.getStatus(Miner.energyKey)>= build_cost;
    }
}
