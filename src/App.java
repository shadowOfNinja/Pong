import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//See guide on: https://kevinsguides.com/guides/code/java/javaprojs/simple-2d-pong

public class App {
    //declare and initialize the frame
    static JFrame f = new JFrame("Pong");

    public static void main(String[] args) {

		//make it so program exits on close button click
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//the size of the game will be 480x640, the size of the JFrame needs to be slightly larger
        f.setSize(650,515);

		//show the window
        f.setVisible(true);

		//make the new PongGame
        PongGame game = new PongGame();

        //add the game to the JFrame
        f.add(game);

        //show the window
        f.setVisible(true);

        //make a new Timer
        Timer timer = new Timer(33, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                game.gameLogic();
                
                //repaint the screen
                game.repaint();

            }
        });

        //start the timer after it's been created
        timer.start();
	}
}
