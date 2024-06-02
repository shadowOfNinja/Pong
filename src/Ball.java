import java.awt.Color;
import java.awt.Graphics;

public class Ball {
    static final int MAX_SPEED = 7;
    private int x, y, cx, cy, size, speed;
    private Color colour;

    public Ball(int x, int y, int cx, int cy, int size, int speed, Color colour) {
        this.x = x;
        this.y = y;
        this.cx = cx;
        this.cy = cy;
        this.size = size;
        this.speed = speed;
        this.colour = colour;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCx(int cx) {
        this.cx = cx;
    }
    
    public void setCy(int cy) {
        this.cy = cy;
    }
    
    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSize() {
        return this.size;
    }

    public void increaseSpeed(){
        //make sure current speed is less than max speed before incrementing
        if(speed < MAX_SPEED){
            //increase the speed by one
            speed ++;

            //update cy and cx with the new speed
            cx = (cx / Math.abs(cx)*speed);
            cy = (cy / Math.abs(cy)*speed);

        }

    }

    public void paint(Graphics g) {
        g.setColor(colour);
        g.fillOval(x, y, size, size);
    }

    public void moveBall() {
        this.x += cx;
        this.y += cy;
    }

    public void bounceOffEdges(int top, int bottom) {
        if (this.y > bottom - this.size || this.y < top) {
            reverseY();
        }
        
        // if (this.x < 0 || this.x > 640 - this.size)
        // {
        //     reverseX();
        // }
    }

    public void reverseX() {
        this.cx *= -1;
    }

    public void reverseY() {
        this.cy *= -1;
    }
}
