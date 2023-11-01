package ck.ckrc.erie.warchess.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {

    private File target=null;
    private FileWriter fwriter=null;
    private BufferedWriter writer=null;

    public Logger() throws IOException {

        File source=new File("D:\\erie\\");
        if(!source.exists())source.mkdirs();

        target=new File(source,new Date().toString()+".log");
        fwriter=new FileWriter(target);
        writer=new BufferedWriter(fwriter);
    }

    public void addLog(String message,String source){
        try {
            writer.write(String.format("[%s]<%s>:%s",source,new Date().toString(),message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
