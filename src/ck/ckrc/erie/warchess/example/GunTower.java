package ck.ckrc.erie.warchess.example;

import ck.ckrc.erie.warchess.game.Chess;
import ck.ckrc.erie.warchess.game.DamageEvent;
import ck.ckrc.erie.warchess.game.DamageListener;

public class GunTower implements Chess {

    private DamageEvent myDmgEvt;
    private DamageListener myDmgListener;

    public GunTower(int x,int y){

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
        return false;
    }

    @Override
    public void roundBegin() {

    }

    @Override
    public void roundEnd() {

    }
}
