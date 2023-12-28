package ck.ckrc.erie.warchess.utils;

import ck.ckrc.erie.warchess.Main;
import javafx.scene.image.Image;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 资源文件处理和序列化
 * 小型资源可以字符串化方便与类字节码共同传输
 * 大型资源也可利用此类统一获取管理
 */
public class ResourceSerialization {

    /**
     * 资源文件夹
     */
    public static File resourceFile=new File(Main.rootFile,"resource");
    /**
     * 储存资源数据
     */
    public static Map<String,byte[]> resourceMap=new HashMap<>();

    static {
        if(!resourceFile.exists())resourceFile.mkdirs();
    }

    /**
     * 将字节数组转为字符串储存
     * @param arr 字节数组
     * @return base64编码后的字符串
     */
    public static String loadFromByteArray(byte[] arr){
        var encoder= Base64.getEncoder();
        return encoder.encodeToString(arr);
    }

    /**
     * 将字符串解码为字节数组
     * @param str base64编码后的字符串
     * @return 字节数组
     */
    public static byte[] toByteArray(String str){
        var decoder=Base64.getDecoder();
        return decoder.decode(str);
    }

    /**
     * 将文件编码为字符串
     * @param file 被编码文件
     * @return base64编码得到的字符串
     * @throws IOException 如果读取文件失败
     */
    public static String loadFromFile(File file) throws IOException {
        FileInputStream fis=new FileInputStream(file);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = fis.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        fis.close();
        return loadFromByteArray(buffer.toByteArray());
    }

    /**
     * 从字节数组解码图像
     * @param arr 字节数组
     * @return 图像对象
     */
    public static Image getImageFromByteArray(byte[] arr){
        var bis=new ByteArrayInputStream(arr);
        return new Image(bis);
    }

    /**
     * 以限定名名方式添加一个资源
     * @param name 资源名
     * @param data 资源数据
     */
    public static void addResourceWithName(String name,byte[] data){
        resourceMap.put(name,data);
    }

    /**
     * 以名称-类名方式添加一个资源
     * @param id 资源名
     * @param clazz 类名
     * @param data 资源数据
     */
    public static void addResourceWithId(String id,Class<?> clazz,byte[] data){
        resourceMap.put(clazz.getName()+'-'+id,data);
    }

    /**
     * 不使用网络，尝试获取资源
     * @param name 资源限定名
     * @return 资源数据，如果找到。否则返回null
     */
    public static byte[] getResourceByNameWithoutNetwork(String name){
        if(resourceMap.containsKey(name))
            return resourceMap.get(name);
        File f=new File(resourceFile, name);
        if(f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f);
                var arr = fis.readAllBytes();
                resourceMap.put(name, arr);
                fis.close();
                return arr;
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 尝试获取资源
     * @param name 资源限定名
     * @return 资源数据，如果找到。否则返回null
     */
    public static byte[] getResourceByName(String name){
        var x=getResourceByNameWithoutNetwork(name);
        if(x!=null) return x;
        else if(Main.syncThread!=null){
            Main.log.addLog("Requesting resource from remote connections:"+name, ResourceSerialization.class);
            try{
                Main.syncThread.sendResRequire(name);
            } catch (IOException e) {
                Main.log.addLog("Failed to get Resource from connected client:"+name,ResourceSerialization.class);
                Main.log.addLog(e, ResourceSerialization.class);
            }
        }
        return null;
    }

    /**
     * 以名称-类名方式尝试获取资源
     * @param id 名称
     * @param clazz 类
     * @return 资源数据，如果找到。否则返回null
     */
    public static byte[] getResourceById(String id,Class<?> clazz){
        String name=clazz.getName()+'-'+id;
        return getResourceByName(name);
    }

    /**
     * 从资源获取URL
     * @param id 资源名称
     * @param clazz 类名
     * @return URL，注意不能以字符串方式处理
     */
    public static URL getURLFromResourceID(String id,Class<?> clazz){
        String name=clazz.getName()+'-'+id;
        try {
            return new URL(null,"bytes:///"+name,new BytesURLHandler());
        } catch (MalformedURLException e) {
            Main.log.addLog("Failed to convert byte array to URL",ResourceSerialization.class);
        }
        return null;
    }

    /**
     * 将资源文件保存到本地，并获取其URL
     * @param id 资源名
     * @param clazz 类名
     * @return 资源URL
     */
    public static URL putResourceToDisk(String id,Class<?> clazz){
        var data=getResourceById(id,clazz);
        if(data==null)return null;
        try{
            File f=new File(resourceFile,clazz.getName()+'-'+id);
            var fos=new FileOutputStream(f);
            fos.write(data);
            fos.close();
            return f.toURI().toURL();
        } catch (IOException e) {
            Main.log.addLog("Failed to save Resource:"+clazz.getName()+'-'+id, ResourceSerialization.class);
            Main.log.addLog(e, ResourceSerialization.class);
        }
        return null;
    }

}
