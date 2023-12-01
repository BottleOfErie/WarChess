package ck.ckrc.erie.warchess.game;

import ck.ckrc.erie.warchess.ui.GameScene;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class PlayAction implements EventHandler<MouseEvent> {
    private GraphicsContext graphicsContext;
    boolean haschess=false;
    public PlayAction(GraphicsContext graphicsContext){
        this.graphicsContext=graphicsContext;
    }
    @Override
    public void handle(MouseEvent event){
        int x=(int)(event.getX() / 60);
        int y=(int)(event.getY() / 60);

        if(!haschess){
            drawchess(x, y);
        }
        else{
            graphicsContext.clearRect(x*60+10, y*60+10, 40, 40);
        }
    }
    private void drawchess(int x, int y){
        Image image=new Image("/Images/OIP.jpg");
        graphicsContext.drawImage(image,x*60+10,y*60+10,40,40);
    }
}
