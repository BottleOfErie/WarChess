package ck.ckrc.erie.warchess.utils;

import ck.ckrc.erie.warchess.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 统一日志器
 */
public class Logger {

    /**
     * Debug选项开关
     * 设为true，则不记录日志文件，改为从标准输出流输出
     */
    public boolean debug=false;

    /**
     * 日志文件写入器
     */
    private FileWriter writer=null;

    /**
     * 日志时间格式化
     */
    private static final SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    /**
     * 初始化日志文件夹、日志文件等
     * @throws IOException 如果无法初始化
     */
    public Logger() throws IOException {

        File source=new File(Main.rootFile, "log");
        if(!source.exists())source.mkdirs();

        var time=new Date();
        File target = new File(source, format.format(time) + ".log");
        writer=new FileWriter(target);
    }

    /**
     * 记录一条日志
     * @param message 日志内容
     * @param source 日志发送者
     */
    public void addLog(String message,String source){
        if(debug) {
            System.out.printf("[%s]<%s>:%s%n", source, format.format(new Date()), message);
            return;
        }
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
