import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;
import java.util.function.ToLongBiFunction;
import java.lang.*;
import javax.swing.JFileChooser.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;


// Drawing shapes
class Shape {
    int oldX, oldY, currentX, currentY;

    int type = 0; // 0 represents Rectangle, 1 represents circle, 2 represents line

    Color defaultcolor = Color.BLUE;

    Color color = defaultcolor;

    Boolean setDash = false;

    float defaultstrokeThickness = 3.0f;

    float BorderThickness = defaultstrokeThickness;

    float scale = 1.0f;



    Shape(Shape s) {
        oldX = s.oldX;
        oldY = s.oldY;
        currentX = s.currentX;
        currentY = s.currentY;
        color = s.color;
        type = s.type;
        setDash = s.setDash;
        BorderThickness = s.BorderThickness;
        scale = s.scale;
    }

    Shape() {
        oldX = 0;
        oldY = 0;
        currentY = 0;
        currentX = 0;
    }



    public void draw(Graphics2D g2) {
        Color borderColor = Color.BLACK;
        g2.setColor(color);

        //determine the shape is selected or not
        if (setDash == false) {
            g2.setStroke(new BasicStroke(BorderThickness / scale));
        } else {
            float[] dash = {5, 5};
            g2.setStroke(new BasicStroke(BorderThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
        }

        //preparation for drawing circle
        int deltaX = Math.abs(currentX - oldX);
        int deltaY = Math.abs(currentY - oldY);
        int l = Math.max(deltaX, deltaY);


        if (!color.equals(Color.white) && !color.equals(Color.black)) {
            //Drawing part
            if (this.type == 0) { //Rectangle
                g2.fillRect(Math.min(oldX, currentX), Math.min(oldY, currentY), Math.abs(currentX - oldX), Math.abs(currentY - oldY));
                g2.setColor(borderColor);
                g2.drawRect(Math.min(oldX, currentX), Math.min(oldY, currentY), Math.abs(currentX - oldX), Math.abs(currentY - oldY));
                g2.setColor(color);
            } else if (this.type == 1) { //Circle
                g2.fillOval(Math.min(oldX, currentX), Math.min(oldY, currentY), l, l);
                g2.setColor(borderColor);
                g2.drawOval(Math.min(oldX, currentX), Math.min(oldY, currentY), l, l);
                g2.setColor(color);
            } else if (this.type == 2) { //Line
                g2.drawLine(oldX, oldY, currentX, currentY);
            }
        } else if (color.equals(Color.black)) {
            g2.drawRect(oldX, oldY, currentX - oldX, currentY - oldY);
        }
    }
}





// Drawing Canvas
class DrawArea extends JComponent {
    //used to store all drawn shapes
    ArrayList<Shape> ShapeList = new ArrayList<Shape>();
    //used for Group Shapes Selection
    ArrayList<Integer> GroupShapesSelectedIndex = new ArrayList<Integer>();
    //used to store the new drawn shape
    Shape newshape;
    //used to store the previous drawn shape
    Shape another;
    //used for visualization of Multi-selection
    Shape multiSelectRect;
    //used for temporary storage
    Shape tempStore;
    //used to tell which mode the DrawArea is currently in.
    // 0 represents drawing mode, 1 represents selection mode, 2 represents filling mode
    // 3 represents erasing mode, 4 represents Group Shapes selection mode
    int mode = 0;
    int previousSelection = -1;
    Color fillcolor = Color.white;


    ShapeObserver shapeob;
    ColorObserver colorob;
    LineThicknessObserver ltob;


    public void changeMultiCoords(Shape s) {
        int newX, newY, newCurX, newCurY;
        if (s.type == 0 || s.type == 2) {
            newX = s.oldX;
            newY = s.oldY;
            newCurX = s.currentX;
            newCurY = s.currentY;
        } else {
            int l = Math.max(Math.abs(s.oldX - s.currentX), Math.abs(s.oldY - s.currentY));
            newX = s.oldX;
            newY = s.oldY;
            newCurX = s.oldX + l;
            newCurY = s.oldY + l;
        }
        if (GroupShapesSelectedIndex.isEmpty() == true) {
            multiSelectRect.oldX = Math.min(newX, newCurX) - 30;
            if (multiSelectRect.oldX < 0) {
                multiSelectRect.oldX = 0;
            }
            multiSelectRect.oldY = Math.min(newY, newCurY) - 30;
            if (multiSelectRect.oldY < 0) {
                multiSelectRect.oldY = 0;
            }
            multiSelectRect.currentX = Math.max(newX, newCurX) + 30;
            multiSelectRect.currentY = Math.max(newY, newCurY) + 30;
        } else {
            int temp = Math.min(newX, newCurX);
            if (temp <= multiSelectRect.oldX) {
                multiSelectRect.oldX = temp - 30;
            }
            if (multiSelectRect.oldX < 0) {
                multiSelectRect.oldX = 0;
            }
            temp = Math.min(newY, newCurY);
            if (temp <= multiSelectRect.oldY) {
                multiSelectRect.oldY = temp - 30;
            }
            if (multiSelectRect.oldY < 0) {
                multiSelectRect.oldY = 0;
            }
            temp = Math.max(newX, newCurX);
            if (temp > multiSelectRect.currentX) {
                multiSelectRect.currentX = temp + 30;
            }
            temp = Math.max(newY, newCurY);
            if (temp > multiSelectRect.currentY) {
                multiSelectRect.currentY = temp + 30;
            }
        }
    }



    public DrawArea() {
        newshape = new Shape();
        another = new Shape();
        multiSelectRect = new Shape();
        multiSelectRect.oldX = -1;
        multiSelectRect.oldY = -1;
        multiSelectRect.currentX = -1;
        multiSelectRect.currentY = -1;
        multiSelectRect.type = 0;
        multiSelectRect.BorderThickness =5.0f;
        multiSelectRect.setDash = true;
        multiSelectRect.color = Color.black;
        tempStore = new Shape();
        tempStore.oldX = -1;
        tempStore.oldY = -1;
        tempStore.currentX = -1;
        tempStore.currentY = -1;

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (mode == 0) {
                    // save coord x,y when mouse is pressed
                    newshape.oldX = e.getX();
                    newshape.oldY = e.getY();
                    another.oldX = e.getX();
                    another.oldY = e.getY();
                } else if (mode == 4) {
                    tempStore.oldX = e.getX();
                    tempStore.oldY = e.getY();
                }
            }
        });



        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (mode == 0) {
                    // coord x,y when drag mouse
                    newshape.currentX = e.getX();
                    newshape.currentY = e.getY();
                    repaint();
                } else if (mode == 4) {
                    tempStore.currentX = e.getX();
                    tempStore.currentY = e.getY();
                    if (tempStore.oldX <= multiSelectRect.currentX - 5 && tempStore.oldX >= multiSelectRect.oldX + 5 &&
                            tempStore.oldY <= multiSelectRect.currentY - 5 && tempStore.oldY >= multiSelectRect.oldY + 5) { //drag action applied
                        int changeofX = tempStore.oldX - tempStore.currentX;
                        int changeofY = tempStore.oldY - tempStore.currentY;
                        multiSelectRect.oldX -= changeofX;
                        multiSelectRect.currentX -= changeofX;
                        multiSelectRect.oldY -= changeofY;
                        multiSelectRect.currentY -= changeofY;
                        for (int i = 0; i < GroupShapesSelectedIndex.size(); ++i) {
                            ShapeList.get(GroupShapesSelectedIndex.get(i)).oldX -= changeofX;
                            ShapeList.get(GroupShapesSelectedIndex.get(i)).currentX -= changeofX;
                            ShapeList.get(GroupShapesSelectedIndex.get(i)).oldY -= changeofY;
                            ShapeList.get(GroupShapesSelectedIndex.get(i)).currentY -= changeofY;
                        }
                        tempStore.oldX = tempStore.currentX;
                        tempStore.oldY = tempStore.currentY;
                        repaint();
                    } else if (tempStore.oldX <= multiSelectRect.oldX + 5 && tempStore.oldX >= multiSelectRect.oldX - 5 &&
                            tempStore.oldY <= multiSelectRect.oldY + 5 && tempStore.oldY >= multiSelectRect.oldY - 5) { //grab top-left corner

                        double oldLength = (multiSelectRect.currentX - multiSelectRect.oldX);
                        double newLength = (multiSelectRect.currentX - tempStore.currentX);
                        double Xratio = oldLength / newLength;

                        double oldHeight = (multiSelectRect.currentY - multiSelectRect.oldY);
                        double newHeight = (multiSelectRect.currentY - tempStore.currentY);
                        double Yratio = oldHeight / newHeight;
                        multiSelectRect.oldX = tempStore.currentX;
                        multiSelectRect.oldY = tempStore.currentY;

                        for (int i = 0; i < GroupShapesSelectedIndex.size(); ++i) {
                            int RightBotX = 0;
                            int RightBotY = 0;
                            int TopLeftX = 0;
                            int TopLeftY = 0;
                            if (ShapeList.get(GroupShapesSelectedIndex.get(i)).type == 0 ||
                                    ShapeList.get(GroupShapesSelectedIndex.get(i)).type == 2) {
                                RightBotX = Math.max(ShapeList.get(GroupShapesSelectedIndex.get(i)).oldX,
                                        ShapeList.get(GroupShapesSelectedIndex.get(i)).currentX);
                                RightBotY = Math.max(ShapeList.get(GroupShapesSelectedIndex.get(i)).oldY,
                                        ShapeList.get(GroupShapesSelectedIndex.get(i)).currentY);
                                TopLeftX = Math.min(ShapeList.get(GroupShapesSelectedIndex.get(i)).oldX,
                                        ShapeList.get(GroupShapesSelectedIndex.get(i)).currentX);
                                TopLeftY = Math.min(ShapeList.get(GroupShapesSelectedIndex.get(i)).oldY,
                                        ShapeList.get(GroupShapesSelectedIndex.get(i)).currentY);
                                double length = RightBotX - TopLeftX;
                                double height = RightBotY - TopLeftY;
                                double resultlength = length / Xratio;
                                double resultheight = height / Yratio;
                                double cornerLength = multiSelectRect.currentX - RightBotX;
                                double cornerHeight = multiSelectRect.currentY - RightBotY;
                                double resultCornerLength = cornerLength / Xratio;
                                double resultCornerHeight = cornerHeight / Yratio;
                                double newRightBotX = multiSelectRect.currentX - resultCornerLength;
                                double newRightBotY = multiSelectRect.currentY - resultCornerHeight;
                                double newTopLeftX = newRightBotX - resultlength;
                                double newTopLeftY = newRightBotY - resultheight;
                                long myX = Math.round(newTopLeftX);
                                long myY = Math.round(newTopLeftY);
                                long myBotX = Math.round(newRightBotX);
                                long myBotY = Math.round(newRightBotY);
                                ShapeList.get(GroupShapesSelectedIndex.get(i)).oldX = Math.toIntExact(myX);
                                ShapeList.get(GroupShapesSelectedIndex.get(i)).oldY = Math.toIntExact(myY);
                                ShapeList.get(GroupShapesSelectedIndex.get(i)).currentX = Math.toIntExact(myBotX);
                                ShapeList.get(GroupShapesSelectedIndex.get(i)).currentY = Math.toIntExact(myBotY);
                            } else {
                                int l = Math.max(Math.abs(ShapeList.get(GroupShapesSelectedIndex.get(i)).currentY -
                                                ShapeList.get(GroupShapesSelectedIndex.get(i)).oldY),
                                        Math.abs(ShapeList.get(GroupShapesSelectedIndex.get(i)).currentX -
                                                ShapeList.get(GroupShapesSelectedIndex.get(i)).oldX));
                                TopLeftX = Math.min(ShapeList.get(GroupShapesSelectedIndex.get(i)).currentX,
                                        ShapeList.get(GroupShapesSelectedIndex.get(i)).oldX);
                                TopLeftY = Math.min(ShapeList.get(GroupShapesSelectedIndex.get(i)).currentY,
                                        ShapeList.get(GroupShapesSelectedIndex.get(i)).oldY);
                                double r = 0.5 * l;
                                RightBotX = TopLeftX + l;
                                RightBotY = TopLeftY + l;
                                double length = l;
                                double height = l;
                                double resultlength = length / Xratio;
                                double resultheight = height / Yratio;
                                double newl;
                                if (resultheight > resultlength) {
                                    newl = resultlength;
                                } else {
                                    newl = resultheight;
                                }

                                double cornerLength = multiSelectRect.currentX - RightBotX;
                                double cornerHeight = multiSelectRect.currentY - RightBotY;
                                double resultCornerLength = cornerLength / Xratio;
                                double resultCornerHeight = cornerHeight / Yratio;

                                double newRightBotX = multiSelectRect.currentX - resultCornerLength;
                                double newRightBotY = multiSelectRect.currentY - resultCornerHeight;
                                double newTopLeftX = newRightBotX - newl;
                                double newTopLeftY = newRightBotY - newl;
                                long myX = Math.round(newTopLeftX);
                                long myY = Math.round(newTopLeftY);
                                long myBotX = Math.round(newRightBotX);
                                long myBotY = Math.round(newRightBotY);
                                ShapeList.get(GroupShapesSelectedIndex.get(i)).oldX = Math.toIntExact(myX);
                                ShapeList.get(GroupShapesSelectedIndex.get(i)).oldY = Math.toIntExact(myY);
                                ShapeList.get(GroupShapesSelectedIndex.get(i)).currentX = Math.toIntExact(myBotX);
                                ShapeList.get(GroupShapesSelectedIndex.get(i)).currentY = Math.toIntExact(myBotY);
                            }
                        }
                        tempStore.oldX = tempStore.currentX;
                        tempStore.oldY = tempStore.currentY;
                        repaint();
                    }
                }
            }
        });


        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                // save coord x,y when mouse is pressed
                if (mode == 0) {
                    another.currentX = e.getX();
                    another.currentY = e.getY();
                    if (!(another.currentX == another.oldX && another.currentY == another.oldY)) {
                        ShapeList.add(new Shape(another));
                    }
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int MyX = e.getX();
                int MyY = e.getY();
                int selected = -1;
                newshape.oldX = -1;
                newshape.currentX = -1;
                newshape.oldY = -1;
                newshape.currentY = -1;
                another.oldX = -1;
                another.currentX = -1;
                another.oldY = -1;
                another.currentY = -1;
                for (int i = ShapeList.size() - 1; i >= 0 ; --i) {
                    if (ShapeList.get(i).type == 0) { //rectangle
                        if (MyX >= Math.min(ShapeList.get(i).oldX, ShapeList.get(i).currentX) &&
                                MyX <= Math.max(ShapeList.get(i).oldX, ShapeList.get(i).currentX) &&
                                MyY >= Math.min(ShapeList.get(i).oldY, ShapeList.get(i).currentY) &&
                                MyY <= Math.max(ShapeList.get(i).oldY, ShapeList.get(i).currentY)) {
                            selected = i;
                            if (mode == 4) {
                                boolean flag = GroupShapesSelectedIndex.contains(selected);
                                if (!flag) {
                                    changeMultiCoords(ShapeList.get(selected));
                                    GroupShapesSelectedIndex.add(selected);
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    } else if (ShapeList.get(i).type == 1) { //circle
                        int l = Math.max(Math.abs(ShapeList.get(i).currentY - ShapeList.get(i).oldY),
                                Math.abs(ShapeList.get(i).currentX - ShapeList.get(i).oldX));
                        double r = 0.5 * l;
                        int TopLeftX = Math.min(ShapeList.get(i).currentX, ShapeList.get(i).oldX);
                        int TopLeftY = Math.min(ShapeList.get(i).currentY, ShapeList.get(i).oldY);
                        double CenterX = TopLeftX + r;
                        double CenterY = TopLeftY + r;
                        double d = Math.sqrt((MyX - CenterX) * (MyX - CenterX) + (MyY - CenterY) * (MyY - CenterY));
                        if (d <= r) {
                            selected = i;
                            if (mode == 4) {
                                boolean flag = GroupShapesSelectedIndex.contains(selected);
                                if (!flag) {
                                    changeMultiCoords(ShapeList.get(selected));
                                    GroupShapesSelectedIndex.add(selected);
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    } else if (ShapeList.get(i).type == 2) { //line
                        double d = Line2D.ptSegDist(ShapeList.get(i).oldX, ShapeList.get(i).oldY,
                                ShapeList.get(i).currentX, ShapeList.get(i).currentY, MyX, MyY);
                        if (0 <= d && d <= 5) {
                            selected = i;
                            if (mode == 4) {
                                boolean flag = GroupShapesSelectedIndex.contains(selected);
                                if (!flag) {
                                    changeMultiCoords(ShapeList.get(selected));
                                    GroupShapesSelectedIndex.add(selected);
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                 }
                if (mode == 1) { //select shape
                    if (selected != -1) {
                        if (previousSelection != -1) {
                            ShapeList.get(previousSelection).setDash = false;
                        }
                        previousSelection = selected;
                        ShapeList.get(selected).setDash = true;
                        shapeob.changeShape(ShapeList.get(selected).type);
                        Color temp = ShapeList.get(selected).color;
                        if (temp.equals(Color.red) || temp.equals(Color.blue) || temp.equals(Color.orange) ||
                                temp.equals(Color.magenta) || temp.equals(Color.green) || temp.equals(Color.pink)) {
                            colorob.changeColor(ShapeList.get(selected).color);
                        } else {
                            colorob.changeColor(Color.white);
                        }
                        ltob.changeLineThickness(ShapeList.get(selected).BorderThickness);
                        repaint();
                    }
                } else if (mode == 2) {
                    if (fillcolor != Color.white) {
                        ShapeList.get(selected).color = fillcolor;
                        repaint();
                    }
                } else if (mode == 3) { // delete shape
                    if (selected != -1) {
                        ShapeList.remove(selected);
                        repaint();
                    }
                } else if (mode == 4) {
                    for (int i = 0; i < GroupShapesSelectedIndex.size(); ++i) {
                        ShapeList.get(GroupShapesSelectedIndex.get(i)).setDash = true;
                    }
                    repaint();
                }
            }
        });
    }


    // custom graphics drawing
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // cast to get 2D drawing methods
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  // antialiasing look nicer
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (ShapeList != null) {
            for(int i = 0; i < ShapeList.size(); ++i) {

                //Printing, used for testing
                //System.out.printf("index: %d, size: %d\n", i, ShapeList.size());
                //System.out.printf("oldx: %d, oldy: %d, currentX: %d, currentY: %d\n", ShapeList.get(i).oldX,ShapeList.get(i).oldY,ShapeList.get(i).currentX,ShapeList.get(i).currentY);
                ShapeList.get(i).draw(g2);
            }
        }
        if (mode == 0) {
            if (newshape.currentX != -1 && newshape.currentY != -1 && newshape.oldX != -1 && newshape.oldY != -1) {
                newshape.draw(g2);
            }
        }
        if (mode == 4) {
            if (multiSelectRect.currentX != -1 && multiSelectRect.currentY != -1 && multiSelectRect.oldX != -1 && multiSelectRect.oldY != -1) {
                multiSelectRect.draw(g2);
            }
        }
    }
}



// Observer interfaces
interface ColorObserver {
    void changeColor(Color c);
}


interface ShapeObserver {
    void changeShape(int index);
}

interface LineThicknessObserver {
    void changeLineThickness(float thickness);
}



//Main program
public class Drawing {

    DrawArea drawArea;

    private class MyButton extends JButton {
        private Color background;

        MyButton(String s, Color bg, ImageIcon ic) {
            super(ic);
            this.setOpaque(true);
            background = bg;
        }

    }

    public class ToolandShape extends JPanel implements ShapeObserver {
        public void changeShape(int index) {
            if (index == 0) { // rectangle
                this.getComponent(4).setBackground(Color.DARK_GRAY);
                this.getComponent(3).setBackground(null);
                this.getComponent(2).setBackground(null);
                this.getComponent(1).setBackground(null);
                this.getComponent(5).setBackground(null);
            } else if (index == 1) { //circle
                this.getComponent(3).setBackground(Color.DARK_GRAY);
                this.getComponent(4).setBackground(null);
                this.getComponent(2).setBackground(null);
                this.getComponent(1).setBackground(null);
                this.getComponent(5).setBackground(null);
            } else if (index == 2) { //line
                this.getComponent(2).setBackground(Color.DARK_GRAY);
                this.getComponent(3).setBackground(null);
                this.getComponent(4).setBackground(null);
                this.getComponent(1).setBackground(null);
                this.getComponent(5).setBackground(null);
            }
        }
    }

    public class ColorPalette extends JPanel implements ColorObserver {
        public void changeColor(Color c) {
            if (c.equals(Color.blue)) {
                drawArea.newshape.color = Color.blue;
                drawArea.another.color = Color.blue;
                drawArea.fillcolor = Color.blue;
                this.getComponent(0).setBackground(Color.blue);
                this.getComponent(1).setBackground(null);
                this.getComponent(2).setBackground(null);
                this.getComponent(3).setBackground(null);
                this.getComponent(4).setBackground(null);
                this.getComponent(5).setBackground(null);
            } else if (c.equals(Color.red)) {
                drawArea.newshape.color = Color.red;
                drawArea.another.color = Color.red;
                drawArea.fillcolor = Color.red;
                this.getComponent(1).setBackground(Color.red);
                this.getComponent(0).setBackground(null);
                this.getComponent(2).setBackground(null);
                this.getComponent(3).setBackground(null);
                this.getComponent(4).setBackground(null);
                this.getComponent(5).setBackground(null);
            } else if (c.equals(Color.orange)) {
                drawArea.newshape.color = Color.orange;
                drawArea.another.color = Color.orange;
                drawArea.fillcolor = Color.orange;
                this.getComponent(2).setBackground(Color.orange);
                this.getComponent(1).setBackground(null);
                this.getComponent(0).setBackground(null);
                this.getComponent(3).setBackground(null);
                this.getComponent(4).setBackground(null);
                this.getComponent(5).setBackground(null);
            } else if (c.equals(Color.magenta)) {
                drawArea.newshape.color = Color.magenta;
                drawArea.another.color = Color.magenta;
                drawArea.fillcolor = Color.magenta;
                this.getComponent(3).setBackground(Color.magenta);
                this.getComponent(1).setBackground(null);
                this.getComponent(2).setBackground(null);
                this.getComponent(0).setBackground(null);
                this.getComponent(4).setBackground(null);
                this.getComponent(5).setBackground(null);
            } else if (c.equals(Color.green)) {
                drawArea.newshape.color = Color.green;
                drawArea.another.color = Color.green;
                drawArea.fillcolor = Color.green;
                this.getComponent(4).setBackground(Color.green);
                this.getComponent(1).setBackground(null);
                this.getComponent(2).setBackground(null);
                this.getComponent(3).setBackground(null);
                this.getComponent(0).setBackground(null);
                this.getComponent(5).setBackground(null);
            } else if (c.equals(Color.pink)) {
                drawArea.newshape.color = Color.pink;
                drawArea.another.color = Color.pink;
                drawArea.fillcolor = Color.pink;
                this.getComponent(5).setBackground(Color.pink);
                this.getComponent(1).setBackground(null);
                this.getComponent(2).setBackground(null);
                this.getComponent(3).setBackground(null);
                this.getComponent(4).setBackground(null);
                this.getComponent(0).setBackground(null);
            } else if (c.equals(Color.white)) {
                drawArea.newshape.color = Color.white;
                drawArea.another.color = Color.white;
                drawArea.fillcolor = Color.white;
                this.getComponent(5).setBackground(null);
                this.getComponent(1).setBackground(null);
                this.getComponent(2).setBackground(null);
                this.getComponent(3).setBackground(null);
                this.getComponent(4).setBackground(null);
                this.getComponent(0).setBackground(null);
            }
        }
    }

    public class LineThicknessPalette extends JPanel implements LineThicknessObserver {
        public void changeLineThickness(float thickness) {
            if (thickness == 3.0f) { // thin
                drawArea.newshape.BorderThickness = 3.0f;
                drawArea.another.BorderThickness = 3.0f;
                this.getComponent(0).setBackground(Color.DARK_GRAY);
                this.getComponent(1).setBackground(null);
                this.getComponent(2).setBackground(null);
            } else if (thickness == 5.0f) { //circle
                drawArea.newshape.BorderThickness = 5.0f;
                drawArea.another.BorderThickness = 5.0f;
                this.getComponent(1).setBackground(Color.DARK_GRAY);
                this.getComponent(0).setBackground(null);
                this.getComponent(2).setBackground(null);
            } else if (thickness == 7.0f) { //line
                drawArea.newshape.BorderThickness = 7.0f;
                drawArea.another.BorderThickness = 7.0f;
                this.getComponent(2).setBackground(Color.DARK_GRAY);
                this.getComponent(1).setBackground(null);
                this.getComponent(0).setBackground(null);
            }
        }
    }

    ToolandShape ToolPalette;

    ColorPalette colorPalette;

    LineThicknessPalette lt;

    MyButton circle, rectangle, line, eraser, cursor, fill; //draw shape buttons

    MyButton blue, red, orange, magenta, green, pink; //color buttons

    MyButton thin, med, thick; //line thickness buttons

    public static void main(String[] args) {
       Drawing myDrawing = new Drawing();
    }


    ActionListener mybuttonactionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (drawArea.mode == 4) {
                drawArea.multiSelectRect.oldX = -1;
                drawArea.multiSelectRect.oldY = -1;
                drawArea.multiSelectRect.currentX = -1;
                drawArea.multiSelectRect.currentY = -1;
                drawArea.tempStore.oldX = -1;
                drawArea.tempStore.oldY = -1;
                drawArea.tempStore.currentX = -1;
                drawArea.tempStore.currentY = -1;
                for (int i = 0; i < drawArea.GroupShapesSelectedIndex.size(); ++i) {
                    drawArea.ShapeList.get(drawArea.GroupShapesSelectedIndex.get(i)).setDash = false;
                }
                drawArea.GroupShapesSelectedIndex.clear();
                drawArea.repaint();
            }
            if (e.getSource() == rectangle) {
                drawArea.newshape.type = 0;
                drawArea.another.type = 0;
                drawArea.mode = 0;
                drawArea.fillcolor = Color.white;
                if (drawArea.previousSelection != -1) {
                    drawArea.ShapeList.get(drawArea.previousSelection).setDash = false;
                    drawArea.previousSelection = -1;
                    drawArea.repaint();
                }
                rectangle.setBackground(rectangle.background);
                circle.setBackground(null);
                line.setBackground(null);
                eraser.setBackground(null);
                cursor.setBackground(null);
                fill.setBackground(null);
            } else if (e.getSource() == circle) {
                drawArea.newshape.type = 1;
                drawArea.another.type = 1;
                drawArea.mode = 0;
                drawArea.fillcolor = Color.white;
                if (drawArea.previousSelection != -1) {
                    drawArea.ShapeList.get(drawArea.previousSelection).setDash = false;
                    drawArea.previousSelection = -1;
                    drawArea.repaint();
                }
                rectangle.setBackground(null);
                circle.setBackground(circle.background);
                line.setBackground(null);
                eraser.setBackground(null);
                cursor.setBackground(null);
                fill.setBackground(null);
            } else if (e.getSource() == line) {
                drawArea.newshape.type = 2;
                drawArea.another.type = 2;
                drawArea.mode = 0;
                drawArea.fillcolor = Color.white;
                if (drawArea.previousSelection != -1) {
                    drawArea.ShapeList.get(drawArea.previousSelection).setDash = false;
                    drawArea.previousSelection = -1;
                    drawArea.repaint();
                }
                rectangle.setBackground(null);
                circle.setBackground(null);
                line.setBackground(line.background);
                eraser.setBackground(null);
                cursor.setBackground(null);
                fill.setBackground(null);
            } else if (e.getSource() == eraser) {
                drawArea.mode = 3;
                drawArea.fillcolor = Color.white;
                if (drawArea.previousSelection != -1) {
                    drawArea.ShapeList.get(drawArea.previousSelection).setDash = false;
                    drawArea.previousSelection = -1;
                    drawArea.repaint();
                }
                rectangle.setBackground(null);
                circle.setBackground(null);
                line.setBackground(null);
                eraser.setBackground(eraser.background);
                cursor.setBackground(null);
                fill.setBackground(null);
            } else if (e.getSource() == cursor) {
                drawArea.mode = 1;
                drawArea.fillcolor = Color.white;
                rectangle.setBackground(null);
                circle.setBackground(null);
                line.setBackground(null);
                eraser.setBackground(null);
                cursor.setBackground(cursor.background);
                fill.setBackground(null);
            } else if (e.getSource() == fill) {
                drawArea.mode = 2;
                if (drawArea.previousSelection != -1) {
                    drawArea.ShapeList.get(drawArea.previousSelection).setDash = false;
                    drawArea.previousSelection = -1;
                    drawArea.repaint();
                }
                rectangle.setBackground(null);
                circle.setBackground(null);
                line.setBackground(null);
                eraser.setBackground(null);
                cursor.setBackground(null);
                fill.setBackground(fill.background);
            }
        }
    };




    ActionListener mycoloractionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (drawArea.mode == 0) {
                if (e.getSource() == red) {
                    drawArea.newshape.color = Color.red;
                    drawArea.another.color = Color.red;
                    drawArea.fillcolor = Color.red;
                    red.setBackground(red.background);
                    blue.setBackground(null);
                    orange.setBackground(null);
                    magenta.setBackground(null);
                    green.setBackground(null);
                    pink.setBackground(null);
                } else if (e.getSource() == blue) {
                    drawArea.newshape.color = Color.blue;
                    drawArea.another.color = Color.blue;
                    drawArea.fillcolor = Color.blue;
                    red.setBackground(null);
                    blue.setBackground(blue.background);
                    orange.setBackground(null);
                    magenta.setBackground(null);
                    green.setBackground(null);
                    pink.setBackground(null);
                } else if (e.getSource() == magenta) {
                    drawArea.newshape.color = Color.magenta;
                    drawArea.another.color = Color.magenta;
                    drawArea.fillcolor = Color.magenta;
                    red.setBackground(null);
                    blue.setBackground(null);
                    orange.setBackground(null);
                    magenta.setBackground(magenta.background);
                    green.setBackground(null);
                    pink.setBackground(null);
                } else if (e.getSource() == orange) {
                    drawArea.newshape.color = Color.orange;
                    drawArea.another.color = Color.orange;
                    drawArea.fillcolor = Color.orange;
                    red.setBackground(null);
                    blue.setBackground(null);
                    orange.setBackground(orange.background);
                    magenta.setBackground(null);
                    green.setBackground(null);
                    pink.setBackground(null);
                } else if (e.getSource() == green) {
                    drawArea.newshape.color = Color.green;
                    drawArea.another.color = Color.green;
                    drawArea.fillcolor = Color.green;
                    red.setBackground(null);
                    blue.setBackground(null);
                    orange.setBackground(null);
                    magenta.setBackground(null);
                    green.setBackground(green.background);
                    pink.setBackground(null);
                } else if (e.getSource() == pink) {
                    drawArea.newshape.color = Color.pink;
                    drawArea.another.color = Color.pink;
                    drawArea.fillcolor = Color.pink;
                    red.setBackground(null);
                    blue.setBackground(null);
                    orange.setBackground(null);
                    magenta.setBackground(null);
                    green.setBackground(null);
                    pink.setBackground(pink.background);
                }
            } else if (drawArea.mode == 1) {
                if (e.getSource() == red) {
                    if (drawArea.previousSelection != -1) {
                        drawArea.ShapeList.get(drawArea.previousSelection).color = Color.red;
                        drawArea.repaint();
                    }
                    drawArea.fillcolor = Color.red;
                    drawArea.newshape.color = Color.red;
                    drawArea.another.color = Color.red;
                    red.setBackground(red.background);
                    blue.setBackground(null);
                    orange.setBackground(null);
                    magenta.setBackground(null);
                    green.setBackground(null);
                    pink.setBackground(null);
                } else if (e.getSource() == green) {
                    if (drawArea.previousSelection != -1) {
                        drawArea.ShapeList.get(drawArea.previousSelection).color = Color.green;
                        drawArea.repaint();
                    }
                    drawArea.fillcolor = Color.green;
                    drawArea.newshape.color = Color.green;
                    drawArea.another.color = Color.green;
                    red.setBackground(null);
                    blue.setBackground(null);
                    orange.setBackground(null);
                    magenta.setBackground(null);
                    green.setBackground(green.background);
                    pink.setBackground(null);
                } else if (e.getSource() == blue) {
                    if (drawArea.previousSelection != -1) {
                        drawArea.ShapeList.get(drawArea.previousSelection).color = Color.blue;
                        drawArea.repaint();
                    }
                    drawArea.fillcolor = Color.blue;
                    drawArea.newshape.color = Color.blue;
                    drawArea.another.color = Color.blue;
                    red.setBackground(null);
                    blue.setBackground(blue.background);
                    orange.setBackground(null);
                    magenta.setBackground(null);
                    green.setBackground(null);
                    pink.setBackground(null);
                } else if (e.getSource() == magenta) {
                    if (drawArea.previousSelection != -1) {
                        drawArea.ShapeList.get(drawArea.previousSelection).color = Color.magenta;
                        drawArea.repaint();
                    }
                    drawArea.fillcolor = Color.magenta;
                    drawArea.newshape.color = Color.magenta;
                    drawArea.another.color = Color.magenta;
                    red.setBackground(null);
                    blue.setBackground(null);
                    orange.setBackground(null);
                    magenta.setBackground(magenta.background);
                    green.setBackground(null);
                    pink.setBackground(null);
                } else if (e.getSource() == orange) {
                    if (drawArea.previousSelection != -1) {
                        drawArea.ShapeList.get(drawArea.previousSelection).color = Color.orange;
                        drawArea.repaint();
                    }
                    drawArea.fillcolor = Color.orange;
                    drawArea.newshape.color = Color.orange;
                    drawArea.another.color = Color.orange;
                    red.setBackground(null);
                    blue.setBackground(null);
                    orange.setBackground(orange.background);
                    magenta.setBackground(null);
                    green.setBackground(null);
                    pink.setBackground(null);
                } else if (e.getSource() == pink) {
                    if (drawArea.previousSelection != -1) {
                        drawArea.ShapeList.get(drawArea.previousSelection).color = Color.pink;
                        drawArea.repaint();
                    }
                    drawArea.fillcolor = Color.pink;
                    drawArea.newshape.color = Color.pink;
                    drawArea.another.color = Color.pink;
                    red.setBackground(null);
                    blue.setBackground(null);
                    orange.setBackground(null);
                    magenta.setBackground(null);
                    green.setBackground(null);
                    pink.setBackground(pink.background);
                }
            } else if (drawArea.mode == 2) {
                if (e.getSource() == red) {
                    drawArea.fillcolor = Color.red;
                    drawArea.newshape.color = Color.red;
                    drawArea.another.color = Color.red;
                    red.setBackground(red.background);
                    blue.setBackground(null);
                    orange.setBackground(null);
                    magenta.setBackground(null);
                    green.setBackground(null);
                    pink.setBackground(null);
                } else if (e.getSource() == blue) {
                    drawArea.fillcolor = Color.blue;
                    drawArea.newshape.color = Color.blue;
                    drawArea.another.color = Color.blue;
                    red.setBackground(null);
                    blue.setBackground(blue.background);
                    orange.setBackground(null);
                    magenta.setBackground(null);
                    green.setBackground(null);
                    pink.setBackground(null);
                } else if (e.getSource() == magenta) {
                    drawArea.fillcolor = Color.magenta;
                    drawArea.newshape.color = Color.magenta;
                    drawArea.another.color = Color.magenta;
                    red.setBackground(null);
                    blue.setBackground(null);
                    orange.setBackground(null);
                    magenta.setBackground(magenta.background);
                    green.setBackground(null);
                    pink.setBackground(null);
                } else if (e.getSource() == orange) {
                    drawArea.fillcolor = Color.orange;
                    drawArea.newshape.color = Color.orange;
                    drawArea.another.color = Color.orange;
                    red.setBackground(null);
                    blue.setBackground(null);
                    orange.setBackground(orange.background);
                    magenta.setBackground(null);
                    green.setBackground(null);
                    pink.setBackground(null);
                } else if (e.getSource() == green) {
                    drawArea.fillcolor = Color.green;
                    drawArea.newshape.color = Color.green;
                    drawArea.another.color = Color.green;
                    red.setBackground(null);
                    blue.setBackground(null);
                    orange.setBackground(null);
                    magenta.setBackground(null);
                    green.setBackground(green.background);
                    pink.setBackground(null);
                } else if (e.getSource() == pink) {
                    drawArea.fillcolor = Color.pink;
                    drawArea.newshape.color = Color.pink;
                    drawArea.another.color = Color.pink;
                    red.setBackground(null);
                    blue.setBackground(null);
                    orange.setBackground(null);
                    magenta.setBackground(null);
                    green.setBackground(null);
                    pink.setBackground(pink.background);
                }
            }
        }
    };



    ActionListener myLineThicknessactionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (drawArea.mode == 0) {
                if (e.getSource() == thin) {
                    drawArea.newshape.BorderThickness = 3.0f;
                    drawArea.another.BorderThickness = 3.0f;
                    thin.setBackground(thin.background);
                    med.setBackground(null);
                    thick.setBackground(null);
                } else if (e.getSource() == med) {
                    drawArea.newshape.BorderThickness = 5.0f;
                    drawArea.another.BorderThickness = 5.0f;
                    thin.setBackground(null);
                    med.setBackground(med.background);
                    thick.setBackground(null);
                } else if (e.getSource() == thick) {
                    drawArea.newshape.BorderThickness = 7.0f;
                    drawArea.another.BorderThickness = 7.0f;
                    thin.setBackground(null);
                    med.setBackground(null);
                    thick.setBackground(thick.background);
                }
            } else if (drawArea.mode == 1) {
                if (e.getSource() == thin) {
                    if (drawArea.previousSelection != -1) {
                        drawArea.ShapeList.get(drawArea.previousSelection).BorderThickness = 3.0f;
                        drawArea.repaint();
                    }
                    drawArea.newshape.BorderThickness = 3.0f;
                    drawArea.another.BorderThickness = 3.0f;
                    thin.setBackground(thin.background);
                    med.setBackground(null);
                    thick.setBackground(null);
                } else if (e.getSource() == med) {
                    if (drawArea.previousSelection != -1) {
                        drawArea.ShapeList.get(drawArea.previousSelection).BorderThickness = 5.0f;
                        drawArea.repaint();
                    }
                    drawArea.newshape.BorderThickness = 5.0f;
                    drawArea.another.BorderThickness = 5.0f;
                    thin.setBackground(null);
                    med.setBackground(med.background);
                    thick.setBackground(null);
                } else if (e.getSource() == thick) {
                    if (drawArea.previousSelection != -1) {
                        drawArea.ShapeList.get(drawArea.previousSelection).BorderThickness = 7.0f;
                        drawArea.repaint();
                    }
                    drawArea.newshape.BorderThickness = 7.0f;
                    drawArea.another.BorderThickness = 7.0f;
                    thin.setBackground(null);
                    med.setBackground(null);
                    thick.setBackground(thick.background);
                }
            }
        }
    };




    public Drawing() {
        JFrame myframe = new JFrame("java vector drawing program");



        drawArea = new DrawArea();
        drawArea.setBorder(BorderFactory.createLineBorder(Color.black));

        InputMap ipm;
        ipm = drawArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap acm;
        acm = drawArea.getActionMap();

        Action myaction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (drawArea.mode == 1) {// select mode
                    if (drawArea.previousSelection != -1) {// there's a shape being selected now
                        drawArea.ShapeList.get(drawArea.previousSelection).setDash = false;
                        drawArea.newshape.color = drawArea.newshape.defaultcolor;
                        drawArea.another.color = drawArea.newshape.defaultcolor;
                        drawArea.newshape.BorderThickness = drawArea.newshape.defaultstrokeThickness;
                        drawArea.newshape.BorderThickness = drawArea.another.defaultstrokeThickness;
                        drawArea.fillcolor = Color.white;
                        red.setBackground(null);
                        blue.setBackground(null);
                        orange.setBackground(null);
                        magenta.setBackground(null);
                        green.setBackground(null);
                        pink.setBackground(null);
                        thin.setBackground(null);
                        med.setBackground(null);
                        thick.setBackground(null);
                        circle.setBackground(null);
                        rectangle.setBackground(null);
                        line.setBackground(null);
                        drawArea.repaint();
                    }
                }
            }
        };

        ipm.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "myaction");
        acm.put("myaction", myaction);



        //create top menu
        JMenu menu = new JMenu("file");
        for (String s: new String[] {"new", "load","save"})
        {
            // add this menu item to the menu
            JMenuItem mi = new JMenuItem(s);
            // set the listener when events occur
            mi.addActionListener(menuItemListener);
            // add this menu item to the menu
            menu.add(mi);
        }
        JMenuBar menubar = new JMenuBar();
        menubar.add(menu);




        //create Tool Palette
        ToolPalette = new ToolandShape();
        ToolPalette.setLayout(new GridLayout(3, 2));
        ToolPalette.setBackground(Color.WHITE);
        ToolPalette.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.BLACK));
        drawArea.shapeob = ToolPalette;
        // create different buttons

        ImageIcon CursorImage = new ImageIcon(new ImageIcon("src/cursor.png").getImage().getScaledInstance(40,40, Image.SCALE_DEFAULT));
        cursor = new MyButton("cursor", Color.darkGray,CursorImage);
        cursor.addActionListener(mybuttonactionListener);
        ImageIcon EraserImage = new ImageIcon(new ImageIcon("src/eraser.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        eraser = new MyButton("eraser", Color.darkGray,EraserImage);
        eraser.addActionListener(mybuttonactionListener);
        ImageIcon LineImage = new ImageIcon(new ImageIcon("src/line.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        line = new MyButton("line", Color.darkGray, LineImage);
        line.addActionListener(mybuttonactionListener);
        ImageIcon CircleImage = new ImageIcon(new ImageIcon("src/circle.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        circle = new MyButton("circle", Color.darkGray, CircleImage);
        circle.addActionListener(mybuttonactionListener);
        ImageIcon RectangleImage = new ImageIcon(new ImageIcon("src/rectangle.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        rectangle = new MyButton("rectangle", Color.darkGray, RectangleImage);
        rectangle.addActionListener(mybuttonactionListener);
        rectangle.setBackground(rectangle.background);
        ImageIcon FillImage = new ImageIcon(new ImageIcon("src/fill.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        fill = new MyButton("fill", Color.darkGray, FillImage);
        fill.addActionListener(mybuttonactionListener);
        //add buttons
        ToolPalette.add(cursor);
        ToolPalette.add(eraser);
        ToolPalette.add(line);
        ToolPalette.add(circle);
        ToolPalette.add(rectangle);
        ToolPalette.add(fill);




        //create color Palette
        colorPalette = new ColorPalette();
        colorPalette.setLayout(new GridLayout(3, 2));
        colorPalette.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.BLACK));
        colorPalette.setBackground(Color.WHITE);
        drawArea.colorob = colorPalette;
        // create different buttons
        ImageIcon Clorblue = new ImageIcon(new ImageIcon("src/blue.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        blue = new MyButton("blue", Color.blue, Clorblue);
        blue.addActionListener(mycoloractionListener);
        blue.setBackground(blue.background);
        ImageIcon Clorred = new ImageIcon(new ImageIcon("src/red.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        red = new MyButton("red", Color.red, Clorred);
        red.addActionListener(mycoloractionListener);
        ImageIcon Clororange = new ImageIcon(new ImageIcon("src/orange.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        orange = new MyButton("orange", Color.orange, Clororange);
        orange.addActionListener(mycoloractionListener);
        ImageIcon Clormagenta = new ImageIcon(new ImageIcon("src/magenta.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        magenta = new MyButton("magenta", Color.MAGENTA, Clormagenta);
        magenta.addActionListener(mycoloractionListener);
        ImageIcon Clorgreen = new ImageIcon(new ImageIcon("src/green.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        green = new MyButton("green",Color.green, Clorgreen);
        green.addActionListener(mycoloractionListener);
        ImageIcon Clorpink = new ImageIcon(new ImageIcon("src/pink.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        pink = new MyButton("pink", Color.pink, Clorpink);
        pink.addActionListener(mycoloractionListener);
        //add buttons
        colorPalette.add(blue);
        colorPalette.add(red);
        colorPalette.add(orange);
        colorPalette.add(magenta);
        colorPalette.add(green);
        colorPalette.add(pink);




        //create Group Shapes select
        JPanel groupSelect = new JPanel();
        groupSelect.setLayout(new GridLayout(1, 1));
        groupSelect.setBorder(BorderFactory.createEtchedBorder());
        groupSelect.setBackground(Color.white);
        groupSelect.setMaximumSize(new Dimension(150,100));
        JButton GroupShapeSelect = new JButton("Multi-select");
        GroupShapeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (drawArea.mode == 1) {
                    if (drawArea.previousSelection != -1) {
                        drawArea.ShapeList.get(drawArea.previousSelection).setDash = false;
                        drawArea.repaint();
                    }
                } else if (drawArea.mode == 4) {
                    drawArea.multiSelectRect.oldX = -1;
                    drawArea.multiSelectRect.oldY = -1;
                    drawArea.multiSelectRect.currentX = -1;
                    drawArea.multiSelectRect.currentY = -1;
                    drawArea.tempStore.oldX = -1;
                    drawArea.tempStore.oldY = -1;
                    drawArea.tempStore.currentX = -1;
                    drawArea.tempStore.currentY = -1;
                    for (int i = 0; i < drawArea.GroupShapesSelectedIndex.size(); ++i) {
                        drawArea.ShapeList.get(drawArea.GroupShapesSelectedIndex.get(i)).setDash = false;
                    }
                    drawArea.GroupShapesSelectedIndex.clear();
                    drawArea.repaint();
                }
                drawArea.mode = 4;
                cursor.setBackground(null);
                eraser.setBackground(null);
                line.setBackground(null);
                circle.setBackground(null);
                rectangle.setBackground(null);
                fill.setBackground(null);
                red.setBackground(null);
                blue.setBackground(null);
                orange.setBackground(null);
                green.setBackground(null);
                pink.setBackground(null);
                magenta.setBackground(null);
                thin.setBackground(null);
                med.setBackground(null);
                thick.setBackground(null);

            }
        });
        groupSelect.add(GroupShapeSelect);




        //create Color chooser
        JPanel Colorchooser = new JPanel();
        Colorchooser.setLayout(new GridLayout(1,1));
        Colorchooser.setBorder(BorderFactory.createEtchedBorder());
        Colorchooser.setBackground(Color.white);
        Colorchooser.setMaximumSize(new Dimension(150, 100));
        JButton chooser = new JButton("chooser");
        chooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (drawArea.mode == 0 || drawArea.mode == 2) {
                    Color c = Color.BLACK;
                    c = JColorChooser.showDialog(null, "choose a color", c);
                    if (c != null) {
                        drawArea.fillcolor = c;
                        drawArea.newshape.color = c;
                        drawArea.another.color = c;
                        red.setBackground(null);
                        blue.setBackground(null);
                        green.setBackground(null);
                        orange.setBackground(null);
                        magenta.setBackground(null);
                        pink.setBackground(null);
                    }
                } else if (drawArea.mode == 1) {
                    Color c = Color.BLACK;
                    c = JColorChooser.showDialog(null, "choose a color", c);
                    if (c != null) {
                        if (drawArea.previousSelection != -1) {
                            drawArea.ShapeList.get(drawArea.previousSelection).color = c;
                            drawArea.repaint();
                            drawArea.fillcolor = c;
                            drawArea.newshape.color = c;
                            drawArea.another.color = c;
                            red.setBackground(null);
                            blue.setBackground(null);
                            green.setBackground(null);
                            orange.setBackground(null);
                            magenta.setBackground(null);
                            pink.setBackground(null);
                        }
                    }
                } else if (drawArea.mode == 3) {

                    JFrame error = new JFrame("ERROR");
                    JTextField ErrorMessage= new JTextField();
                    ErrorMessage.setText("You cannot use the chooser while you are in ERASER mode !");
                    ErrorMessage.setEditable(false);
                    error.add(ErrorMessage);
                    error.setSize(390, 100);
                    error.setVisible(true);
                    error.setResizable(false);
                }
            }
        });
        Colorchooser.add(chooser);




        lt = new LineThicknessPalette();
        lt.setBackground(Color.white);
        lt.setLayout(new GridLayout(3,1));
        lt.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.BLACK));
        drawArea.ltob = lt;
        // create different buttons
        ImageIcon thinImage = new ImageIcon(new ImageIcon("src/thin.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        thin = new MyButton("thin", Color.darkGray, thinImage);
        thin.addActionListener(myLineThicknessactionListener);
        thin.setBackground(thin.background);
        ImageIcon medImage = new ImageIcon(new ImageIcon("src/med.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        med = new MyButton("med", Color.darkGray, medImage);
        med.addActionListener(myLineThicknessactionListener);
        ImageIcon thickImage = new ImageIcon(new ImageIcon("src/thick.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
        thick = new MyButton("thick", Color.darkGray, thickImage);
        thick.addActionListener(myLineThicknessactionListener);
        //add buttons
        lt.add(thin);
        lt.add(med);
        lt.add(thick);





        //create Top Level Container that contains the tool bar
        class TopLevelContainer extends JPanel {
            public TopLevelContainer() {
                this.setBackground(Color.white);
                this.setBorder(BorderFactory.createEtchedBorder());
                this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
                this.add(ToolPalette);
                this.add(Box.createVerticalStrut(10));
                this.add(colorPalette);
                this.add(Box.createVerticalStrut(10));
                this.add(Colorchooser);
                this.add(Box.createVerticalStrut(10));
                this.add(groupSelect);
                this.add(Box.createVerticalStrut(10));
                this.add(lt);
            }
        };




        myframe.setLayout(new BorderLayout());
        myframe.add(menubar, BorderLayout.NORTH);
        myframe.add(new TopLevelContainer(), BorderLayout.WEST);
        myframe.add(drawArea, BorderLayout.CENTER);


        myframe.setSize(1000, 1000);
        myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myframe.setVisible(true);
        myframe.setResizable(true);


    }

    // create a menu item listener
    MenuItemListener menuItemListener = new MenuItemListener();

    class MenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JMenuItem mi = (JMenuItem)e.getSource();
            if (mi.getText() == "new") {
                if (drawArea.mode == 4) {
                    drawArea.multiSelectRect.oldX = -1;
                    drawArea.multiSelectRect.oldY = -1;
                    drawArea.multiSelectRect.currentX = -1;
                    drawArea.multiSelectRect.currentY = -1;
                    drawArea.tempStore.oldX = -1;
                    drawArea.tempStore.oldY = -1;
                    drawArea.tempStore.currentX = -1;
                    drawArea.tempStore.currentY = -1;
                    for (int i = 0; i < drawArea.GroupShapesSelectedIndex.size(); ++i) {
                        drawArea.ShapeList.get(drawArea.GroupShapesSelectedIndex.get(i)).setDash = false;
                    }
                    drawArea.GroupShapesSelectedIndex.clear();
                    drawArea.repaint();
                }
                drawArea.ShapeList.clear();
                drawArea.previousSelection = -1;
                drawArea.fillcolor = Color.white;
                drawArea.mode = 0;
                drawArea.newshape.color = drawArea.newshape.defaultcolor;
                drawArea.another.color = drawArea.newshape.defaultcolor;
                drawArea.newshape.BorderThickness = drawArea.newshape.defaultstrokeThickness;
                drawArea.another.BorderThickness = drawArea.another.defaultstrokeThickness;
                drawArea.newshape.setDash = false;
                drawArea.another.setDash = false;
                drawArea.newshape.type = 0;
                drawArea.another.type = 0;
                drawArea.newshape.oldX = -1;
                drawArea.newshape.oldY = -1;
                drawArea.newshape.currentX = -1;
                drawArea.another.currentY = -1;
                drawArea.another.oldX = -1;
                drawArea.another.oldY = -1;
                drawArea.another.currentX = -1;
                drawArea.another.currentY = -1;
                rectangle.setBackground(rectangle.background);
                circle.setBackground(null);
                line.setBackground(null);
                eraser.setBackground(null);
                cursor.setBackground(null);
                fill.setBackground(null);
                red.setBackground(null);
                blue.setBackground(blue.background);
                orange.setBackground(null);
                magenta.setBackground(null);
                green.setBackground(null);
                pink.setBackground(null);
                thin.setBackground(thin.background);
                med.setBackground(null);
                thick.setBackground(null);
                drawArea.repaint();
            } else if (mi.getText() == "save") {
                String info = "";
                String temp = "";
                int TotalNumber = drawArea.ShapeList.size();
                info = Integer.toString(TotalNumber);
                info += "\n";
                for (int i = 0; i < TotalNumber; ++i) {
                    temp = Integer.toString(drawArea.ShapeList.get(i).type);
                    info = info + temp + "\n";
                    temp = Integer.toString(drawArea.ShapeList.get(i).oldX);
                    info = info + temp + "\n";
                    temp = Integer.toString(drawArea.ShapeList.get(i).oldY);
                    info = info + temp + "\n";
                    temp = Integer.toString(drawArea.ShapeList.get(i).currentX);
                    info = info + temp + "\n";
                    temp = Integer.toString(drawArea.ShapeList.get(i).currentY);
                    info = info + temp + "\n";
                    if (drawArea.ShapeList.get(i).color == Color.red) {
                        temp = "red";
                    } else if (drawArea.ShapeList.get(i).color == Color.blue) {
                        temp = "blue";
                    } else if (drawArea.ShapeList.get(i).color == Color.orange) {
                        temp = "orange";
                    } else if (drawArea.ShapeList.get(i).color == Color.magenta) {
                        temp = "magenta";
                    } else if (drawArea.ShapeList.get(i).color == Color.green) {
                        temp = "green";
                    } else if (drawArea.ShapeList.get(i).color == Color.pink) {
                        temp = "pink";
                    } else {
                        temp = Integer.toString(drawArea.ShapeList.get(i).color.getRGB());
                    }
                    info = info + temp + "\n";
                    if (drawArea.ShapeList.get(i).BorderThickness == 3.0f) {
                        temp = "thin\n";
                        info += temp;
                    } else if (drawArea.ShapeList.get(i).BorderThickness == 5.0f) {
                        temp = "med\n";
                        info += temp;
                    } else if (drawArea.ShapeList.get(i).BorderThickness == 7.0f) {
                        temp = "thick\n";
                        info += temp;
                    }
                }
                JFileChooser fs = new JFileChooser(new File("./src"));
                fs.setDialogTitle("Save a File");
                fs.setFileFilter(new FileNameExtensionFilter("Text File(.txt)",".txt"));
                fs.setApproveButtonText("save");
                int result = fs.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String content = info;
                    File f = fs.getSelectedFile();
                    try {
                        FileWriter fw = new FileWriter(f.getPath());
                        fw.write(content);
                        fw.flush();
                        fw.close();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage());
                    }
                }
            } else if (mi.getText() == "load") {
                if (drawArea.mode == 4) {
                    drawArea.multiSelectRect.oldX = -1;
                    drawArea.multiSelectRect.oldY = -1;
                    drawArea.multiSelectRect.currentX = -1;
                    drawArea.multiSelectRect.currentY = -1;
                    drawArea.tempStore.oldX = -1;
                    drawArea.tempStore.oldY = -1;
                    drawArea.tempStore.currentX = -1;
                    drawArea.tempStore.currentY = -1;
                    for (int i = 0; i < drawArea.GroupShapesSelectedIndex.size(); ++i) {
                        drawArea.ShapeList.get(drawArea.GroupShapesSelectedIndex.get(i)).setDash = false;
                    }
                    drawArea.GroupShapesSelectedIndex.clear();
                    drawArea.repaint();
                }
                drawArea.ShapeList.clear();
                drawArea.previousSelection = -1;
                drawArea.fillcolor = Color.white;
                drawArea.mode = 0;
                drawArea.newshape.color = drawArea.newshape.defaultcolor;
                drawArea.another.color = drawArea.newshape.defaultcolor;
                drawArea.newshape.BorderThickness = drawArea.newshape.defaultstrokeThickness;
                drawArea.another.BorderThickness = drawArea.another.defaultstrokeThickness;
                drawArea.newshape.setDash = false;
                drawArea.another.setDash = false;
                drawArea.newshape.type = 0;
                drawArea.another.type = 0;
                drawArea.newshape.oldX = -1;
                drawArea.newshape.oldY = -1;
                drawArea.newshape.currentX = -1;
                drawArea.another.currentY = -1;
                drawArea.another.oldX = -1;
                drawArea.another.oldY = -1;
                drawArea.another.currentX = -1;
                drawArea.another.currentY = -1;
                rectangle.setBackground(rectangle.background);
                circle.setBackground(null);
                line.setBackground(null);
                eraser.setBackground(null);
                cursor.setBackground(null);
                fill.setBackground(null);
                red.setBackground(null);
                //blue.setBackground(blue.background);
                blue.setBackground(blue.background);
                orange.setBackground(null);
                magenta.setBackground(null);
                green.setBackground(null);
                pink.setBackground(null);
                thin.setBackground(thin.background);
                med.setBackground(null);
                thick.setBackground(null);
                JFileChooser fs = new JFileChooser(new File("./src"));
                fs.setDialogTitle("Load a File");
                fs.setFileFilter(new FileNameExtensionFilter("Text File(.txt)",".txt"));
                fs.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fs.showDialog(null, "load");
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        File f = fs.getSelectedFile();
                        BufferedReader br = new BufferedReader(new FileReader(f.getPath()));
                        String readin = "";
                        int size = 0;
                        int temp = 0;
                        readin = br.readLine();
                        size = Integer.parseInt(readin);
                        for (int i = 0; i < size; ++i) {
                            readin = br.readLine();
                            temp = Integer.parseInt(readin);
                            drawArea.newshape.type = temp;
                            readin = br.readLine();
                            temp = Integer.parseInt(readin);
                            drawArea.newshape.oldX = temp;
                            readin = br.readLine();
                            temp = Integer.parseInt(readin);
                            drawArea.newshape.oldY = temp;
                            readin = br.readLine();
                            temp = Integer.parseInt(readin);
                            drawArea.newshape.currentX = temp;
                            readin = br.readLine();
                            temp = Integer.parseInt(readin);
                            drawArea.newshape.currentY = temp;
                            readin = br.readLine();
                            if (readin.equals("red")) {
                                drawArea.newshape.color = Color.red;
                            } else if (readin.equals("blue")) {
                                drawArea.newshape.color = Color.blue;
                            } else if (readin.equals("green")) {
                                drawArea.newshape.color = Color.green;
                            } else if (readin.equals("orange")) {
                                drawArea.newshape.color = Color.orange;
                            } else if (readin.equals("magenta")) {
                                drawArea.newshape.color = Color.magenta;
                            } else if (readin.equals("pink")) {
                                drawArea.newshape.color = Color.pink;
                            } else {
                                Color c = new Color(Integer.parseInt(readin));
                                drawArea.newshape.color = c;
                            }
                            readin = br.readLine();
                            float currentThickness = 3.0f;
                            if (readin.equals("thin")) {
                                currentThickness = 3.0f;
                            } else if (readin.equals("med")) {
                                currentThickness = 5.0f;
                            } else if (readin.equals("thick")) {
                                currentThickness = 7.0f;
                            }
                            drawArea.newshape.BorderThickness = currentThickness;
                            drawArea.ShapeList.add(new Shape(drawArea.newshape));
                        }
                        drawArea.repaint();
                    } catch (Exception e2){
                        JOptionPane.showMessageDialog(null, e2.getMessage());
                    }
                }
            }
        }
    }
}
