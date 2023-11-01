package ck.ckrc.erie.warchess.game;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ChessClassLoader extends ClassLoader{

    private Map<String,Class<?>> clazzs=null;

    public ChessClassLoader(){
        super();
        clazzs=new HashMap<>();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if(clazzs.containsKey(name))return clazzs.get(name);
        else throw new ClassNotFoundException(name);
    }

    public Class<?> loadChessClassFromByteArray(byte[] byteArr,String name){
        var clazz=this.defineClass(name,byteArr,0,byteArr.length);
        clazzs.put(name,clazz);
        return clazz;
    }

    public Class<?> loadChessClassFromFile(File f,String name){
        try {
            FileInputStream fis=new FileInputStream(f);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            int t=0;
            while(-1!=(t=fis.read()))baos.write(t);
            var byteArr=baos.toByteArray();
            return loadChessClassFromByteArray(byteArr,name);
        } catch (FileNotFoundException e) {//24
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);//27
        }
    }

    public Collection<Class<?>> getClazzs(){
        return clazzs.values();
    }

}
