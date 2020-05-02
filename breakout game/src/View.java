import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import javax.swing.JPanel;


public class View extends JPanel implements Observer {
    //ball, paddle and blocks
    Model model;

    //Strings used for painting
    String label_Current_Score = "Score: ";
    String label_FPS = "FPS: ";

    //Used for window resize event
    double ratio_X = 1.0;
    double ratio_Y = 1.0;


    View (Model model) {
        this.model = model;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  // antialiasing look nicer
                RenderingHints.VALUE_ANTIALIAS_ON);

        //Set Transformation
        AffineTransform old = g2.getTransform();
        AffineTransform newAT = new AffineTransform();
        newAT.scale(ratio_X, ratio_Y);
        g2.setTransform(newAT);

        if (model.state == 1) {

            g2.setColor(Color.black);
            g2.fillRect(0,0,500,500);
            //draw strings(include scores and fps)
            g2.setColor(Color.white);
            label_Current_Score = "Score: " + Integer.toString(model.Score);
            g2.drawString(label_Current_Score, 10, 20);
            label_FPS = "FPS: " + Integer.toString(model.FPS);
            g2.drawString(label_FPS, 10, 40);

            //draw ball
            g2.setColor(Color.red);
            g2.fillOval(model.ball_x, model.ball_y, model.ball_width, model.ball_width);
            g2.setColor(Color.white);
            g2.drawOval(model.ball_x, model.ball_y, model.ball_width, model.ball_width);

            //draw paddle
            g2.setColor(Color.pink);
            g2.fillRect(model.paddle_x, model.paddle_y, model.paddle_width, model.paddle_height);
            g2.setColor(Color.white);
            g2.drawRect(model.paddle_x, model.paddle_y, model.paddle_width, model.paddle_height);


            //draw blocks
            for (int i = 0; i < 5; ++i) {
                if (model.blocks.get(i).exist_or_not == true) {
                    g2.setColor(Color.green);
                    g2.fillRect(model.blocks.get(i).topLeftX, model.blocks.get(i).topLeftY, model.blocks.get(i).width, model.blocks.get(i).height);
                    g2.setColor(Color.white);
                    g2.drawRect(model.blocks.get(i).topLeftX, model.blocks.get(i).topLeftY, model.blocks.get(i).width, model.blocks.get(i).height);
                }
            }
            for (int i = 5; i < 10; ++i) {
                if (model.blocks.get(i).exist_or_not == true) {
                    g2.setColor(Color.yellow);
                    g2.fillRect(model.blocks.get(i).topLeftX, model.blocks.get(i).topLeftY, model.blocks.get(i).width, model.blocks.get(i).height);
                    g2.setColor(Color.white);
                    g2.drawRect(model.blocks.get(i).topLeftX, model.blocks.get(i).topLeftY, model.blocks.get(i).width, model.blocks.get(i).height);
                }
            }
            for (int i = 10; i < 15; ++i) {
                if (model.blocks.get(i).exist_or_not == true) {
                    if (i != 12) {
                        g2.setColor(Color.blue);
                        g2.fillRect(model.blocks.get(i).topLeftX, model.blocks.get(i).topLeftY, model.blocks.get(i).width, model.blocks.get(i).height);
                        g2.setColor(Color.white);
                        g2.drawRect(model.blocks.get(i).topLeftX, model.blocks.get(i).topLeftY, model.blocks.get(i).width, model.blocks.get(i).height);
                    } else {
                        g2.setColor(Color.cyan);
                        g2.fillRect(model.blocks.get(i).topLeftX, model.blocks.get(i).topLeftY, model.blocks.get(i).width, model.blocks.get(i).height);
                        g2.setColor(Color.white);
                        g2.drawRect(model.blocks.get(i).topLeftX, model.blocks.get(i).topLeftY, model.blocks.get(i).width, model.blocks.get(i).height);
                    }
                }
            }
            for (int i = 15; i < 20; ++i) {
                if (model.blocks.get(i).exist_or_not == true) {
                    g2.setColor(Color.pink);
                    g2.fillRect(model.blocks.get(i).topLeftX, model.blocks.get(i).topLeftY, model.blocks.get(i).width, model.blocks.get(i).height);
                    g2.setColor(Color.white);
                    g2.drawRect(model.blocks.get(i).topLeftX, model.blocks.get(i).topLeftY, model.blocks.get(i).width, model.blocks.get(i).height);
                }
            }
            for (int i = 20; i < 25; ++i) {
                if (model.blocks.get(i).exist_or_not == true) {
                    g2.setColor(Color.magenta);
                    g2.fillRect(model.blocks.get(i).topLeftX, model.blocks.get(i).topLeftY, model.blocks.get(i).width, model.blocks.get(i).height);
                    g2.setColor(Color.white);
                    g2.drawRect(model.blocks.get(i).topLeftX, model.blocks.get(i).topLeftY, model.blocks.get(i).width, model.blocks.get(i).height);
                }
            }
        } else if (model.state == -1) {
            g2.setColor(Color.black);
            g2.fillRect(0,0,500,500);
            g2.setColor(Color.white);
            Font oldFont = g2.getFont();
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            g2.drawString("Student ID: 20729582", 30, 40);
            g2.drawString("Name: Dainan Zhang", 30, 70);
            g2.setFont(oldFont);
            g2.drawString("Game Description:", 30, 130);
            g2.drawString("1: You can use either Mouse or the LEFT/RIGHT key to control the paddle", 30, 160);
            g2.drawString("2: ESC key allows you to quit the game immediately", 30, 190);
            g2.drawString("3. 5 marks are awarded for each bounce on paddle", 30, 220);
            g2.drawString("4: 10 marks are awarded for hitting a block", 30, 250);
            g2.drawString("5: When all the blocks are cleared, a new 5x5 blocks will be generated", 30, 280);
            g2.drawString("6: When the ball goes to the bottom of the window, the game is over", 30, 310);
            g2.drawString("7: You can always resize the window, the limit is 400x400 to 800x800", 30, 340);
            g2.drawString("8. To start the game, just press the START below", 30, 370);
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            g2.drawString("START", 200, 420);
        } else if (model.state == 0) {
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 50));
            g2.setColor(Color.black);
            g2.fillRect(0,0,500,500);
            g2.setColor(Color.white);
            g2.drawString("Game Over", 120, 160);
            g2.drawString("Total Score: ", 120, 220);
            g2.drawString(Integer.toString(model.Score), 220, 280);
            g2.drawString("EXIT", 190, 400);
        }
        //Set transformation back
        g2.setTransform(old);
    }

    public void handle_animation_FPS() {
        repaint();
    }


    @Override
    public void update(Observable arg0, Object arg1) {
    }
}
