package com.dannycheung.canvaslayout.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.dannycheung.canvaslayout.dom.VitualDom;
import com.dannycheung.canvaslayout.node.ViewNode;
import com.facebook.yoga.YogaConstants;

public class CanvasLayout extends View {

    protected ViewNode root;
    private Paint paint;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    //
    public boolean isInScrollingContainer;
    private boolean isInScrollingContainerFlag;

    public CanvasLayout(Context context) {
        super(context);
        init();
    }

    public CanvasLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        setWillNotDraw(false);
        this.setDrawingCacheEnabled(false);
    }

    public void render(ViewNode root) {
        this.root = root;
        requestLayout();
        invalidate();
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (root != null) {
//            this.root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED);
//            setMeasuredDimension((int) (this.root.getLayoutWidth() * VitualDom.getDensity()), (int) (this.root.getLayoutHeight() * VitualDom.getDensity()));
//        }
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (root != null) {
            if (!(getParent() instanceof CanvasLayout)) {
                createLayout(widthMeasureSpec, heightMeasureSpec);
            }
//            float measureWidth = root.getWidth().unit == YogaUnit.POINT || root.getWidth().unit == YogaUnit.AUTO ?
//                    root.getLayoutWidth() * VitualDom.getDensity(): root.getLayoutWidth();
//            float measureHeight = root.getHeight().unit == YogaUnit.POINT || root.getHeight().unit == YogaUnit.AUTO ?
//                    root.getLayoutHeight() * VitualDom.getDensity(): root.getLayoutHeight();
            float measureWidth = root.getLayoutHeight() * VitualDom.getDensity();
            float measureHeight = root.getLayoutHeight() * VitualDom.getDensity();
            final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

            /*
            if ("test".equals(root.id)) {
                Log.e("Danny", measureWidth + " " + measureHeight);
                Log.e("Danny", widthSize + " " + heightSize);
                Log.e("Danny", widthMode + " " + heightMode);
                Log.e("Danny", "-----------end---------");
            }
            */

            switch (widthMode) {
                case MeasureSpec.AT_MOST:
                case MeasureSpec.EXACTLY:
                    measureWidth = widthSize;
                    break;
            }
            switch (heightMode) {
                case MeasureSpec.AT_MOST:
                case MeasureSpec.EXACTLY:
                    measureHeight = heightSize;
                    break;
            }

            setMeasuredDimension(
                    Math.round(measureWidth),
                    Math.round(measureHeight));
        }
    }

    private void createLayout(int widthMeasureSpec, int heightMeasureSpec) {
        if (root != null) {
            final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

//            if (heightMode == MeasureSpec.EXACTLY) {
//                root.setHeight(heightSize / VitualDom.getDensity());
//            }
//            if (widthMode == MeasureSpec.EXACTLY) {
//                root.setWidth(widthSize / VitualDom.getDensity());
//            }
//            if (heightMode == MeasureSpec.AT_MOST) {
//                root.setMaxHeight(heightSize / VitualDom.getDensity());
//                if (heightSize > 0) {
//                    root.setHeight(heightSize / VitualDom.getDensity());
//                }
//            }
//            if (widthMode == MeasureSpec.AT_MOST) {
//                root.setMaxWidth(widthSize / VitualDom.getDensity());
//                root.setWidth(widthSize / VitualDom.getDensity());
//            }
            root.setWidth(widthSize / VitualDom.getDensity());
            root.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(paintFlagsDrawFilter);
        if (root != null) {
            root.onDraw(this, canvas, paint);
        }
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (root != null) {
            boolean result = root.dispatchTouchEvent(ev, getPaddingLeft(), getPaddingTop());
            if (!result) {
                result = super.dispatchTouchEvent(ev);
            }
            return result;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isInScrollingContainer = isInScrollingContainer() || isInScrollingContainerFlag;
        if (root != null) {
            root.onAttachedToWindow();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isInScrollingContainer = isInScrollingContainer() || isInScrollingContainerFlag;
        if (root != null) {
            root.onDetachedFromWindow();
        }
    }

    public ViewNode getRoot() {
        return root;
    }

    public void setInScrollingContainer(boolean value) {
        this.isInScrollingContainerFlag = value;
        isInScrollingContainer = isInScrollingContainer() || isInScrollingContainerFlag;
    }

    private boolean isInScrollingContainer() {
        ViewParent p = getParent();
        while (p != null && p instanceof ViewGroup) {
            if (((ViewGroup) p).shouldDelayChildPressedState()) {
                return true;
            }
            p = p.getParent();
        }
        return false;
    }
}
