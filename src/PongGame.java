import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

public class PongGame extends JPanel implements MouseMotionListener {
    static final int WINDOW_WIDTH = 640, WINDOW_HEIGHT = 480;
    static final int baseSpeed = 5;
    static final int basePaddleHeight = 75;
    private Ball ball;
    private Paddle userPaddle, pcPaddle;
    private int userMouseY;
    private int userScore, pcScore;
    private int bounceCount;
    private int impactX, impactY;
    private int predictedX, predictedY;
    private int scale;

    public PongGame() {
        this.ball = new Ball(300, 200, 3, 3, 10, baseSpeed, Color.white);
        this.userPaddle = new Paddle(10, 200, basePaddleHeight, baseSpeed, Color.BLUE);
        this.pcPaddle = new Paddle(610, 200, basePaddleHeight, baseSpeed, Color.WHITE);
        
        // initial player mouse position
        userMouseY = 100;

        userScore = 0;
        pcScore = 0;

        bounceCount = 0;

         //listen for motion events on this object
         addMouseMotionListener(this);
    }

    // TO DO: 
    // 1) add a new AI function that can predict the final path of the ball
    public void gameLogic() {
        ball.moveBall();
        ball.bounceOffEdges(0, WINDOW_HEIGHT);
        userPaddle.moveTowards(userMouseY);
        
        // new AI function that will predict the path of the ball
        pcPaddle.moveTowards(predictBallPath());
        
        // basic AI, PC moves to current height of the ball
        //pcPaddle.moveTowards(ball.getY());

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
            reset(2);
        }
        else if(ball.getX() > WINDOW_WIDTH){
            //pc has lost
            userScore++;
            reset(1);
        }
    }

    public void reset(int player) {
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

        //serve the ball depending on who won that rally
        if (player == 1) {
            ball.setCx(3);
        }
        else {
            ball.setCx(-3);
        }
        //randomly serve the ball up or down
        if (Math.random() <= .5) {
            ball.setCy(3);    
        }
        else {
            ball.setCy(-3);    
        }
        ball.setSpeed(3);
        bounceCount = 0;

        //set difficulty scale
        if (pcScore > userScore) {
            scale = (WINDOW_WIDTH / 100) * (pcScore - userScore);
            //System.out.println("PC ahead, calc scale as: " + WINDOW_WIDTH + " / 100 * " + pcScore + " - " + userScore + " = " + scale);
        } 
        else if (userScore > pcScore) {
            scale = ((WINDOW_WIDTH / 100) * (userScore - pcScore)) * -1;
            //System.out.println("Player ahead, calc scale as: " + WINDOW_WIDTH + " / 100 * " + userScore + " - " + pcScore + " x -1 = " + scale);
        }
        else {
            scale = 0;
        }

        if ((WINDOW_WIDTH / 2) - scale < 0) {
            scale = 10;
        } 
        else if ((WINDOW_WIDTH / 2) + scale > WINDOW_WIDTH) {
            scale = WINDOW_WIDTH - 10;
        }
        //System.out.println("Scale is: " + scale + ". Prediction starts at: " + ((WINDOW_WIDTH / 2) + scale));
    }

    public int predictBallPath() {
        int ballCurrentX, ballCurrentY, ballCurrentCx, ballCurrentCy;
        // when the ball's cx is positive (moving towards the pc player), predict the path of the ball
        // when the ball's cx is negative, set the position to the y co-ordinate of the ball
        // Also have the paddle track the current hight a set distance away. Scale with difficulty

        if (ball.getCx() < 0 || ball.getX() < (WINDOW_WIDTH / 2) + scale) {
            return ball.getY();
        }
        else 
        {
            // get the ball's current location and direction
            ballCurrentX = ball.getX();
            ballCurrentY = ball.getY();
            ballCurrentCx = ball.getCx();
            ballCurrentCy = ball.getCy();

            predictedX = ballCurrentX;
            predictedY = ballCurrentY;

            impactX = predictedX;
            impactY = predictedY;

            while (predictedX < pcPaddle.getX()) {
                predictedX += ballCurrentCx;
                if (predictedY > WINDOW_HEIGHT - ball.getSize() || predictedY < 0) {
                    impactX = predictedX;
                    impactY = predictedY;
                    ballCurrentCy *= -1;
                }
                predictedY += ballCurrentCy;
            }
            //System.out.println("Predicting ball with go to : " + predictedX + ", " + predictedY);
            return predictedY;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        ball.paint(g);
        userPaddle.paint(g);
        pcPaddle.paint(g);

        g.setColor(Color.WHITE);
        g.drawLine(WINDOW_WIDTH / 2, 0, WINDOW_WIDTH / 2, WINDOW_HEIGHT);

        //update score
        g.setColor(Color.WHITE);
        //the drawString method needs a String to print, and a location to print it at.
        g.drawString("Score - User [ " + userScore + " ]   PC [ " + pcScore + " ]", 250, 20   );

        // debug lines showing predictions
        /*if (ball.getCx() > 0) {
            // straightest path between the ball and PC's paddle
            g.setColor(Color.green);
            g.drawLine(ball.getX(), ball.getY(), pcPaddle.getX(), pcPaddle.getCenterY());
            
            if (ball.getX() > ((WINDOW_WIDTH/2) + scale)) {
                // AI's predicted path
                g.setColor(Color.red);
                            
                g.drawLine(userPaddle.getX(), ball.getImpactY(), impactX, impactY);
                g.drawLine(impactX, impactY, predictedX, predictedY);

                g.drawOval(predictedX, predictedY, ball.getSize(), ball.getSize());
            }
        }

        // debug line showing where predictions start
        g.setColor(Color.orange);
        g.drawLine(((WINDOW_WIDTH/2) + scale),0,((WINDOW_WIDTH/2) + scale),WINDOW_HEIGHT);
        */
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
