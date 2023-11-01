package ck.ckrc.erie.warchess.game;

public class DamageEvent {

    private final int x,y;
    private final double damage;
    private final Chess parent;
    private Object flag;

    public DamageEvent(int x, int y, double damage, Chess parent){
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

    public double getDamage() {
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
