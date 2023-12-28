package ck.ckrc.erie.warchess.game;

import java.io.Serializable;

public interface DamageListener extends Serializable {

    double takeDamage(double damage);

}
