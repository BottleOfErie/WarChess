package ck.ckrc.erie.warchess.game;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashMap;

public class ChessClassExtractor implements ClassFileTransformer {

    public HashMap<String,byte[]> map=new HashMap<>();
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        map.put(className.replace("/","."),classfileBuffer);
        return null;
    }

}
