package com.example.ragdoll;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import java.util.Vector;

public class DrawPart {
    String type;
    RectF rectF; //used to bound the shape for each part, easy to calculate contains or not
    Vector<DrawPart> children = new Vector<>();
    DrawPart parent;
    Matrix matrix = new Matrix(); // identity matrix

    float deltaDegree = 0;
    float MaxDegree = 0;

    float scaleFactor = 1;


    DrawPart(RectF rectF, String type) {
        this.rectF = rectF;
        this.type = type;
    }


    // Methods related to movement
    // Translate by dx, dy
    // Appends to the current matrix, so operations are cumulative
    public void translate(float dx, float dy) {
        matrix.postTranslate(dx, dy);
    }

    // Scale by sx, sy
    // Appends to the current matrix, so operations are cumulative
    public void scale(float sx, float sy) {
        matrix.postScale(sx, sy);
    }

    // Move the whole doll
    public void MoveTorso(float deltaX, float deltaY) {
        for (DrawPart obj : children) {
            obj.MoveTorso(deltaX, deltaY);
        }
        this.translate(deltaX, deltaY);
    }



    public void addchild(DrawPart obj) {
        this.children.add(obj);
        obj.parent = this;
    }

    // used for doll-part select detection
    public String whichIsChosen(Point p) {
        for (DrawPart obj : children) {
            String tempresult = obj.whichIsChosen(p);
            if (tempresult != null) {
                return tempresult;
            }
        }
        RectF hittest = new RectF();
        this.matrix.mapRect(hittest, rectF);
        if (hittest.contains(p.x, p.y)) {
            return type;
        } else {
            return null;
        }
    }

    public void draw(Canvas canvas) {
        //save the current canvas
        canvas.save();
        //apply this part's matrix on the canvas
        canvas.setMatrix(matrix);
        //set paint and style
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        //draw this part
        if (type.equals("torso")) {
            canvas.drawRoundRect(rectF,30, 30, paint);
        } else {
            canvas.drawOval(rectF, paint);
        }
        //get back to the previous canvas
        canvas.restore();

        //draw children
        for (DrawPart obj : children) {
            canvas.save();
            obj.draw(canvas);
            canvas.restore();
        }
    }
}



