package ck.ckrc.erie.warchess.utils;

import ck.ckrc.erie.warchess.Main;
import javafx.scene.image.Image;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ResourceSerialization {

    public static File resourceFile=new File(Main.rootFile,"resource");
    protected static Map<String,byte[]> resourceMap=new HashMap<>();

    static {
        if(!resourceFile.exists())resourceFile.mkdirs();
    }

    public static String loadFromByteArray(byte[] arr){
        var encoder= Base64.getEncoder();
        return encoder.encodeToString(arr);
    }

    public static byte[] toByteArray(String str){
        var decoder=Base64.getDecoder();
        return decoder.decode(str);
    }

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

    public static Image getImageFromByteArray(byte[] arr){
        var bis=new ByteArrayInputStream(arr);
        return new Image(bis);
    }

    public static void addResourceWithName(String name,byte[] data){
        resourceMap.put(name,data);
    }

    public static void addResourceWithId(String id,Class<?> clazz,byte[] data){
        resourceMap.put(clazz.getName()+'-'+id,data);
    }

    public static byte[] getResourceByName(String name){
        if(resourceMap.containsKey(name))
            return resourceMap.get(name);
        File f=new File(resourceFile, name);
        if(!f.exists())return null;
        try {
            FileInputStream fis=new FileInputStream(f);
            var arr=fis.readAllBytes();
            resourceMap.put(name,arr);
            return arr;
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] getResourceById(String id,Class<?> clazz){
        String name=clazz.getName()+'-'+id;
        return getResourceByName(name);
    }

    public static URL getURLFromResourceID(String id,Class<?> clazz){
        String name=clazz.getName()+'-'+id;
        try {
            return new URL(null,"bytes:///"+name,new BytesURLHandler());
        } catch (MalformedURLException e) {
            Main.log.addLog("Failed to convert byte array to URL",ResourceSerialization.class);
        }
        return null;
    }

}
