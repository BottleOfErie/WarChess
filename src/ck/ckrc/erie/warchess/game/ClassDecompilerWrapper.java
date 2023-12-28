package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.PreMain;
import org.jd.core.v1.ClassFileToJavaSourceDecompiler;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;
import org.jd.gui.util.decompiler.StringBuilderPrinter;

/**
 * 包裹一个类，用于反编译
 */
public class ClassDecompilerWrapper implements Loader {

    /**
     * 字节码
     */
    private byte[] data=null;
    /**
     * 类名
     */
    private String className=null;

    /**
     *
     * @param data 字节码
     * @param className 类名
     */
    public ClassDecompilerWrapper(byte[] data,String className){
        this.data=data;
        this.className=className;
        if(className==null)
            this.className="";
    }

    @Override
    public boolean canLoad(String s) {
        return className.equals(s) || PreMain.transformer.map.get(s)!=null;
    }

    @Override
    public byte[] load(String s) throws LoaderException {
        if(className.equals(s))
            return data;
        return PreMain.transformer.map.get(s);
    }

    /**
     * 获取反编译结果
     * @return 反编译结果
     */
    public String decompile(){
        ClassFileToJavaSourceDecompiler decompiler=new ClassFileToJavaSourceDecompiler();
        StringBuilderPrinter printer=new StringBuilderPrinter();
        try {
            decompiler.decompile(this,printer,this.className);
        } catch (Exception e) {
            Main.log.addLog(e,this.getClass());
            return null;
        }
        return printer.getStringBuffer().toString();
    }

}
