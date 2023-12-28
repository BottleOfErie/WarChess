package ck.ckrc.erie.warchess.game;

/**
 * 伤害监听器接口
 */
public interface DamageListener {

    /**
     * 处理伤害链
     * @param damage 收到的伤害
     * @return 溢出未处理的伤害
     */
    double takeDamage(double damage);

}
