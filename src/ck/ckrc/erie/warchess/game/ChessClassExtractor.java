package ck.ckrc.erie.warchess.game;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.HashMap;

/**
 * 用于在类加载前提取被加载类的字节码<br/>
 * 也可手动储存新加载的字节码
 */
public class ChessClassExtractor implements ClassFileTransformer{

    /**
     * 储存字节码的表
     */
    public HashMap<String,byte[]> map=new HashMap<>();

    /**
     * 仅用于记录字节码，不实际修改
     * @param loader                the defining loader of the class to be transformed,
     *                              may be {@code null} if the bootstrap loader
     * @param className             the name of the class in the internal form of fully
     *                              qualified class and interface names as defined in
     *                              <i>The Java Virtual Machine Specification</i>.
     *                              For example, <code>"java/util/List"</code>.
     * @param classBeingRedefined   if this is triggered by a redefine or retransform,
     *                              the class being redefined or retransformed;
     *                              if this is a class load, {@code null}
     * @param protectionDomain      the protection domain of the class being defined or redefined
     * @param classfileBuffer       the input byte buffer in class file format - must not be modified
     *
     * @return 原输入字节码
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        map.put(className.replace("/","."),classfileBuffer);
        //System.out.println(className+":"+loader.getName());
        return classfileBuffer;
    }
}
