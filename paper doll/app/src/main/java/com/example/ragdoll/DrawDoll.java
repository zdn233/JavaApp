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

import androidx.core.view.MotionEventCompat;

public class DrawDoll extends View{

    DrawPart torso;
    String PartSelected = null;
    Point DragInitialPosition;
    float oldX = 0;
    float oldY = 0;

    int focusOnOne = 0;
    int mode = 0;
    int previousFocusOnOne = 0;
    int detect = 0;
    String scalePartSelected = null;
    String scalePartSelectedAnother = null;


    ScaleGestureDetector mySGD;
    float myscale = 1f;



    DrawDoll(Context context) {
        super(context);
        setOnTouchListener(new MyOwnTouchListener());
        mySGD = new ScaleGestureDetector(context, new myScaleListener());
        build();
    }

    public void reset() {
        torso = null;
        PartSelected = null;
        DragInitialPosition = null;
        oldX = 0;
        oldY = 0;

        focusOnOne = 0;
        mode = 0;
        previousFocusOnOne = 0;
        detect = 0;
        scalePartSelected = null;
        scalePartSelectedAnother = null;

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


    //Own Touch Listener
    class MyOwnTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            mySGD.onTouchEvent(motionEvent);
            float previousFactor;

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                DragInitialPosition = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                PartSelected = torso.whichIsChosen(DragInitialPosition);
                oldX = motionEvent.getX();
                oldY = motionEvent.getY();

                mode = 0;
                focusOnOne += 1;

                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                if (PartSelected != null && (mode == 0 || (mode == 1 && PartSelected.equals(scalePartSelected)))) {
                    if (PartSelected.equals("torso")) {
                        torso.MoveTorso(motionEvent.getX() - oldX, motionEvent.getY() - oldY);
                        oldX = motionEvent.getX();
                        oldY = motionEvent.getY();
                        invalidate();
                    } else if (PartSelected.equals("Head")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 55;
                        originPivot[1] = 150;
                        torso.children.get(2).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180/Math.PI);
                        float realAngle = (float) angle;
                        float TotalAngle = torso.children.get(2).deltaDegree + realAngle;
                        if (TotalAngle > 360 || TotalAngle < -360) {
                            TotalAngle %= 360;
                        }
                        if (TotalAngle <= 360 && TotalAngle >= -360) {
                            if (TotalAngle <=0) {
                                TotalAngle += 360;
                            }
                        }
                        RectF testInorNot = new RectF();
                        torso.children.get(2).matrix.mapRect(testInorNot, torso.children.get(2).rectF);
                        if (((TotalAngle >= 0 && TotalAngle <= 50) || (TotalAngle >= 310 && TotalAngle <=360))
                                && testInorNot.contains(motionEvent.getX(), motionEvent.getY())) {
                            torso.children.get(2).matrix.preRotate(realAngle, 55,150);
                            torso.children.get(2).deltaDegree += realAngle;
                            oldX = motionEvent.getX();
                            oldY = motionEvent.getY();
                            invalidate();
                        }
                    } else if (PartSelected.equals("LeftUpperArm")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 30;
                        originPivot[1] = 0;
                        torso.children.get(0).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180/Math.PI);
                        float realAngle = (float) angle;
                        torso.children.get(0).matrix.preRotate(realAngle, 30,0);
                        torso.children.get(0).deltaDegree += realAngle;
                        torso.children.get(0).children.get(0).matrix.set(torso.children.get(0).matrix);
                        torso.children.get(0).children.get(0).matrix.preTranslate(0,170);
                        torso.children.get(0).children.get(0).matrix.preRotate(torso.children.get(0).children.get(0).deltaDegree, 25, 0);
                        torso.children.get(0).children.get(0).children.get(0).matrix.set(torso.children.get(0).children.get(0).matrix);
                        torso.children.get(0).children.get(0).children.get(0).matrix.preTranslate(0, 200);
                        torso.children.get(0).children.get(0).children.get(0).matrix.preRotate(torso.children.get(0).children.get(0).children.get(0).deltaDegree, 25, 0);
                        oldX = motionEvent.getX();
                        oldY = motionEvent.getY();
                        invalidate();
                    } else if (PartSelected.equals("RightUpperArm")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 20;
                        originPivot[1] = 0;
                        torso.children.get(1).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180/Math.PI);
                        float realAngle = (float) angle;
                        torso.children.get(1).matrix.preRotate(realAngle, 20,0);
                        torso.children.get(1).deltaDegree += realAngle;
                        torso.children.get(1).children.get(0).matrix.set(torso.children.get(1).matrix);
                        torso.children.get(1).children.get(0).matrix.preTranslate(0,170);
                        torso.children.get(1).children.get(0).matrix.preRotate(torso.children.get(1).children.get(0).deltaDegree, 25, 0);
                        torso.children.get(1).children.get(0).children.get(0).matrix.set(torso.children.get(1).children.get(0).matrix);
                        torso.children.get(1).children.get(0).children.get(0).matrix.preTranslate(0, 200);
                        torso.children.get(1).children.get(0).children.get(0).matrix.preRotate(torso.children.get(1).children.get(0).children.get(0).deltaDegree, 25, 0);
                        oldX = motionEvent.getX();
                        oldY = motionEvent.getY();
                        invalidate();
                    } else if (PartSelected.equals("LeftLowerArm")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 25;
                        originPivot[1] = 0;
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
                        if (((TotalAngle >= 0 && TotalAngle <= 135) || (TotalAngle >= 225 && TotalAngle <=360))
                                && testInorNot.contains(motionEvent.getX(), motionEvent.getY())) {
                            torso.children.get(0).children.get(0).matrix.preRotate(realAngle, 25,0);
                            torso.children.get(0).children.get(0).deltaDegree += realAngle;
                            torso.children.get(0).children.get(0).children.get(0).matrix.set(torso.children.get(0).children.get(0).matrix);
                            torso.children.get(0).children.get(0).children.get(0).matrix.preTranslate(0, 200);
                            torso.children.get(0).children.get(0).children.get(0).matrix.preRotate(torso.children.get(0).children.get(0).children.get(0).deltaDegree, 25, 0);
                            oldX = motionEvent.getX();
                            oldY = motionEvent.getY();
                            invalidate();
                        }
                    } else if (PartSelected.equals("RightLowerArm")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 25;
                        originPivot[1] = 0;
                        torso.children.get(1).children.get(0).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180 / Math.PI);
                        float realAngle = (float) angle;
                        float TotalAngle = torso.children.get(1).children.get(0).deltaDegree + realAngle;
                        if (TotalAngle > 360 || TotalAngle < -360) {
                            TotalAngle %= 360;
                        }
                        if (TotalAngle <= 360 && TotalAngle >= -360) {
                            if (TotalAngle <= 0) {
                                TotalAngle += 360;
                            }
                        }
                        RectF testInorNot = new RectF();
                        torso.children.get(1).children.get(0).matrix.mapRect(testInorNot,
                                torso.children.get(1).children.get(0).rectF);
                        if (((TotalAngle >= 0 && TotalAngle <= 135) || (TotalAngle >= 225 && TotalAngle <= 360))
                                && testInorNot.contains(motionEvent.getX(), motionEvent.getY())) {
                            torso.children.get(1).children.get(0).matrix.preRotate(realAngle, 25, 0);
                            torso.children.get(1).children.get(0).deltaDegree += realAngle;
                            torso.children.get(1).children.get(0).children.get(0).matrix.set(torso.children.get(1).children.get(0).matrix);
                            torso.children.get(1).children.get(0).children.get(0).matrix.preTranslate(0, 200);
                            torso.children.get(1).children.get(0).children.get(0).matrix.preRotate(torso.children.get(1).children.get(0).children.get(0).deltaDegree, 25, 0);
                            oldX = motionEvent.getX();
                            oldY = motionEvent.getY();
                            invalidate();
                        }
                    } else if (PartSelected.equals("LeftHand")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 25;
                        originPivot[1] = 0;
                        torso.children.get(0).children.get(0).children.get(0).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180 / Math.PI);
                        float realAngle = (float) angle;
                        float TotalAngle = torso.children.get(0).children.get(0).children.get(0).deltaDegree + realAngle;
                        if (TotalAngle > 360 || TotalAngle < -360) {
                            TotalAngle %= 360;
                        }
                        if (TotalAngle <= 360 && TotalAngle >= -360) {
                            if (TotalAngle <= 0) {
                                TotalAngle += 360;
                            }
                        }
                        RectF testInorNot = new RectF();
                        torso.children.get(0).children.get(0).children.get(0).matrix.mapRect(testInorNot,
                                torso.children.get(0).children.get(0).children.get(0).rectF);
                        if (((TotalAngle >= 0 && TotalAngle <= 35) || (TotalAngle >= 325 && TotalAngle <= 360))
                                && testInorNot.contains(motionEvent.getX(), motionEvent.getY())) {
                            torso.children.get(0).children.get(0).children.get(0).matrix.preRotate(realAngle, 25, 0);
                            torso.children.get(0).children.get(0).children.get(0).deltaDegree += realAngle;
                            oldX = motionEvent.getX();
                            oldY = motionEvent.getY();
                            invalidate();
                        }
                    } else if (PartSelected.equals("RightHand")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 25;
                        originPivot[1] = 0;
                        torso.children.get(1).children.get(0).children.get(0).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180 / Math.PI);
                        float realAngle = (float) angle;
                        float TotalAngle = torso.children.get(1).children.get(0).children.get(0).deltaDegree + realAngle;
                        if (TotalAngle > 360 || TotalAngle < -360) {
                            TotalAngle %= 360;
                        }
                        if (TotalAngle <= 360 && TotalAngle >= -360) {
                            if (TotalAngle <= 0) {
                                TotalAngle += 360;
                            }
                        }
                        RectF testInorNot = new RectF();
                        torso.children.get(1).children.get(0).children.get(0).matrix.mapRect(testInorNot,
                                torso.children.get(1).children.get(0).children.get(0).rectF);
                        if (((TotalAngle >= 0 && TotalAngle <= 35) || (TotalAngle >= 325 && TotalAngle <= 360))
                                && testInorNot.contains(motionEvent.getX(), motionEvent.getY())) {
                            torso.children.get(1).children.get(0).children.get(0).matrix.preRotate(realAngle, 25, 0);
                            torso.children.get(1).children.get(0).children.get(0).deltaDegree += realAngle;
                            oldX = motionEvent.getX();
                            oldY = motionEvent.getY();
                            invalidate();
                        }
                    } else if (PartSelected.equals("LeftUpperLeg")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 35;
                        originPivot[1] = 0;
                        torso.children.get(3).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180 / Math.PI);
                        float realAngle = (float) angle;
                        float TotalAngle = torso.children.get(3).deltaDegree + realAngle;
                        if (TotalAngle > 360 || TotalAngle < -360) {
                            TotalAngle %= 360;
                        }
                        if (TotalAngle <= 360 && TotalAngle >= -360) {
                            if (TotalAngle <= 0) {
                                TotalAngle += 360;
                            }
                        }
                        RectF testInorNot = new RectF();
                        torso.children.get(3).matrix.mapRect(testInorNot, torso.children.get(3).rectF);
                        if (((TotalAngle >= 0 && TotalAngle <= 90) || (TotalAngle >= 270 && TotalAngle <= 360))
                                && testInorNot.contains(motionEvent.getX(), motionEvent.getY())) {
                            // Update Angle
                            torso.children.get(3).deltaDegree += realAngle;
                            // Let every child fit
                            torso.children.get(3).matrix.set(torso.matrix);
                            torso.children.get(3).matrix.preTranslate(20, 340);
                            torso.children.get(3).matrix.preRotate(torso.children.get(3).deltaDegree, 35, 0);
                            torso.children.get(3).matrix.preScale(1,  torso.children.get(3).scaleFactor, 0, 0);
                            torso.children.get(3).children.get(0).matrix.set(torso.children.get(3).matrix);
                            torso.children.get(3).children.get(0).matrix.preTranslate(0, 200);
                            previousFactor = (float) 1.0 / torso.children.get(3).scaleFactor;
                            torso.children.get(3).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                            torso.children.get(3).children.get(0).matrix.preRotate(torso.children.get(3).children.get(0).deltaDegree, 35,0);
                            torso.children.get(3).children.get(0).matrix.preScale(1, torso.children.get(3).scaleFactor, 0, 0);
                            torso.children.get(3).children.get(0).matrix.preScale(1, torso.children.get(3).children.get(0).scaleFactor, 0, 0);
                            torso.children.get(3).children.get(0).children.get(0).matrix.set(torso.children.get(3).children.get(0).matrix);
                            torso.children.get(3).children.get(0).children.get(0).matrix.preTranslate(10,250);
                            previousFactor = (float) 1.0 / torso.children.get(3).children.get(0).scaleFactor;
                            torso.children.get(3).children.get(0).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                            previousFactor = (float) 1.0 / torso.children.get(3).scaleFactor;
                            torso.children.get(3).children.get(0).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                            torso.children.get(3).children.get(0).children.get(0).matrix.preRotate(90, 25, 0);
                            torso.children.get(3).children.get(0).children.get(0).matrix.preRotate(torso.children.get(3).children.get(0).children.get(0).deltaDegree, 25, 0);
                            oldX = motionEvent.getX();
                            oldY = motionEvent.getY();
                            invalidate();

                        }
                    } else if (PartSelected.equals("RightUpperLeg")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 35;
                        originPivot[1] = 0;
                        torso.children.get(4).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180 / Math.PI);
                        float realAngle = (float) angle;
                        float TotalAngle = torso.children.get(4).deltaDegree + realAngle;
                        if (TotalAngle > 360 || TotalAngle < -360) {
                            TotalAngle %= 360;
                        }
                        if (TotalAngle <= 360 && TotalAngle >= -360) {
                            if (TotalAngle <= 0) {
                                TotalAngle += 360;
                            }
                        }
                        RectF testInorNot = new RectF();
                        torso.children.get(4).matrix.mapRect(testInorNot, torso.children.get(4).rectF);
                        if (((TotalAngle >= 0 && TotalAngle <= 90) || (TotalAngle >= 270 && TotalAngle <= 360))
                                && testInorNot.contains(motionEvent.getX(), motionEvent.getY())) {
                            // Update angle
                            torso.children.get(4).deltaDegree += realAngle;
                            // Let every child fit
                            torso.children.get(4).matrix.set(torso.matrix);
                            torso.children.get(4).matrix.preTranslate(145, 340);
                            torso.children.get(4).matrix.preRotate(torso.children.get(4).deltaDegree, 35, 0);
                            torso.children.get(4).matrix.preScale(1, torso.children.get(4).scaleFactor, 0, 0);
                            torso.children.get(4).children.get(0).matrix.set(torso.children.get(4).matrix);
                            torso.children.get(4).children.get(0).matrix.preTranslate(0, 200);
                            previousFactor = (float) 1.0 / torso.children.get(4).scaleFactor;
                            torso.children.get(4).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                            torso.children.get(4).children.get(0).matrix.preRotate(torso.children.get(4).children.get(0).deltaDegree, 35,0);
                            torso.children.get(4).children.get(0).matrix.preScale(1, torso.children.get(4).scaleFactor, 0, 0);
                            torso.children.get(4).children.get(0).matrix.preScale(1, torso.children.get(4).children.get(0).scaleFactor, 0, 0);
                            torso.children.get(4).children.get(0).children.get(0).matrix.set(torso.children.get(4).children.get(0).matrix);
                            torso.children.get(4).children.get(0).children.get(0).matrix.preTranslate(10,250);
                            previousFactor = (float) 1.0 / torso.children.get(4).children.get(0).scaleFactor;
                            torso.children.get(4).children.get(0).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                            previousFactor = (float) 1.0 / torso.children.get(4).scaleFactor;
                            torso.children.get(4).children.get(0).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                            torso.children.get(4).children.get(0).children.get(0).matrix.preRotate(-90, 25, 0);
                            torso.children.get(4).children.get(0).children.get(0).matrix.preRotate(torso.children.get(4).children.get(0).children.get(0).deltaDegree, 25, 0);
                            oldX = motionEvent.getX();
                            oldY = motionEvent.getY();
                            invalidate();
                        }
                    } else if (PartSelected.equals("LeftLowerLeg")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 35;
                        originPivot[1] = 0;
                        torso.children.get(3).children.get(0).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180/Math.PI);
                        float realAngle = (float) angle;
                        float TotalAngle = torso.children.get(3).children.get(0).deltaDegree + realAngle;
                        if (TotalAngle > 360 || TotalAngle < -360) {
                            TotalAngle %= 360;
                        }
                        if (TotalAngle <= 360 && TotalAngle >= -360) {
                            if (TotalAngle <=0) {
                                TotalAngle += 360;
                            }
                        }
                        RectF testInorNot = new RectF();
                        torso.children.get(3).children.get(0).matrix.mapRect(testInorNot,
                                torso.children.get(3).children.get(0).rectF);
                        if (((TotalAngle >= 0 && TotalAngle <= 90) || (TotalAngle >= 270 && TotalAngle <=360))
                                && testInorNot.contains(motionEvent.getX(), motionEvent.getY())) {
                            // Update Angle
                            torso.children.get(3).children.get(0).deltaDegree += realAngle;
                            // Let every child fit
                            torso.children.get(3).matrix.set(torso.matrix);
                            torso.children.get(3).matrix.preTranslate(20, 340);
                            torso.children.get(3).matrix.preRotate(torso.children.get(3).deltaDegree, 35, 0);
                            torso.children.get(3).matrix.preScale(1,  torso.children.get(3).scaleFactor, 0, 0);
                            torso.children.get(3).children.get(0).matrix.set(torso.children.get(3).matrix);
                            torso.children.get(3).children.get(0).matrix.preTranslate(0, 200);
                            previousFactor = (float) 1.0 / torso.children.get(3).scaleFactor;
                            torso.children.get(3).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                            torso.children.get(3).children.get(0).matrix.preRotate(torso.children.get(3).children.get(0).deltaDegree, 35,0);
                            torso.children.get(3).children.get(0).matrix.preScale(1, torso.children.get(3).scaleFactor, 0, 0);
                            torso.children.get(3).children.get(0).matrix.preScale(1, torso.children.get(3).children.get(0).scaleFactor, 0, 0);
                            torso.children.get(3).children.get(0).children.get(0).matrix.set(torso.children.get(3).children.get(0).matrix);
                            torso.children.get(3).children.get(0).children.get(0).matrix.preTranslate(10,250);
                            previousFactor = (float) 1.0 / torso.children.get(3).children.get(0).scaleFactor;
                            torso.children.get(3).children.get(0).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                            previousFactor = (float) 1.0 / torso.children.get(3).scaleFactor;
                            torso.children.get(3).children.get(0).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                            torso.children.get(3).children.get(0).children.get(0).matrix.preRotate(90, 25, 0);
                            torso.children.get(3).children.get(0).children.get(0).matrix.preRotate(torso.children.get(3).children.get(0).children.get(0).deltaDegree, 25, 0);
                            oldX = motionEvent.getX();
                            oldY = motionEvent.getY();
                            invalidate();
                        }
                    } else if (PartSelected.equals("RightLowerLeg")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 35;
                        originPivot[1] = 0;
                        torso.children.get(4).children.get(0).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180/Math.PI);
                        float realAngle = (float) angle;
                        float TotalAngle = torso.children.get(4).children.get(0).deltaDegree + realAngle;
                        if (TotalAngle > 360 || TotalAngle < -360) {
                            TotalAngle %= 360;
                        }
                        if (TotalAngle <= 360 && TotalAngle >= -360) {
                            if (TotalAngle <=0) {
                                TotalAngle += 360;
                            }
                        }
                        RectF testInorNot = new RectF();
                        torso.children.get(4).children.get(0).matrix.mapRect(testInorNot,
                                torso.children.get(4).children.get(0).rectF);
                        if (((TotalAngle >= 0 && TotalAngle <= 90) || (TotalAngle >= 270 && TotalAngle <=360))
                                && testInorNot.contains(motionEvent.getX(), motionEvent.getY())) {
                            // Update angle
                            torso.children.get(4).children.get(0).deltaDegree += realAngle;
                            // Let every child fit
                            torso.children.get(4).matrix.set(torso.matrix);
                            torso.children.get(4).matrix.preTranslate(145, 340);
                            torso.children.get(4).matrix.preRotate(torso.children.get(4).deltaDegree, 35, 0);
                            torso.children.get(4).matrix.preScale(1, torso.children.get(4).scaleFactor, 0, 0);
                            torso.children.get(4).children.get(0).matrix.set(torso.children.get(4).matrix);
                            torso.children.get(4).children.get(0).matrix.preTranslate(0, 200);
                            previousFactor = (float) 1.0 / torso.children.get(4).scaleFactor;
                            torso.children.get(4).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                            torso.children.get(4).children.get(0).matrix.preRotate(torso.children.get(4).children.get(0).deltaDegree, 35,0);
                            torso.children.get(4).children.get(0).matrix.preScale(1, torso.children.get(4).scaleFactor, 0, 0);
                            torso.children.get(4).children.get(0).matrix.preScale(1, torso.children.get(4).children.get(0).scaleFactor, 0, 0);
                            torso.children.get(4).children.get(0).children.get(0).matrix.set(torso.children.get(4).children.get(0).matrix);
                            torso.children.get(4).children.get(0).children.get(0).matrix.preTranslate(10,250);
                            previousFactor = (float) 1.0 / torso.children.get(4).children.get(0).scaleFactor;
                            torso.children.get(4).children.get(0).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                            previousFactor = (float) 1.0 / torso.children.get(4).scaleFactor;
                            torso.children.get(4).children.get(0).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                            torso.children.get(4).children.get(0).children.get(0).matrix.preRotate(-90, 25, 0);
                            torso.children.get(4).children.get(0).children.get(0).matrix.preRotate(torso.children.get(4).children.get(0).children.get(0).deltaDegree, 25, 0);
                            oldX = motionEvent.getX();
                            oldY = motionEvent.getY();
                            invalidate();
                        }
                    } else if (PartSelected.equals("LeftFoot")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 25;
                        originPivot[1] = 0;
                        torso.children.get(3).children.get(0).children.get(0).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180 / Math.PI);
                        float realAngle = (float) angle;
                        float TotalAngle = torso.children.get(3).children.get(0).children.get(0).deltaDegree + realAngle;
                        if (TotalAngle > 360 || TotalAngle < -360) {
                            TotalAngle %= 360;
                        }
                        if (TotalAngle <= 360 && TotalAngle >= -360) {
                            if (TotalAngle <= 0) {
                                TotalAngle += 360;
                            }
                        }
                        RectF testInorNot = new RectF();
                        torso.children.get(3).children.get(0).children.get(0).matrix.mapRect(testInorNot,
                                torso.children.get(3).children.get(0).children.get(0).rectF);
                        if (((TotalAngle >= 0 && TotalAngle <= 35) || (TotalAngle >= 325 && TotalAngle <= 360))
                                && testInorNot.contains(motionEvent.getX(), motionEvent.getY())) {
                            torso.children.get(3).children.get(0).children.get(0).matrix.preRotate(realAngle, 25, 0);
                            torso.children.get(3).children.get(0).children.get(0).deltaDegree += realAngle;
                            oldX = motionEvent.getX();
                            oldY = motionEvent.getY();
                            invalidate();
                        }
                    } else if (PartSelected.equals("RightFoot")) {
                        float[] tempPivot = new float[2];
                        float[] originPivot = new float[2];
                        originPivot[0] = 25;
                        originPivot[1] = 0;
                        torso.children.get(4).children.get(0).children.get(0).matrix.mapPoints(tempPivot, originPivot);
                        double angle = getAngle(oldX, oldY,
                                motionEvent.getX(), motionEvent.getY(), tempPivot[0], tempPivot[1]) * (180 / Math.PI);
                        float realAngle = (float) angle;
                        float TotalAngle = torso.children.get(4).children.get(0).children.get(0).deltaDegree + realAngle;
                        if (TotalAngle > 360 || TotalAngle < -360) {
                            TotalAngle %= 360;
                        }
                        if (TotalAngle <= 360 && TotalAngle >= -360) {
                            if (TotalAngle <= 0) {
                                TotalAngle += 360;
                            }
                        }
                        RectF testInorNot = new RectF();
                        torso.children.get(4).children.get(0).children.get(0).matrix.mapRect(testInorNot,
                                torso.children.get(4).children.get(0).children.get(0).rectF);
                        if (((TotalAngle >= 0 && TotalAngle <= 35) || (TotalAngle >= 325 && TotalAngle <= 360))
                                && testInorNot.contains(motionEvent.getX(), motionEvent.getY())) {
                            torso.children.get(4).children.get(0).children.get(0).matrix.preRotate(realAngle, 25, 0);
                            torso.children.get(4).children.get(0).children.get(0).deltaDegree += realAngle;
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


    // Own scale gesture detector
    private class myScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {


        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            myscale = detector.getScaleFactor();

            Point FocusPoint = new Point((int) detector.getFocusX(), (int) detector.getFocusY());

            String s = torso.whichIsChosen(FocusPoint);

            float previousFactor;

            mode = 1;
            detect = 0;

            if (s != null) {
                if (focusOnOne != previousFocusOnOne) {
                    scalePartSelectedAnother = s;
                    scalePartSelected = s;
                    PartSelected = s;
                }
                if (scalePartSelectedAnother != null) {
                    if (!((!scalePartSelectedAnother.equals(s)) && (previousFocusOnOne == focusOnOne))) {
                        detect = 1;
                    }
                } else if (focusOnOne == 1) {
                    detect = 1;
                }
                if (detect == 1) {
                    if (s.equals("LeftUpperLeg") || s.equals("LeftLowerLeg")) {
                        if (s.equals("LeftUpperLeg")) {
                            torso.children.get(3).scaleFactor *= myscale;
                        } else {
                            torso.children.get(3).children.get(0).scaleFactor *= myscale;
                        }
                        torso.children.get(3).matrix.set(torso.matrix);
                        torso.children.get(3).matrix.preTranslate(20, 340);
                        torso.children.get(3).matrix.preRotate(torso.children.get(3).deltaDegree, 35, 0);
                        torso.children.get(3).matrix.preScale(1, torso.children.get(3).scaleFactor, 0, 0);
                        torso.children.get(3).children.get(0).matrix.set(torso.children.get(3).matrix);
                        torso.children.get(3).children.get(0).matrix.preTranslate(0, 200);
                        previousFactor = (float) 1.0 / torso.children.get(3).scaleFactor;
                        torso.children.get(3).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                        torso.children.get(3).children.get(0).matrix.preRotate(torso.children.get(3).children.get(0).deltaDegree, 35, 0);
                        torso.children.get(3).children.get(0).matrix.preScale(1, torso.children.get(3).scaleFactor, 0, 0);
                        torso.children.get(3).children.get(0).matrix.preScale(1, torso.children.get(3).children.get(0).scaleFactor, 0, 0);
                        torso.children.get(3).children.get(0).children.get(0).matrix.set(torso.children.get(3).children.get(0).matrix);
                        torso.children.get(3).children.get(0).children.get(0).matrix.preTranslate(10, 250);
                        previousFactor = (float) 1.0 / torso.children.get(3).children.get(0).scaleFactor;
                        torso.children.get(3).children.get(0).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                        previousFactor = (float) 1.0 / torso.children.get(3).scaleFactor;
                        torso.children.get(3).children.get(0).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                        torso.children.get(3).children.get(0).children.get(0).matrix.preRotate(90, 25, 0);
                        torso.children.get(3).children.get(0).children.get(0).matrix.preRotate(torso.children.get(3).children.get(0).children.get(0).deltaDegree, 25, 0);
                        invalidate();
                    } else if (s.equals("RightUpperLeg") || s.equals("RightLowerLeg")) {
                        if (s.equals("RightUpperLeg")) {
                            torso.children.get(4).scaleFactor *= myscale;
                        } else {
                            torso.children.get(4).children.get(0).scaleFactor *= myscale;
                        }
                        torso.children.get(4).matrix.set(torso.matrix);
                        torso.children.get(4).matrix.preTranslate(145, 340);
                        torso.children.get(4).matrix.preRotate(torso.children.get(4).deltaDegree, 35, 0);
                        torso.children.get(4).matrix.preScale(1, torso.children.get(4).scaleFactor, 0, 0);
                        torso.children.get(4).children.get(0).matrix.set(torso.children.get(4).matrix);
                        torso.children.get(4).children.get(0).matrix.preTranslate(0, 200);
                        previousFactor = (float) 1.0 / torso.children.get(4).scaleFactor;
                        torso.children.get(4).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                        torso.children.get(4).children.get(0).matrix.preRotate(torso.children.get(4).children.get(0).deltaDegree, 35, 0);
                        torso.children.get(4).children.get(0).matrix.preScale(1, torso.children.get(4).scaleFactor, 0, 0);
                        torso.children.get(4).children.get(0).matrix.preScale(1, torso.children.get(4).children.get(0).scaleFactor, 0, 0);
                        torso.children.get(4).children.get(0).children.get(0).matrix.set(torso.children.get(4).children.get(0).matrix);
                        torso.children.get(4).children.get(0).children.get(0).matrix.preTranslate(10, 250);
                        previousFactor = (float) 1.0 / torso.children.get(4).children.get(0).scaleFactor;
                        torso.children.get(4).children.get(0).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                        previousFactor = (float) 1.0 / torso.children.get(4).scaleFactor;
                        torso.children.get(4).children.get(0).children.get(0).matrix.preScale(1, previousFactor, 0, 0);
                        torso.children.get(4).children.get(0).children.get(0).matrix.preRotate(-90, 25, 0);
                        torso.children.get(4).children.get(0).children.get(0).matrix.preRotate(torso.children.get(4).children.get(0).children.get(0).deltaDegree, 25, 0);
                        invalidate();
                    }
                }
            }
            previousFocusOnOne = focusOnOne;
            return true;
        }
    }



    void build() {
        torso = new DrawPart(new RectF(0, 0, 250, 340), "torso");
        torso.translate(1000,400);

        //left arm (at index 0 of torso children)
        //Upper arm
        DrawPart LeftUpperArm = new DrawPart(new RectF(0, 0, 50, 170), "LeftUpperArm");
        LeftUpperArm.translate(965, 400);
        LeftUpperArm.matrix.preRotate(15, LeftUpperArm.rectF.right, LeftUpperArm.rectF.top);
        torso.addchild(LeftUpperArm);
        //Lower arm
        DrawPart LeftLowerArm = new DrawPart(new RectF(0, 0, 50, 200), "LeftLowerArm");
        LeftLowerArm.matrix.set(LeftUpperArm.matrix);
        LeftLowerArm.deltaDegree = 0;
        LeftLowerArm.MaxDegree = 135;
        LeftLowerArm.matrix.preTranslate(0, 170);
        LeftUpperArm.addchild(LeftLowerArm);
        //LeftHand
        DrawPart LeftHand = new DrawPart(new RectF(0,0, 50, 60), "LeftHand");
        LeftHand.matrix.set(LeftLowerArm.matrix);
        LeftHand.deltaDegree = 0;
        LeftHand.MaxDegree = 35;
        LeftHand.matrix.preTranslate(0, 200);
        LeftLowerArm.addchild(LeftHand);

        //right arm (at index 1 of torso children)
        //Upper arm
        DrawPart RightUpperArm = new DrawPart(new RectF(0, 0, 50, 170), "RightUpperArm");
        RightUpperArm.translate(1235, 400);
        RightUpperArm.matrix.preRotate(-15, RightUpperArm.rectF.left, RightUpperArm.rectF.top);
        torso.addchild(RightUpperArm);
        //Lower arm
        DrawPart RightLowerArm = new DrawPart(new RectF(0, 0, 50, 200), "RightLowerArm");
        RightLowerArm.matrix.set(RightUpperArm.matrix);
        RightLowerArm.deltaDegree = 0;
        RightLowerArm.MaxDegree = 135;
        RightLowerArm.matrix.preTranslate(0, 170);
        RightUpperArm.addchild(RightLowerArm);
        //LeftHand
        DrawPart RightHand = new DrawPart(new RectF(0,0, 50, 60), "RightHand");
        RightHand.matrix.set(RightLowerArm.matrix);
        RightHand.deltaDegree = 0;
        RightHand.MaxDegree = 35;
        RightHand.matrix.preTranslate(0, 200);
        RightLowerArm.addchild(RightHand);

        //head (at index 2 of torso children)
        DrawPart Head = new DrawPart(new RectF(0, 0, 110, 150), "Head");
        Head.translate(1070, 250);
        Head.deltaDegree = 0;
        Head.MaxDegree = 50;
        torso.addchild(Head);

        //left leg (at index 3 of torso children)
        //Upper leg
        DrawPart LeftUpperLeg = new DrawPart(new RectF(0, 0, 70, 200), "LeftUpperLeg");
        LeftUpperLeg.translate(1020, 740);
        LeftUpperLeg.deltaDegree = 0;
        LeftUpperLeg.MaxDegree = 90;
        torso.addchild(LeftUpperLeg);
        //Lower leg
        DrawPart LeftLowerLeg = new DrawPart(new RectF(0,0,70, 250), "LeftLowerLeg");
        LeftLowerLeg.matrix.set(LeftUpperLeg.matrix);
        LeftLowerLeg.deltaDegree = 0;
        LeftLowerLeg.MaxDegree = 90;
        LeftLowerLeg.matrix.preTranslate(0, 200);
        LeftUpperLeg.addchild(LeftLowerLeg);
        //Foot
        DrawPart LeftFoot = new DrawPart(new RectF(0,0,50,110), "LeftFoot");
        LeftFoot.matrix.set(LeftLowerLeg.matrix);
        LeftFoot.deltaDegree = 0;
        LeftFoot.MaxDegree = 35;
        LeftFoot.matrix.preTranslate(10, 250);
        LeftFoot.matrix.preRotate(90, 25, 0);
        LeftLowerLeg.addchild(LeftFoot);

        //right leg (at index 4 of torso children)
        //Upper leg
        DrawPart RightUpperLeg = new DrawPart(new RectF(0, 0, 70, 200), "RightUpperLeg");
        RightUpperLeg.translate(1145, 740);
        RightUpperLeg.deltaDegree = 0;
        RightUpperLeg.MaxDegree = 90;
        torso.addchild(RightUpperLeg);
        //Lower leg
        DrawPart RightLowerLeg = new DrawPart(new RectF(0,0,70, 250), "RightLowerLeg");
        RightLowerLeg.matrix.set(RightUpperLeg.matrix);
        RightLowerLeg.deltaDegree = 0;
        RightLowerLeg.MaxDegree = 90;
        RightLowerLeg.matrix.preTranslate(0, 200);
        RightUpperLeg.addchild(RightLowerLeg);
        //Foot
        DrawPart RightFoot = new DrawPart(new RectF(0,0,50,110), "RightFoot");
        RightFoot.matrix.set(RightLowerLeg.matrix);
        RightFoot.deltaDegree = 0;
        RightFoot.MaxDegree = 35;
        RightFoot.matrix.preTranslate(10, 250);
        RightFoot.matrix.preRotate(-90, 25, 0);
        RightLowerLeg.addchild(RightFoot);
    }
}
