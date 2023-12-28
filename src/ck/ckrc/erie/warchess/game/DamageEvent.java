package ck.ckrc.erie.warchess.game;

/**
 * 定义棋盘中造成伤害的事件
 */
public class DamageEvent {

    /**
     * 伤害发生位置
     */
    private final int x,y;
    /**
     * 伤害值
     */
    private final double damage;
    /**
     * 伤害发送者
     */
    private final Chess parent;

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

    /**
     * 检查这个伤害事件是否仍然有效
     * @return 检查结果，参见{@link Chess}中checkEvent方法
     */
    public boolean check() {
        return parent.checkEvent(this);
    }
}
