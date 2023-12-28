package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.PreMain;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ChessClassLoader extends ClassLoader{

    private Map<String,Class<?>> chessClass =null;
    private static class TempClassLoader extends ClassLoader {

        private Class<?> clazz=null;

        @Override
        protected Class<?> findClass(String name) {
            if (clazz != null && name.equals(clazz.getName())) return clazz;
            return null;
        }

        public Class<?> loadClass(byte[] data){
            try {
                clazz = this.defineClass(null, data, 0, data.length);
            }catch (Exception | Error ignored){}
            return clazz;
        }
    }

    public ChessClassLoader(){
        super("Chess",getSystemClassLoader());
        chessClass =new HashMap<>();
        Main.log.addLog("Initialized",this.getClass());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println(name);
        if(chessClass.containsKey(name))return chessClass.get(name);
        else {
            Main.log.addLog("NoSuchClass:"+name,this.getClass());
            throw new ClassNotFoundException(name);
        }
    }

    private String preloadClassCheck(byte[] byteArr){
        if(byteArr==null) return null;
        var temp=new TempClassLoader();
        var tclazz= temp.loadClass(byteArr);
        if(tclazz==null) return null;
        var name= tclazz.getName();
        tclazz=null;
        temp=null;
        return name;
    }

    public Class<?> loadChessClassFromByteArray(byte[] byteArr){
        var name=preloadClassCheck(byteArr);
        if(name==null)return null;
        if(chessClass.containsKey(name)) {
            var choice = getAlert(name).showAndWait();
            if (choice.isEmpty() || choice.get() != ButtonType.OK) return null;
        }
        var clazz=this.defineClass(name,byteArr,0,byteArr.length);
        chessClass.put(name,clazz);
        PreMain.transformer.map.put(name,byteArr);
        Main.log.addLog("Loaded Class:"+name,this.getClass());
        return clazz;
    }

    public void syncLoadedClassFromAgent(byte[] byteArr){
        var clazz= new TempClassLoader().loadClass(byteArr);
        if(clazz==null) return;
        var name=clazz.getName();
        chessClass.put(name,clazz);
    }

    private Alert getAlert(String name){
        var dialog=new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setContentText("这个类("+name+")似乎已经加载过了，是否覆盖？");
        return dialog;
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

    public Collection<Class<?>> getAllClass(){
        return chessClass.values();
    }

    public Collection<Class<?>> getAllChessClass(){
        return chessClass.values().stream().filter((Class<?>clazz)->clazz.getSuperclass()==Chess.class).toList();
    }

    public Class<?> getClassByName(String name){return chessClass.get(name);}

}
