package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.PreMain;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 棋子类加载器
 */
public class ChessClassLoader extends ClassLoader{

    /**
     * 储存已加载的类
     */
    private Map<String,Class<?>> chessClass =null;

    /**
     * 临时类加载器
     */
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

    /**
     * 类加载器名：Chess<br/>
     * 父类加载器：app
     */
    public ChessClassLoader(){
        super("Chess",getSystemClassLoader());
        chessClass =new HashMap<>();
        Main.log.addLog("Initialized",this.getClass());
    }

    /**
     * 根据限定名寻找类
     * @param name
     *          The <a href="#binary-name">binary name</a> of the class
     *
     * @return 限定名相同的类
     * @throws ClassNotFoundException 如果没有记录这个名字
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println(name);
        if(chessClass.containsKey(name))return chessClass.get(name);
        else {
            Main.log.addLog("NoSuchClass:"+name,this.getClass());
            throw new ClassNotFoundException(name);
        }
    }

    /**
     * 类加载前，用临时类加载器获取它的类名，避免覆盖已有的类
     * @param byteArr 被加载的字节码
     * @return 类名，如果可以成功加载。否则返回null。
     */
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

    /**
     * 从字节码加载类
     * @param byteArr 字节码
     * @return 解码得到的类，如果成功加载。否则返回null
     */
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

    /**
     * 从周围方法获取类冲突时的确认框
     * @param name 类名
     * @return 确认提示框
     */
    private Alert getAlert(String name){
        var dialog=new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setContentText("这个类("+name+")似乎已经加载过了，是否覆盖？");
        return dialog;
    }

    /**
     * 从文件加载类
     * @param f 文件对象
     * @return 得到的类，如果成功加载。否则返回null
     */
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

    /**
     * 手动添加类，不保证类加载器可用
     * @param clazz 被添加的类
     * @deprecated 不保证可用，不支持内部类
     */
    public void addChessClass(Class<?> clazz){
        Main.log.addLog("Loaded Class:"+clazz.getName(),this.getClass());
        chessClass.put(clazz.getName(),clazz);
    }

    /**
     * 获取类加载器内所有记录的类
     * @return 所有类
     */
    public Collection<Class<?>> getAllClass(){
        return chessClass.values();
    }

    /**
     * 获取类加载器内所有{@link Chess}子类
     * @return 所有满足条件的类
     */
    public Collection<Class<?>> getAllChessClass(){
        return chessClass.values().stream().filter((Class<?>clazz)->clazz.getSuperclass()==Chess.class).toList();
    }

    /**
     * 通过类名获取类
     * @param name 类名
     * @return 指定的类，如果存在。
     */
    public Class<?> getClassByName(String name){return chessClass.get(name);}

}
