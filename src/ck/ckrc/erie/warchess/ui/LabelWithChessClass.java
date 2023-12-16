package ck.ckrc.erie.warchess.ui;

import javafx.scene.control.Label;

public class LabelWithChessClass extends Label {

    private String clazz=null;

    public String getClazzSimple() {
        return clazzSimple;
    }

    private String clazzSimple=null;

    public LabelWithChessClass(String className,String classSimpleName){
        super("classname:"+classSimpleName);
        this.clazz=className;
        clazzSimple=classSimpleName;
    }

    public String getClazz() {
        return clazz;
    }

}
