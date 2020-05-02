import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;


public class Model extends Observable {

    //data related to the game
    int FPS = 30;
    int Score = 0;
    int state = -1; // -1 represents initial state (i.e. slash scree); 1 represents game playing state
    int initial_waiting = 0;
    boolean pause_or_not = false;

    //ball related data;
    int ball_x, ball_y;
    int dx = 2;
    int dy = 2;
    int ball_width = 20;

    //paddle related data;
    int paddle_x = 200;
    int paddle_y = 440;
    int paddle_height = 20;
    int paddle_width = 100;

    //block related data
    ArrayList<block> blocks;



    Model() {
        //generate random coordinate for the initial position of the ball
        Random rand = new Random();
        ball_x = (rand.nextInt(400) + 50);
        ball_y = (rand.nextInt(50) + 250);

        //initialization of blocks in the model
        blocks = new ArrayList<block>();
        for (int r = 0; r < 5; ++r) {
            for (int c = 0; c < 5; ++c) {
                block myblock = new block();
                myblock.row = r;
                myblock.col = c;
                myblock.topLeftX = 50 + c * myblock.width;
                myblock.topLeftY = 50 + r * myblock.height;
                blocks.add(myblock);
            }
        }
        setChanged();
    }

    //Functions used to get the ball related data
    public int getBallx() {
        return ball_x;
    }

    public int getBally() {
        return ball_y;
    }

    public int getBallwidth() {
        return ball_width;
    }

    public void addScore(int s) {
        Score += s;
    }

    public void checkSpecial(int i) {
        if (i == 12) {
            paddle_width = 200;
        }
    }

    //handle animation for ball speed
    public void handle_animation_Velocity() {

        if (state == 1 && initial_waiting < 99) {
            initial_waiting += 1;
        } else if (state == 1 && initial_waiting == 99 && pause_or_not) {
            initial_waiting = 0;
        } else if (state == 1 && initial_waiting == 99 && !pause_or_not) {
            initial_waiting += 1;
        }else if (state == 1 && initial_waiting == 100) {

            // if we hit the edge of the window, change direction
            if (ball_x < 0 || ball_x > (500 - ball_width)) {
                dx *= -1;
            }
            if (ball_y < 0) {
                dy *= -1;
            }
            if (((ball_y >= (paddle_y - ball_width) && ball_y <= (paddle_y - ball_width + 2))
                            && (ball_x >= (paddle_x - ball_width - 3) && ball_x <= (paddle_x + paddle_width + 3)))) {
                dy *= -1;
                addScore(5);
            } else if ((((ball_x + ball_width) >= paddle_x && (ball_x + ball_width) <= paddle_x + 2)
                        || (ball_x <= paddle_x + paddle_width && ball_x >= paddle_x + paddle_width - 2))
                        && ball_y <= paddle_y - 2 && ball_y + ball_width >= paddle_y + paddle_height + 2) {
                dx *= -1;
                addScore(5);
            }

            for (int i = 0; i < 25; ++i) {
                if (blocks.get(i).exist_or_not == true) {
                    int TLX = blocks.get(i).topLeftX;
                    int TLY = blocks.get(i).topLeftY;
                    int w = blocks.get(i).width;
                    int h = blocks.get(i).height;
                    if ((((ball_x + ball_width) >= TLX && (ball_x + ball_width) <= TLX + 4)
                            || (ball_x <= (TLX + w) && ball_x >= (TLX + w - 4)))
                            && (ball_y >= (TLY - ball_width) && ball_y <= (TLY + h))) {
                        dx *= -1;
                        addScore(10);
                        checkSpecial(i);
                        blocks.get(i).exist_or_not = false;
                        break;
                    }
                    if (((ball_y >= (TLY - ball_width) && ball_y <= (TLY - ball_width + 4))
                            || (ball_y <= (TLY + h) && ball_y >= (TLY + h - 4)))
                            && (ball_x >= (TLX - ball_width) && ball_x <= (TLX + w))) {
                        dy *= -1;
                        addScore(10);
                        checkSpecial(i);
                        blocks.get(i).exist_or_not = false;
                        break;
                    }
                }
            }
            // move the ball
            ball_x += dx;
            ball_y += dy;

            boolean is_all_empty = true;
            for (int i = 0; i < 25; ++i) {
                if (blocks.get(i).exist_or_not == true) {
                    is_all_empty = false;
                }
            }
            if (is_all_empty) {
                paddle_width = 100;
            }
            if (is_all_empty && ball_y >= 250) {
                for (int i = 0; i < 25; ++i) {
                    blocks.get(i).exist_or_not = true;
                }
            }
            setChanged();
            notifyObservers();
        }
    }

}
