package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.ui.Play;
import ck.ckrc.erie.warchess.ui.Setting;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Player implements Serializable{

    private final int teamFlag;

    private Map<String, Serializable> status;

    public Player(int teamFlag){
        this.teamFlag=teamFlag;
        status=new HashMap<>();
    }

    public Object getStatus(String key){
        return status.get(key);
    }
    public void setStatus(String key,Serializable value){status.put(key,value);}

    public int getTeamFlag(){return teamFlag;}

    public static Player getNewPlayer(int teamFlag){
        Player ret=new Player(teamFlag);
        for (Class<?> clazz:
                Play.classlist) {
            if(!Setting.loadornot.get(clazz))continue;
            try {
                var mtd=clazz.getDeclaredMethod("playerInit",Player.class);
                mtd.setAccessible(true);
                mtd.invoke(null,ret);
            } catch (NoSuchMethodException ignored) {
            } catch (InvocationTargetException e) {
                Main.log.addLog(clazz.getName()+"->playerInit() cannot be invoked properly","InitPlayer");
                Main.log.addLog(e,Player.class);
            } catch (IllegalAccessException e) {
                Main.log.addLog(e,Player.class);
                throw new RuntimeException(e);
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder("Player("+teamFlag+"):\n");
        for(var item:status.keySet()){
            builder.append(item);
            builder.append(':');
            builder.append(status.get(item).toString());
        }
        return builder.toString();
    }
}
