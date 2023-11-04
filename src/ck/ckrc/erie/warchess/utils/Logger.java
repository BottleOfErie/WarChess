package ck.ckrc.erie.warchess.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Logger {

    private File target=null;
    private FileWriter writer=null;

    private static final SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    public Logger() throws IOException {

        File source=new File("D:\\erie\\");
        if(!source.exists())source.mkdirs();

        var time=new Date();
        target=new File(source,format.format(time)+".log");
        writer=new FileWriter(target);
    }

    public void addLog(String message,String source){
        try {
            writer.write(String.format("[%s]<%s>:%s\n",source,format.format(new Date()),message));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addLog(String message,Class<?> clazz){
        addLog(message,clazz.getSimpleName());
    }

    public void addLog(Exception e,Class<?> clazz){
        addLog("===EXCEPTION HAPPENED===",clazz);
        e.printStackTrace(new PrintWriter(writer));
    }

    public void addLog(Exception e,String source){
        addLog("===EXCEPTION HAPPENED===",source);
        e.printStackTrace(new PrintWriter(writer));
    }
}
