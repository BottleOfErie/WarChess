package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.PreMain;

import java.io.*;
import java.util.*;
import java.util.Map;

public class ChessClassLoader extends ClassLoader{

    private Map<String,Class<?>> chessClass =null;

    public ChessClassLoader(){
        super();
        chessClass =new HashMap<>();
        Main.log.addLog("Initialized",this.getClass());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if(chessClass.containsKey(name))return chessClass.get(name);
        else {
            Main.log.addLog("NoSuchClass:"+name,this.getClass());
            throw new ClassNotFoundException(name);
        }
    }

    public Class<?> loadChessClassFromByteArray(byte[] byteArr){
        var clazz=this.defineClass(null,byteArr,0,byteArr.length);
        var name=getChessClassName(clazz);
        chessClass.put(name,clazz);
        PreMain.transformer.map.put(name,byteArr);
        Main.log.addLog("Loaded Class:"+name,this.getClass());
        return clazz;
    }

    public Class<?> loadChessClassFromFile(File f) {
        Class<?> result = null;
        Main.log.addLog("Loading class from file:" + f.toString(), this.getClass());
        try (FileInputStream fis = new FileInputStream(f)) {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            int t = 0;
            while (-1 != (t = fis.read())) bas.write(t);
            var byteArr = bas.toByteArray();
            result = loadChessClassFromByteArray(byteArr);
        } catch (FileNotFoundException e) {//24
            Main.log.addLog(e, this.getClass());
        } catch (IOException e) {
            Main.log.addLog(e, this.getClass());
        }
        return result;
    }

    public String getChessClassName(Class<?> clazz){
        try {
            var field=clazz.getField("className");
            return (String)field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    public void addChessClass(Class<?> clazz){
        Main.log.addLog("Loaded Class:"+clazz.getName(),this.getClass());
        chessClass.put(clazz.getName(),clazz);
    }

    public Collection<Class<?>> getChessClass(){
        return chessClass.values();
    }

    public Set<String> getChessClassNames(){
        return chessClass.keySet();
    }

    public Class<?> getClassByName(String name){return chessClass.get(name);}

}
