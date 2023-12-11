package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.Main;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//use to wrap static methods
public class ChessClassInvoker {

    public static String getClassName(Class<?> clazz) {
        String ret=null;
        try{
            Field f = clazz.getDeclaredField("className");
            f.setAccessible(true);
            ret=(String) f.get(null);
        } catch (NoSuchFieldException e) {
            Main.log.addLog("this class doesn't have className field:"+clazz, ChessClassInvoker.class);
        } catch (IllegalAccessException ignored) {}
        return ret;
    }

    public static boolean invokeCheckPlaceRequirements(Class<?> clazz,Player player,int x,int y){
        try{
            Method f=clazz.getDeclaredMethod("checkPlaceRequirements",Player.class,int.class,int.class);
            f.setAccessible(true);
            return (boolean) f.invoke(null,player,x,y);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            Main.log.addLog("this class doesn't have right checkPlaceRequirements method:"+clazz, ChessClassInvoker.class);
        } catch (IllegalAccessException ignored) {}
        return false;
    }

    private Class<?> clazz=null;
    private String className=null;
    private Method checkPlaceRequirements=null;

    public ChessClassInvoker(Class<?> clazz) throws NoSuchFieldException, NoSuchMethodException {
        this.clazz=clazz;
        try{
            Field f = clazz.getDeclaredField("className");
            f.setAccessible(true);
            className=(String) f.get(null);
        } catch (NoSuchFieldException e) {
            Main.log.addLog("this class doesn't have className field:"+clazz, ChessClassInvoker.class);
            throw new NoSuchFieldException("No className field");
        } catch (IllegalAccessException ignored) {}
        try{
            checkPlaceRequirements=clazz.getDeclaredMethod("checkPlaceRequirements",Player.class,int.class,int.class);
            checkPlaceRequirements.setAccessible(true);
            checkPlaceRequirements.invoke(null,Player.getNewPlayer(1),1,1);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            Main.log.addLog("this class doesn't have right checkPlaceRequirements method:"+clazz, ChessClassInvoker.class);
            throw new NoSuchMethodException("No checkPlaceRequirements method");
        } catch (IllegalAccessException ignored) {}
    }

    public String getClassName(){
        return className;
    }

    public boolean invokeCheckPlaceRequirements(Player player,int x,int y){
        try {
            return (boolean) checkPlaceRequirements.invoke(null,player,x,y);
        } catch (InvocationTargetException e) {
            Main.log.addLog("Exception in this class's checkPlaceRequirements method:"+clazz+",args:"+player+","+x+","+y, ChessClassInvoker.class);
            return false;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

}
