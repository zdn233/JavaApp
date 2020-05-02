import javax.swing.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;




public class Main {

    static int FPS = 30;

    static int VelocityPeriod = 8;

    static int current_X = 0;

    static int position_X = 0;
    static int position_Y = 0;

    static int GameMode = 1; // 1 indicates the game is on, 0 indicates game is over.



    //Main function
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                int argsSize = args.length;
                //If no args are passed in, use the default FPS and speed
                if (argsSize != 0) {
                    FPS = Integer.parseInt(args[0]);
                    if (args[1].equals("1")) {
                        VelocityPeriod = 12;
                    } else if (args[1].equals("2")) {
                        VelocityPeriod = 8;
                    } else if (args[1].equals("3")) {
                        VelocityPeriod = 6;
                    }
                }
                createAndShowGUI();
            }
        });
    }



    public static void createAndShowGUI() {

        // setup the frame
        JFrame f = new JFrame("Animation Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //MVC connection
        Model ballModel = new Model();
        ballModel.FPS = FPS;
        View graphicView = new View(ballModel);
        ballModel.addObserver(graphicView);
        ballModel.notifyObservers();

        //frame properties
        f.add(graphicView);
        f.pack();
        f.setSize(500,500);
        f.setVisible(true);
        f.setResizable(true);



        f.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ballModel.initial_waiting = 0;
                double new_X = f.getSize().width;
                double new_Y = f.getSize().height;
                int X = f.getSize().width;
                int Y = f.getSize().height;

                if (X <= 400 && Y <= 400) {
                    new_X = 400;
                    new_Y = 400;
                    f.setSize(400, 400);
                } else if (X <= 400 && 400 < Y && Y < 800) {
                    new_X = 400;
                    f.setSize(400, Y);
                } else if (X <= 400 && Y >= 800) {
                    new_X = 400;
                    new_Y = 800;
                    f.setSize(400, 800);
                } else if (X > 400 &&  X < 800 && Y <= 400) {
                    new_Y = 400;
                    f.setSize(X, 400);
                } else if (X > 400 &&  X < 800 && 400 < Y && Y < 800) {
                    f.setSize(X, Y);
                } else if (X > 400 &&  X < 800 && Y >= 800) {
                    f.setSize(X, 800);
                } else if (X >= 800 && Y <= 400) {
                    f.setSize(800, 400);
                } else if (X >= 800 && 400 < Y && Y < 800) {
                    f.setSize(800, Y);
                } else if (X >= 800 && Y >= 800) {
                    f.setSize(800, 800);
                }

                graphicView.ratio_X = new_X / 500;
                graphicView.ratio_Y = new_Y / 500;
                graphicView.repaint();

            }
        });



        //Everything related to paddle moving events
        graphicView.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (ballModel.state == 1) {
                    position_X = e.getX();
                    position_Y = e.getY();
                   if (!ballModel.pause_or_not) {
                        ballModel.initial_waiting = 0;
                        ballModel.pause_or_not = true;
                    } else {
                        ballModel.initial_waiting = 100;
                        ballModel.pause_or_not = false;
                    }
                } else if (ballModel.state == -1) {
                    ballModel.state = 1;
                } else if (ballModel.state == 0) {
                    System.exit(0);
                }
            }
        });


        graphicView.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (GameMode == 1) {
                    int X = e.getX();
                    if (X <= 0) {
                        ballModel.paddle_x = 0;
                    } else if (X >= 500 - ballModel.paddle_width) {
                        ballModel.paddle_x = 500 - ballModel.paddle_width;
                    } else {
                        ballModel.paddle_x = e.getX();
                    }
                }
            }
        });


        //Key Bindings
        InputMap ipm;
        ipm = graphicView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap acm;
        acm = graphicView.getActionMap();

        //ESC key
        Action myESCaction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ballModel.state == 1) {
                    ballModel.state = 0;
                } else {
                    System.exit(0);
                }
            }
        };

        //LEFT key
        Action myLEFTaction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ballModel.state == 1) {
                    if (VelocityPeriod == 6) {
                        ballModel.paddle_x -= 70;
                    } else if (VelocityPeriod == 8) {
                        ballModel.paddle_x -= 50;
                    } else if (VelocityPeriod == 12) {
                        ballModel.paddle_x -= 40;
                    }
                    if (ballModel.paddle_x <= 0) {
                        ballModel.paddle_x = 0;
                    }
                }
            }
        };


        //RIGHT key
        Action myRIGHTaction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ballModel.state == 1) {
                    if (VelocityPeriod == 6) {
                        ballModel.paddle_x += 70;
                    } else if (VelocityPeriod == 8) {
                        ballModel.paddle_x += 50;
                    } else if (VelocityPeriod == 12) {
                        ballModel.paddle_x += 40;
                    }
                    if (ballModel.paddle_x >= 500 - ballModel.paddle_width) {
                        ballModel.paddle_x = 500 - ballModel.paddle_width;
                    }
                }
            }
        };

        ipm.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "myESCaction");
        acm.put("myESCaction", myESCaction);
        ipm.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "myLEFTaction");
        acm.put("myLEFTaction", myLEFTaction);
        ipm.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "myRIGHTaction");
        acm.put("myRIGHTaction", myRIGHTaction);




        //Timer for FPS and Velocity

        //Timer to control the ball speed
        Timer timer_forVelocity = new Timer();
        TimerTask task_forVelocity = new TimerTask()  {
            @Override
            public void run() {
                ballModel.handle_animation_Velocity();
                if (ballModel.ball_y >=  500) {
                    GameMode = 0;
                    ballModel.state = 0;
                    timer_forVelocity.cancel();
                }
            }
        };
        timer_forVelocity.schedule(task_forVelocity, 0, VelocityPeriod);

        //Timer for the frame rate
        Timer timer_forFPS = new Timer();
        TimerTask task_forFPS = new TimerTask()  {
            @Override
            public void run() {
                graphicView.handle_animation_FPS();
            }
        };
        timer_forFPS.schedule(task_forFPS, 0, (1000/FPS));
    }
}

