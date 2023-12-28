package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.Main;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 用于执行棋子类的静态方法<br/>
 * 具体使用用途，请参见{@link Chess}类<br/>
 * 也可用于封装一个棋子类便于统一调用
 */
public class ChessClassInvoker {

    /**
     * 获取{@link Chess}内的className字段
     * @param clazz 被操纵的类
     * @return className字段的值
     */
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
        } catch (InvocationTargetException e) {
            Main.log.addLog("this class doesn't have right checkPlaceRequirements method:"+clazz, ChessClassInvoker.class);
            Main.log.addLog(e, ChessClassInvoker.class);
        } catch (NoSuchMethodException | IllegalAccessException ignored) {}
        return false;
    }

    public static Chess getNewInstance(Class<?> clazz,Player player,int x,int y){
        try{
            Constructor<?> f=clazz.getConstructor(int.class,int.class,Player.class);
            f.setAccessible(true);
            return (Chess) f.newInstance(x,y,player);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            Main.log.addLog("this class doesn't have right constructor:"+clazz, ChessClassInvoker.class);
            Main.log.addLog(e,ChessClassInvoker.class);
        } catch (IllegalAccessException ignored) {}
        return null;
    }

    public static Node invokeShowData(Class<?> clazz){
        try{
            Method f=clazz.getDeclaredMethod("showData");
            f.setAccessible(true);
            return (Node) f.invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            Main.log.addLog("this class doesn't have right showData method:"+clazz, ChessClassInvoker.class);
        } catch (IllegalAccessException ignored) {}
        return new Label("No Data");
    }

    private Class<?> clazz=null;
    private String className=null;
    private Method checkPlaceRequirements=null;
    private Constructor<?> constructor=null;

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
        try{
            constructor=clazz.getConstructor(int.class,int.class,Player.class);
            constructor.setAccessible(true);
            constructor.newInstance(1,1,Player.getNewPlayer(1));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            Main.log.addLog("this class doesn't have right constructor:"+clazz, ChessClassInvoker.class);
            throw new NoSuchMethodException("No constructor");
        } catch (IllegalAccessException ignored) {}
    }

    public String getClassName(){
        return className;
    }

    public boolean invokeCheckPlaceRequirements(Player player,int x,int y){
        try {
            return (boolean) checkPlaceRequirements.invoke(null,x,y,player);
        } catch (InvocationTargetException e) {
            Main.log.addLog("Exception in this class's checkPlaceRequirements method:"+clazz+",args:"+player+","+x+","+y, ChessClassInvoker.class);
            return false;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    public Chess getNewInstance(Player player,int x,int y){
        try{
            return (Chess) constructor.newInstance(player,x,y);
        } catch (InvocationTargetException | InstantiationException e) {
            Main.log.addLog("this class doesn't have right constructor:"+clazz, ChessClassInvoker.class);
        } catch (IllegalAccessException ignored) {}
        return null;
    }

    public Node invokeShowData(){
        try{
            Method f=clazz.getDeclaredMethod("showData");
            f.setAccessible(true);
            return (Node) f.invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            Main.log.addLog("this class doesn't have right showData method:"+clazz, ChessClassInvoker.class);
        } catch (IllegalAccessException ignored) {}
        return null;
    }

}
