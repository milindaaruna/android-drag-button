package com.mili.dragdropbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;

/**
 * Created by Milinda on 7/17/2017.
 */


public class DragButton extends View {

    public enum Sides{
        Left, Right,  Top, Bottom
    }

    private Context ctx;
    private float imageBtnSizePercentage;
    private float dragEndThresholdPercentage;
    private int arrowCount;
    private String iconFName;


    private boolean buttonPressed;

    private float centerX;
    private float centerY;

    private Sides[] draggableSides;


    private OnDraggedListener onDraggedListener;
    private OnDragStartedListener onDragStartedListener;
    private OnDragEndedListener onDragEndedListener;

    private Bitmap icon;

//    private long btnAnimationStart;
//    private long btnAnimationDuration;//in milliseconds
//    private int btnAnimationFramesPerSecond;
//    private long btnAnimationElapsed;

    public DragButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        init(attrs);
    }

    public DragButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ctx = context;
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DragButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.ctx = context;
        init(attrs);
    }

    public void init(@Nullable AttributeSet attrs) {
        TypedArray arr = ctx.getTheme().obtainStyledAttributes(attrs, R.styleable.DragButton, 0, 0);

        imageBtnSizePercentage = arr.getFloat(R.styleable.DragButton_imageBtnSizePercentage, 0.25f);
        dragEndThresholdPercentage = arr.getFloat(R.styleable.DragButton_dragEndThresholdPercentage, 0.05f);
        arrowCount = arr.getInteger(R.styleable.DragButton_arrowCount, 3);
        iconFName = arr.getString(R.styleable.DragButton_iconFileName);
        if (iconFName == null)
            iconFName = "icon.png";

        arr.recycle();

        centerX = -1;
        centerY = -1;

        try {
            icon = BitmapFactory.decodeStream(ctx.getAssets().open(iconFName));
        } catch (IOException e) {
            e.printStackTrace();
            icon = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(icon);
            Paint paint = new Paint();
            paint.setColor(Color.argb(125, 255, 0, 0));
            paint.setStrokeWidth(15);
            paint.setStyle(Paint.Style.STROKE);

            //canvas.drawRect(1,1,510,510, paint);
            canvas.drawCircle(255, 255, 250, paint);

            RadialGradient paintGradient = new RadialGradient(255,255,250,
                    Color.RED, Color.TRANSPARENT, Shader.TileMode.CLAMP);
            paint.setStyle(Paint.Style.FILL);
            paint.setShader(paintGradient);
            canvas.drawCircle(255, 255, 250, paint);
        }

//        btnAnimationStart = -1;
//        btnAnimationDuration = 1500;
//        btnAnimationFramesPerSecond = 60;
//        btnAnimationElapsed = 0;
    }

    public Sides[] getDraggableSides() {
        return draggableSides;
    }

    public void setDraggableSides(Sides[] draggableSides) {
        this.draggableSides = draggableSides;
    }

    public void setOnDraggedListener(OnDraggedListener listener){
        this.onDraggedListener = listener;
    }

    public void setOnDragStartedListener(OnDragStartedListener listener){
        this.onDragStartedListener = listener;
    }

    public void setOnDragEndedListener(OnDragEndedListener listener){
        this.onDragEndedListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (centerX == -1 && centerY == -1) {// is init?
            centerX = getWidth() / 2;
            centerY = getHeight() / 2;
        }
        RectF bitmapRect = getBitmapRect(centerX, centerY);

        if(icon != null)
            canvas.drawBitmap(icon, null, bitmapRect, new Paint(Paint.ANTI_ALIAS_FLAG));

        drawMarkers(canvas);
    }

    private void drawMarkers(Canvas canvas) {
        Paint redLow = new Paint();
        redLow.setColor(android.graphics.Color.parseColor("#de4f4f"));
        redLow.setStyle(Paint.Style.FILL);

        float size = 20;
        float midY = getHeight() / 2.0f;

        RectF imgRect = getBitmapRect(getWidth() / 2.0f, getHeight() / 2.0f);

        // left markers
        float unitDist = (getWidth() - imgRect.right)/(arrowCount + 1);
        for(int i = 1; i <= arrowCount; i++){
            Path path = new Path();
            path.moveTo(imgRect.right + unitDist * i, midY - (size / 2.0f));
            path.lineTo(imgRect.right + unitDist * i + size, midY);
            path.lineTo(imgRect.right + unitDist * i, midY + (size / 2.0f));
            path.lineTo(imgRect.right + unitDist * i, midY - (size / 2.0f));

            path.close();

            canvas.drawPath(path, redLow);
        }

        // right markers
        for(int i = 1; i <= arrowCount; i++){
            Path path = new Path();
            path.moveTo(imgRect.left - unitDist * i, midY - (size / 2.0f));
            path.lineTo(imgRect.left - unitDist * i - size, midY);
            path.lineTo(imgRect.left - unitDist * i, midY + (size / 2.0f));
            path.lineTo(imgRect.left - unitDist * i, midY - (size / 2.0f));

            path.close();

            canvas.drawPath(path, redLow);
        }
    }

    private void moveButtonToCenter() {
        //btnAnimationStart = System.currentTimeMillis();
        //setBtnCenterXyToCenter();

        centerX = getWidth() / 2.0f;
        centerY = getHeight() / 2.0f;

        invalidate();
    }

//    private void setBtnCenterXyToCenter() {
//        float dist = getDist(getWidth() / 2.0f, getHeight() / 2.0f, centerX, centerY);//distance from current point to center
//
//        float frames = ((btnAnimationFramesPerSecond / 1000.0f) * (btnAnimationDuration - btnAnimationElapsed));
//        Log.i("FFFFFFFFFFFFFFFFFFFFF", Float.toString(frames));
//        float perFrameDist = dist / frames;
//        float nextFrameDist = perFrameDist * (frames - 1);
//
//        PointF nextPoint = getPointByDistanceAndVector(nextFrameDist, getWidth() / 2.0f, getHeight() / 2.0f, centerX, centerY);
//
//        centerX = nextPoint.x;
//        centerY = nextPoint.y;
//    }

    //v1x and v1y is the origin
    private PointF getPointByDistanceAndVector(float dist, float v1x, float v1y, float v2x, float v2y) {
        double len = getDist(v1x, v1y, v2x, v2y);
        double ratio = dist / len;
        double x = ratio * v2x + (1.0 - ratio) * v1x;
        double y = ratio * v2y + (1.0 - ratio) * v1y;

        return new PointF((float)x, (float)y);
    }

    private float getDist(float x1, float y1, float x2, float y2){
        return (float)Math.sqrt(((x1-x2) * (x1-x2)) + ((y1-y2) * (y1-y2)));
    }

    private RectF getBitmapRect(float centerX, float centerY) {
        int minMeasure = getWidth() < getHeight() ? getWidth() : getHeight();

        float bitmapSize = imageBtnSizePercentage;
        float bitmapW = minMeasure * bitmapSize;
        float bitmapH = minMeasure * bitmapSize;

        float bitmapLeft = centerX - (bitmapW / 2.0f);
        float bitmapTop = centerY - (bitmapH / 2.0f);
        float bitmapRight = bitmapLeft + bitmapW;
        float bitmapBottom = bitmapTop + bitmapH;
        RectF bitmapRect = new RectF(bitmapLeft, bitmapTop, bitmapRight, bitmapBottom);

        return bitmapRect;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            RectF btnRect = getBitmapRect(getWidth() / 2.0f, getHeight() / 2.0f);
            if(btnRect.contains(e.getX(), e.getY())) {
                buttonPressed = true;
                if(this.onDragStartedListener != null)
                    onDragStartedListener.onDragStarted(this, e);
            }
            else
                buttonPressed = false;

        }
        else if(e.getAction() == MotionEvent.ACTION_UP){
            if (buttonPressed && onDragEndedListener != null) {
                onDragEndedListener.onDragEnded(this, e);
            }
            buttonPressed = false;

            //redraw center btn image
            moveButtonToCenter();
        }
        else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            if(!buttonPressed)
                return true;

            //redraw center btn image
            centerX = e.getX();
            //centerY = e.getY();
            invalidate();

            float offset = dragEndThresholdPercentage;

            float leftMargin = getWidth() * (1 - offset);
            float rightMargin = getWidth() * offset;

            if (e.getX() > leftMargin) {
                dragged(e);
            } else if (e.getX() < rightMargin) {
                dragged(e);
            }
        }
        return true;
    }

    private void dragged(MotionEvent e) {
        buttonPressed = false;
        if(onDraggedListener != null)
            onDraggedListener.onDragged(this, e);
    }

    public interface OnDraggedListener{
        void onDragged(DragButton view, MotionEvent e);
    }

    public interface OnDragStartedListener{
        void onDragStarted(DragButton view, MotionEvent e);
    }

    public interface OnDragEndedListener{
        void onDragEnded(DragButton view, MotionEvent e);
    }


}

