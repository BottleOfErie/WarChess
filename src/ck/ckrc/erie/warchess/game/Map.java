package ck.ckrc.erie.warchess.game;

import java.util.Random;

public class Map {

    public static final int MapSize=10,maxHeight=100;

    private Chess[][] chessmap;
    private int[][] heightmap;

    public Map(){
        chessmap=new Chess[MapSize][MapSize];
        heightmap=new int[MapSize][MapSize];
        Random r=new Random();
        for(int i=0;i<MapSize;i++)
            for(int j=0;j<MapSize;j++)
                heightmap[i][j]=r.nextInt(maxHeight);
    }

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
