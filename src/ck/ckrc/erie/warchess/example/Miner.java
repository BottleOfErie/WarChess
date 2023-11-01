package ck.ckrc.erie.warchess.example;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.*;

import java.util.Objects;

public class Miner extends Chess {
    public static final int productions=10, build_cost =10;
    public static final String energyKey="example.red-stoneFlux";
    private DamageListener myDmgListener;
    public static final String clazzName="example.Miner";

    public Miner(int x,int y,Player p){
        this.x=x;
        this.y=y;
        this.teamFlag=p.getTeamFlag();
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

        Main.currentGameEngine.getPlayer(teamFlag).setStatus(energyKey,(int)Main.currentGameEngine.getPlayer(teamFlag).getStatus(energyKey)-build_cost);

    }

    @Override
    public Object showPanel() {
        return null;
    }

    @Override
    public boolean checkEvent(DamageEvent evt) {
        return false;
    }

    @Override
    public boolean checkListener(DamageListener listener) {
        if(hp<=0)return false;
        return myDmgListener==listener;
    }

    @Override
    public void roundBegin() {
        //generate auto target
        if(!Objects.equals(Main.currentGameEngine.getCurrentTeam(), teamFlag))return;
        Main.currentGameEngine.getPlayer(teamFlag).setStatus(energyKey,(int)Main.currentGameEngine.getPlayer(teamFlag).getStatus(energyKey)+productions);
    }

    public static boolean checkPlaceRequirements(Player player) {
        return (int)player.getStatus(energyKey)>= build_cost;
    }

    public static void playerInit(Player player){
        player.setStatus(energyKey,10);
    }

}
