import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

public class PongGame extends JPanel implements MouseMotionListener {
    static final int WINDOW_WIDTH = 640, WINDOW_HEIGHT = 480;
    private Ball ball;
    private Paddle userPaddle, pcPaddle;
    private int userMouseY;
    private int userScore, pcScore;
    private int bounceCount;

    public PongGame() {
        this.ball = new Ball(300, 200, 3, 3, 10, 3, Color.white);
        this.userPaddle = new Paddle(10, 200, 75, 3, Color.BLUE);
        this.pcPaddle = new Paddle(610, 200, 75, 3, Color.WHITE);
        
        // initial player mouse position
        userMouseY = 100;

        userScore = 0;
        pcScore = 0;

        bounceCount = 0;

         //listen for motion events on this object
         addMouseMotionListener(this);
    }

    public void gameLogic() {
        ball.moveBall();
        ball.bounceOffEdges(0, WINDOW_HEIGHT);
        userPaddle.moveTowards(userMouseY);
        pcPaddle.moveTowards(ball.getY());

        //check if ball collides with either paddle
        if (pcPaddle.checkCollision(ball) || userPaddle.checkCollision(ball)) {
            //reverse ball if they collide
            ball.reverseX();
            //increase the bounce count
            bounceCount++;
        }

        //after 5 bounces
        if (bounceCount == 5){
            //reset counter
            bounceCount = 0;
            //increase speed will go here
            ball.increaseSpeed();
        }

        //check if someone lost
        if(ball.getX() < 0){
            //player has lost
            pcScore++;
            reset();
        }
        else if(ball.getX() > WINDOW_WIDTH){
            //pc has lost
            userScore++;
            reset();
        }
    }

    public void reset() {
	    //pause for a second
        try{
            Thread.sleep(1000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        //reset ball
        ball.setX(300);
        ball.setY(200);
        ball.setCx(3);
        ball.setCy(3);
        ball.setSpeed(3);
        bounceCount = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        ball.paint(g);
        userPaddle.paint(g);
        pcPaddle.paint(g);

        //update score
        g.setColor(Color.WHITE);
        //the drawString method needs a String to print, and a location to print it at.
        g.drawString("Score - User [ " + userScore + " ]   PC [ " + pcScore + " ]", 250, 20   );
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        userMouseY = e.getY();
    }
}
