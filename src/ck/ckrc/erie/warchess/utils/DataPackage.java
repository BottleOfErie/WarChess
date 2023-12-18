package ck.ckrc.erie.warchess.utils;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.Chess;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataPackage implements Serializable {

    private Map<String,Object> map=null;
    public static final List<String> ignoredFields=new LinkedList<>();
    static {
        ignoredFields.add("hp");
        ignoredFields.add("teamFlag");
        ignoredFields.add("className");
        ignoredFields.add("x");
        ignoredFields.add("y");
    }

    public DataPackage(){
        map=new HashMap<>();
    }

    public Object get(String name){
        return map.get(name);
    }

    public void put(String name,Serializable data){
        map.put(name,data);
    }

    /**
     * won't process hp and teamFlag!
     * use super.syncDataPackage() instead
     *
     */
    public static void processDataPackage(Chess chess,Class<? extends Chess> clazz,DataPackage pack){
        for(var key:pack.map.keySet()){
            try {
                if(ignoredFields.contains(key))continue;
                Field field=clazz.getDeclaredField(key);
                field.setAccessible(true);
                field.set(chess,pack.get(field.getName()));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                Main.log.addLog("cannot process this field:"+key+" in "+clazz,DataPackage.class);
            }
        }
    }


    /**
     * won't serialize arrays!
     */
    public static DataPackage generateDataPackage(Chess chess,Class<? extends Chess> clazz){
        try {
            DataPackage ret=new DataPackage();
            for(var item:ignoredFields)
                processDataPackForPublicField(ret,item,chess,clazz);
            for(Field field:clazz.getDeclaredFields()){
                field.setAccessible(true);
                var modifiers=field.getModifiers();
                if(Modifier.isFinal(modifiers)||Modifier.isStatic(modifiers))continue;
                var typ=field.getType();
                if(typ.isPrimitive()||typ.isEnum()||typ==String.class||typ.isAssignableFrom(Serializable.class))
                    ret.map.put(field.getName(), field.get(chess));
                else
                    Main.log.addLog("Cannot serialize:"+typ,DataPackage.class);
            }
            return ret;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Main.log.addLog("Cannot generate DataPackage for:"+clazz,DataPackage.class);
            Main.log.addLog(e, DataPackage.class);
            return null;
        }
    }

    private static void processDataPackForPublicField(DataPackage pack, String fieldName, Chess chess, Class<?> clazz) throws NoSuchFieldException, IllegalAccessException {
        Field field=clazz.getField(fieldName);
        field.setAccessible(true);
        pack.map.put(fieldName, field.get(chess));
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
