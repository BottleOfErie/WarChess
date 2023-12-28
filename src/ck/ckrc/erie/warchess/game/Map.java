package ck.ckrc.erie.warchess.game;

import java.util.Random;

/**
 * 地图类，储存地形（没啥用）和各个棋子
 */
public class Map {

    /**
     * 地图大小以及地形最大高度
     */
    public static final int MapSize=10,maxHeight=100;

    /**
     * 棋子数组
     */
    private Chess[][] chessmap;
    /**
     * 地形数组
     */
    private int[][] heightmap;

    /**
     * 声明棋子数组和地形数组<br/>
     * 随机初始化高度
     */
    public Map(){
        chessmap=new Chess[MapSize][MapSize];
        heightmap=new int[MapSize][MapSize];
        Random r=new Random();
        for(int i=0;i<MapSize;i++)
            for(int j=0;j<MapSize;j++)
                heightmap[i][j]=r.nextInt(maxHeight);
    }

    /**
     * 处理伤害溢出情况
     * @param x 伤害溢出位置x坐标
     * @param y 伤害溢出位置y坐标
     * @param damage 伤害溢出量
     */
    public void overDamaged(int x,int y,double damage){
        if(damage>0)heightmap[x][y]-= (int) (damage/10);
        if(heightmap[x][y]<0)heightmap[x][y]=0;
    }

    protected void setChess(int x,int y,Chess chess){
        chessmap[x][y]=chess;
    }

    public Chess[][] getChessMap() {
        return chessmap;
    }

    public int getHeight(int x,int y){
        return heightmap[x][y];
    }
}
