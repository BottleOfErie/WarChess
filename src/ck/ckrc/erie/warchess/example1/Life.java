package ck.ckrc.erie.warchess.example1;

import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.*;
import ck.ckrc.erie.warchess.utils.ResourceSerialization;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class Life extends Chess {

    public static final String imageData0="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";
    public static final Image image0= ResourceSerialization.getImageFromByteArray(ResourceSerialization.toByteArray(imageData0));
    public static final String imageData1="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cy5hZG9iZS5jb20vdGlmZi8xLjAvIgogICAgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIgogICB4bXBNTTpEb2N1bWVudElEPSJnaW1wOmRvY2lkOmdpbXA6MzIwNDhhYjktZGM2NS00YThiLWE0OGItM2YyODEwYTYxMjk3IgogICB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOmViZmYzMGY5LWIwZTUtNGQ5Zi05MTFiLTY0YjhhZDg3OGQ4YSIKICAgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOmIyOWZmODhlLWM3NGMtNGU2OC04MzZjLTc5OWEyOWIzMmZlNiIKICAgZGM6Rm9ybWF0PSJpbWFnZS9wbmciCiAgIEdJTVA6QVBJPSIyLjAiCiAgIEdJTVA6UGxhdGZvcm09IldpbmRvd3MiCiAgIEdJTVA6VGltZVN0YW1wPSIxNzAxMjgwODI3OTM2MzM0IgogICBHSU1QOlZlcnNpb249IjIuMTAuMzIiCiAgIHRpZmY6T3JpZW50YXRpb249IjEiCiAgIHhtcDpDcmVhdG9yVG9vbD0iR0lNUCAyLjEwIgogICB4bXA6TWV0YWRhdGFEYXRlPSIyMDIzOjExOjMwVDAyOjAwOjI3KzA4OjAwIgogICB4bXA6TW9kaWZ5RGF0ZT0iMjAyMzoxMTozMFQwMjowMDoyNyswODowMCI+CiAgIDx4bXBNTTpIaXN0b3J5PgogICAgPHJkZjpTZXE+CiAgICAgPHJkZjpsaQogICAgICBzdEV2dDphY3Rpb249InNhdmVkIgogICAgICBzdEV2dDpjaGFuZ2VkPSIvIgogICAgICBzdEV2dDppbnN0YW5jZUlEPSJ4bXAuaWlkOjEzYjA5M2NiLTliYTgtNGM1NS04NjY5LTZjYTMxZjU5N2Q2MiIKICAgICAgc3RFdnQ6c29mdHdhcmVBZ2VudD0iR2ltcCAyLjEwIChXaW5kb3dzKSIKICAgICAgc3RFdnQ6d2hlbj0iMjAyMy0xMS0zMFQwMjowMDoyNyIvPgogICAgPC9yZGY6U2VxPgogICA8L3htcE1NOkhpc3Rvcnk+CiAgPC9yZGY6RGVzY3JpcHRpb24+CiA8L3JkZjpSREY+CjwveDp4bXBtZXRhPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgCjw/eHBhY2tldCBlbmQ9InciPz7KDcU5AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAB3RJTUUH5wsdEgAbh/GBOwAADxBJREFUaN61mVtsVOX6xn/rOOdOmR4o0FIo1LYibWPlICcTDqKiIokJGLnSyJUXEk28MPFCjRfGxAvChYQE8YSx0UQRUGuAAoFUpAYoNECbFlraaUunnfNhnfbFP+tLu/+6t9DuJpN2VqZrvme97/s8z/d8kmmajizLzMaP4zhIkoTjONi2jaIo3Lhxg3PnzmHbNs3NzbS0tACgKAqGYaCq6qx8t+Q4juMuYsY3kyQ++ugjXn/9dbq7u6moqEBVVbLZLIqikEwmkWWZQqHAwoULOXv2LM8+++ysAJFnCsBxHPL5PJcuXWJgYADbttm/fz/BYJBUKoVhGJimiW3byLKMruv4fD5GRkaora3l22+/pbW1Fdu2MQwDy7IwDIOvvvqKZDLJ7du3/3lFHgSM2zqO4zA2NkY0GmVsbIyGhgaGhobQdR3TNCkpKUFRFCRJwrIsTNPEcRwKhQKyLItriqJQW1vLqVOnGB8fx7ZtGhoaqK+vx+/3//eKPHApZZlbt27x2muvcfToUebPn09FRQXhcJiamhoCgQClpaV4vV4KhYIA7vF4UBQFTdOwLIvJyUlSqRTZbJbLly+zdOlS7t27x/bt26msrMQwjP9dRSRJor29nZaWFqLRKKqqEgwGyWQyKIoiFp/P59F1Hdu20XVd/H88HhdVtW2bdDpNPp8nn89jGAaBQICysjLa29uprKzk6aefFkQyq0AsyyIWi1EoFAiHw4yPj+P3+8lms4KFJElC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";
    public static final Image image1= ResourceSerialization.getImageFromByteArray(ResourceSerialization.toByteArray(imageData1));
    public static final String className="ck.ckrc.erie.warchess.example1.Life";
    public static final int[] deltaX={1,1,1,0,0,-1,-1,-1};
    public static final int[] deltaY={1,0,-1,1,-1,1,0,-1};
    public static final int liveHP=2,diedHP=1;
    private int nextHp=0,determinedHP=-1;

    public Life(int x, int y, Player player){
        this.x=x;this.y=y;
        this.teamFlag=player.getTeamFlag();
        this.hp=diedHP;
        this.nextHp=diedHP;
    }

    @Override
    public void roundEnd() {
        int cnt=0;
        for(int i=0;i<8;i++){
            var cs=Main.currentGameEngine.getChess(x+deltaX[i],y+deltaY[i]);
            if(cs!=null&&cs.getClass()==this.getClass()&&cs.hp==liveHP)
                cnt=cnt+1;
        }
        nextHp=hp;
        if(hp==liveHP){
            if(cnt<2||cnt>3)nextHp=diedHP;
        } else if (hp==diedHP) {
            if(cnt==3)nextHp=liveHP;
        }
    }

    @Override
    public void roundBegin() {
        if(determinedHP<1)
            hp=nextHp;
        else{
            hp=determinedHP;
            determinedHP=-1;
        }
    }

    @Override
    public Node showPanel() {
        GridPane pane=new GridPane();
        Label title=new Label("细胞");
        Label position=new Label("position:"+'('+x+','+y+')');
        Label team=new Label("team:"+teamFlag);
        Button liveButton=new Button("设为存活");
        liveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                determinedHP=liveHP;
            }
        });
        Button diedButton=new Button("设为死亡");
        diedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                determinedHP=diedHP;
            }
        });
        pane.addRow(0, title);
        pane.addRow(1, position,team);
        pane.addRow(2, liveButton);pane.addRow(2, diedButton);
        return pane;
    }

    public static Node showData(){
        Label title=new Label("细胞");
        title.setPrefWidth(100);
        return title;
    }

    @Override
    public boolean checkEvent(DamageEvent evt) {
        return false;
    }

    @Override
    public boolean checkListener(DamageListener listener) {
        return false;
    }

    @Override
    public Image paint(long delta) {
        return hp==liveHP?image0:image1;
    }

    public static boolean checkPlaceRequirements(Player player,int x,int y){return true;}
}
