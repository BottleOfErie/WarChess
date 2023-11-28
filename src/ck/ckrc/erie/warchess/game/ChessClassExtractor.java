package ck.ckrc.erie.warchess.game;

import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashMap;

public class ChessClassExtractor implements ClassFileTransformer, Loader {

    public HashMap<String,byte[]> map=new HashMap<>();
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        map.put(className.replace("/","."),classfileBuffer);
        return null;
    }

    @Override
    public boolean canLoad(String s) {
        return map.get(s)!=null;
    }

    @Override
    public byte[] load(String s) throws LoaderException {
        return map.get(s);
    }
}
