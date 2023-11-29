package ck.ckrc.erie.warchess.utils;

import javafx.scene.image.Image;

import java.io.*;
import java.util.Base64;

public class ResourceSerialization {

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

}
