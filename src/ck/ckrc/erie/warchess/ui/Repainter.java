package ck.ckrc.erie.warchess.ui;

public class Repainter extends Thread{

    public boolean repainting=true;

    @Override
    public void run() {
        try{
            while (repainting) {
                Play.drawAllChess();
                Thread.sleep(20);//50fps
            }
        } catch (InterruptedException ignored) {

        }
    }
}
