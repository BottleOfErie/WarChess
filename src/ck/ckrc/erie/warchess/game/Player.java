package ck.ckrc.erie.warchess.game;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private final int teamFlag;

    private Map<String,Object> status;

    public Player(int teamFlag){
        this.teamFlag=teamFlag;
        status=new HashMap<>();
    }

    public Object getStatus(String key){
        return status.get(key);
    }
    public void setStatus(String key,Object value){status.put(key,value);}

    public int getTeamFlag(){return teamFlag;}

}
