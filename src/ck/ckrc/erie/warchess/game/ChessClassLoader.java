package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.Main;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ChessClassLoader extends ClassLoader{

    private Map<String,Class<?>> clazzs=null;

    public ChessClassLoader(){
        super();
        clazzs=new HashMap<>();
        Main.log.addLog("Initialized",this.getClass());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if(clazzs.containsKey(name))return clazzs.get(name);
        else {
            Main.log.addLog("NoSuchClass:"+name,this.getClass());
            throw new ClassNotFoundException(name);
        }
    }

    public Class<?> loadChessClassFromByteArray(byte[] byteArr,String name){
        var clazz=this.defineClass(name,byteArr,0,byteArr.length);
        clazzs.put(name,clazz);
        Main.log.addLog("Loaded Class:"+name,this.getClass());
        return clazz;
    }

    public Class<?> loadChessClassFromFile(File f,String name){
        Main.log.addLog("Loading class from file:"+f.toString(),this.getClass());
        try {
            FileInputStream fis=new FileInputStream(f);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            int t=0;
            while(-1!=(t=fis.read()))baos.write(t);
            var byteArr=baos.toByteArray();
            return loadChessClassFromByteArray(byteArr,name);
        } catch (FileNotFoundException e) {//24
            Main.log.addLog(e,this.getClass());
            throw new RuntimeException(e);
        } catch (IOException e) {
            Main.log.addLog(e,this.getClass());
            throw new RuntimeException(e);//27
        }
    }

    public void addClazz(Class<?> clazz){
        Main.log.addLog("Loaded Class:"+clazz.getName(),this.getClass());
        clazzs.put(clazz.getName(),clazz);
    }

    public Collection<Class<?>> getClazzs(){
        return clazzs.values();
    }

}
