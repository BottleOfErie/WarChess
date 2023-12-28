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

/**
 * 数据包，用于多人游戏同步棋子信息
 */
public class DataPackage implements Serializable {

    /**
     * 这个数据包包括的信息
     */
    private Map<String,Object> map=null;
    /**
     * 数据包自动打包忽略的字段
     */
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
     * 自动读取数据包，将响应数据分配给同名的字段
     * 不会处理{@link Chess}内定义的字段，即hp,x,y,teamFlag,className
     * 这些数据应当使用super.syncDataPackage()处理
     * @param chess 被处理的棋子对象
     * @param clazz 这个棋子对应的类
     * @param pack 数据包
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
     * 自动生成数据包
     * 可处理原始类型、枚举类型、字符串和{@link Serializable}可序列化子类
     * 不处理数组，不处理static或final修饰的字段
     * @param chess 数据来源棋子
     * @param clazz 棋子对应的类
     * @return 数据包
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

    /**
     * 内部方法，用于打包忽略字段
     * @param pack 数据包
     * @param fieldName 被处理字段
     * @param chess 被处理棋子对象
     * @param clazz 被处理的类
     * @throws NoSuchFieldException 如果没有这个字段
     * @throws IllegalAccessException 如果没有访问权限
     */
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
