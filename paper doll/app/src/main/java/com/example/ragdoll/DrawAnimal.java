package com.example.ragdoll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.ScaleGestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;

public class DrawAnimal extends View{

    DrawPart torso = null;
    String PartSelected = null;
    Point DragInitialPosition;
    float oldX = 0;
    float oldY = 0;

    DrawAnimal(Context context) {
        super(context);
        setOnTouchListener(new myOwnTouchSnakeListener());
        build();
    }

    public void reset() {
        torso = null;
        PartSelected = null;
        DragInitialPosition = null;
        oldX = 0;
        oldY = 0;
        build();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        torso.draw(canvas);
    }

    // Used to calculate the angle rotated
    double getAngle(float prevX, float prevY, float curX, float curY, float pivotX, float pivotY) {
        double prevDiffY = prevY - pivotY;
        double prevDiffX = prevX - pivotX;
        double curDiffY = curY - pivotY;
        double curDiffX = curX - pivotX;
        return Math.atan2(curDiffY, curDiffX) - Math.atan2(prevDiffY, prevDiffX);
    }

    class myOwnTouchSnakeListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                DragInitialPosition = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                PartSelected = torso.whichIsChosen(DragInitialPosition);
                oldX = motionEvent.getX();
                oldY = motionEvent.getY();
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                if (PartSelected != null) {
                    if (PartSelected.equals("torso")) {
                        torso.MoveTorso(motionEvent.getX() - oldX, motionEvent.getY() - oldY);
                        oldX = motionEvent.getX();
                        oldY = motionEvent.getY();
                        invalidate();
                    } else if (PartSelected.equals("Butt")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 0;
                        originPivot[1] = 40;
                        torso.children.get(1).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180/Math.PI);
                        float realAngle = (float) angle;
                        float TotalAngle = torso.children.get(1).deltaDegree + realAngle;
                        if (TotalAngle > 360 || TotalAngle < -360) {
                            TotalAngle %= 360;
                        }
                        if (TotalAngle <= 360 && TotalAngle >= -360) {
                            if (TotalAngle <=0) {
                                TotalAngle += 360;
                            }
                        }
                        RectF testInorNot = new RectF();
                        torso.children.get(1).matrix.mapRect(testInorNot, torso.children.get(1).rectF);
                        if (((TotalAngle >= 0 && TotalAngle <= 30) || (TotalAngle >= 330 && TotalAngle <=360))
                                && testInorNot.contains(motionEvent.getX(), motionEvent.getY())) {
                            torso.children.get(1).matrix.preRotate(realAngle, 0,40);
                            torso.children.get(1).deltaDegree += realAngle;
                            oldX = motionEvent.getX();
                            oldY = motionEvent.getY();
                            invalidate();
                        }
                    } else if (PartSelected.equals("Neck")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 30;
                        originPivot[1] = 400;
                        torso.children.get(0).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180/Math.PI);
                        float realAngle = (float) angle;
                        float TotalAngle = torso.children.get(0).deltaDegree + realAngle;
                        if (TotalAngle > 360 || TotalAngle < -360) {
                            TotalAngle %= 360;
                        }
                        if (TotalAngle <= 360 && TotalAngle >= -360) {
                            if (TotalAngle <=0) {
                                TotalAngle += 360;
                            }
                        }
                        RectF testInorNot = new RectF();
                        torso.children.get(0).matrix.mapRect(testInorNot, torso.children.get(0).rectF);
                        if (((TotalAngle >= 0 && TotalAngle <= 45) || (TotalAngle >= 315 && TotalAngle <=360))
                                && testInorNot.contains(motionEvent.getX(), motionEvent.getY())) {
                            torso.children.get(0).matrix.preRotate(realAngle, 30,400);
                            torso.children.get(0).deltaDegree += realAngle;
                            torso.children.get(0).children.get(0).matrix.set(torso.children.get(0).matrix);
                            torso.children.get(0).children.get(0).matrix.preTranslate(0, -200);
                            torso.children.get(0).children.get(0).matrix.preRotate(-90, 30, 200);
                            torso.children.get(0).children.get(0).matrix.preRotate(torso.children.get(0).children.get(0).deltaDegree, 30, 200);
                            oldX = motionEvent.getX();
                            oldY = motionEvent.getY();
                            invalidate();
                        }
                    } else if (PartSelected.equals("Head")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 30;
                        originPivot[1] = 200;
                        torso.children.get(0).children.get(0).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180/Math.PI);
                        float realAngle = (float) angle;
                        float TotalAngle = torso.children.get(0).children.get(0).deltaDegree + realAngle;
                        if (TotalAngle > 360 || TotalAngle < -360) {
                            TotalAngle %= 360;
                        }
                        if (TotalAngle <= 360 && TotalAngle >= -360) {
                            if (TotalAngle <=0) {
                                TotalAngle += 360;
                            }
                        }
                        RectF testInorNot = new RectF();
                        torso.children.get(0).children.get(0).matrix.mapRect(testInorNot,
                                torso.children.get(0).children.get(0).rectF);
                        if (((TotalAngle >= 0 && TotalAngle <= 45) || (TotalAngle >= 315 && TotalAngle <=360))
                                && testInorNot.contains(motionEvent.getX(), motionEvent.getY())) {
                            torso.children.get(0).children.get(0).matrix.preRotate(realAngle, 30,200);
                            torso.children.get(0).children.get(0).deltaDegree += realAngle;
                            oldX = motionEvent.getX();
                            oldY = motionEvent.getY();
                            invalidate();
                        }
                    }
                }
                return true;
            }
            return true;
        }
    }

    void build() {
        torso = new DrawPart(new RectF(0, 0, 800, 80), "torso");
        torso.translate(600,800);

        //Snake Neck
        DrawPart Neck = new DrawPart(new RectF(0, 0, 60, 400), "Neck");
        Neck.translate(570, 400);
        Neck.deltaDegree = 0;
        Neck.MaxDegree = 45;
        torso.addchild(Neck);
        //Snake Head
        DrawPart Head = new DrawPart(new RectF(0, 0, 60, 200), "Head");
        Head.matrix.set(Neck.matrix);
        Head.matrix.preTranslate(0, -200);
        Head.matrix.preRotate(-90, 30, 200);
        Head.deltaDegree = 0;
        Head.MaxDegree = 45;
        Neck.addchild(Head);

        //Snake Butt
        DrawPart Butt = new DrawPart(new RectF(0,0,350, 80), "Butt");
        Butt.translate(1400, 800 );
        Butt.matrix.preRotate(-45, 0, 40);
        Butt.deltaDegree = 0;
        Butt.MaxDegree = 30;
        torso.addchild(Butt);
    }
}
