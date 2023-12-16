package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.PreMain;
import org.jd.core.v1.ClassFileToJavaSourceDecompiler;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;
import org.jd.gui.util.decompiler.StringBuilderPrinter;

import java.util.Objects;

public class ClassDecompilerWrapper implements Loader {

    private byte[] data=null;
    private String className=null;

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
