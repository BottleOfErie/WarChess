package ck.ckrc.erie.warchess.game;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.HashMap;

public class ChessClassExtractor implements ClassFileTransformer{

    public HashMap<String,byte[]> map=new HashMap<>();

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        map.put(className.replace("/","."),classfileBuffer);
        //System.out.println(className+":"+loader.getName());
        return classfileBuffer;
    }
}
