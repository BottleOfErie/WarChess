package ck.ckrc.erie.warchess.game;

public class DamageEvent {

    private final int x,y,damage;
    private final Chess parent;
    private Object flag;

    public DamageEvent(int x, int y, int damage, Chess parent){
        this.x = x;
        this.y = y;

        this.damage = damage;
        this.parent = parent;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDamage() {
        return damage;
    }

    public boolean check() {
        return parent.checkEvent(this);
    }

    public Object getFlag() {
        return flag;
    }

    public void setFlag(Object flag) {
        this.flag = flag;
    }
}
