package ck.ckrc.erie.warchess.utils;

import ck.ckrc.erie.warchess.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    public boolean debug=false;

    private FileWriter writer=null;

    private static final SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    public Logger() throws IOException {

        File source=new File(Main.rootFile, "log");
        if(!source.exists())source.mkdirs();

        var time=new Date();
        File target = new File(source, format.format(time) + ".log");
        writer=new FileWriter(target);
    }

    public void addLog(String message,String source){
        if(debug)
            System.out.printf("[%s]<%s>:%s%n",source,format.format(new Date()),message);
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
        if(debug) {
            e.printStackTrace();
            return;
        }
        e.printStackTrace(new PrintWriter(writer));
    }

    public void addLog(Exception e,String source){
        addLog("===EXCEPTION HAPPENED===",source);
        if(debug) {
            e.printStackTrace();
            return;
        }
        e.printStackTrace(new PrintWriter(writer));
    }
}
