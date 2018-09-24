package com.dannycheung.canvaslayout.node;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.dannycheung.canvaslayout.dom.VitualDom;
import com.dannycheung.canvaslayout.layout.CanvasLayout;
import com.dannycheung.canvaslayout.tools.draw.PathTools;
import com.facebook.yoga.YogaNode;

import java.lang.ref.WeakReference;

public class ViewNode extends YogaNode {

    // prop
    public enum LineStyle {
        MIDDLE, OUTSIDE, INSIDE
    }

    public String id;
    public int backgroundColor;
    public float backgroundCorner;
    public boolean[] backgroundCornerSpec;
    public int lineColor;
    public float lineWeight;
    public float degrees;
    public LineStyle lineStyle = LineStyle.MIDDLE;
    public int alpha = 255;
    public boolean forceSelfAlpha = false;
    public OnClickListener onClickListener;
    public OnTouchListener onTouchListener;
    public OnLongClickListener onLongClickListener;
    public OnAnimationUpdateListener onAnimationUpdateListener;
    public Object data;
    public Shadow shadow;
    public boolean isStatic;
    public boolean visible = true;
    // click
    private WeakReference<CanvasLayout> layoutRef;
    private static final float CLICK_RANGE = 8 * VitualDom.getDensity();
    protected boolean isPressed;
    protected boolean isMoved;
    private float oldDownRawX, oldDownRawY;

    public interface OnClickListener {
        void onClick(ViewNode viewNode, Object data);
    }

    public interface OnTouchListener {
        boolean onTouch(ViewNode viewNode, MotionEvent event, Object data);
    }

    public interface OnLongClickListener {
        void onLongClick(ViewNode viewNode, Object data);
    }

    public interface OnAnimationUpdateListener {
        boolean onUpdate(ViewNode viewNode, Object data);
    }

    public static class Shadow {
        public float radius;
        public float contentCorner;
        public float dx;
        public float dy;
        public int shadowColor = 0x88ff0000;
        public boolean includeSize;
    }

    /**
     * 绘制函数
     */
    public void onDraw(CanvasLayout canvasLayout, Canvas canvas, Paint paint) {
        this.layoutRef = new WeakReference<>(canvasLayout);
        if (!this.visible) {
            return;
        }
        canvas.save();
        canvas.translate(this.getLayoutX() * VitualDom.getDensity(), this.getLayoutY() * VitualDom.getDensity());
        canvas.rotate(this.degrees, this.getLayoutWidth() / 2f, this.getLayoutHeight() / 2f);
        int oldAlpha = paint.getAlpha();
        int newAlpha = forceSelfAlpha ? alpha : (int) (oldAlpha / (float) 0xff * alpha);
        paint.setAlpha(newAlpha);
        /**
         * TODO shadow alpha
         // draw self
         if (this.shadow != null) {
         ShadowTools.drawShadow(this.prop.shadow, bounds, paint, canvas);
         }
         **/
        //background color
        if (this.backgroundColor != 0 || this.lineWeight > 0) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(this.backgroundColor);
            RectF rectF = new RectF(0, 0, this.getLayoutWidth() * VitualDom.getDensity(), this.getLayoutHeight() * VitualDom.getDensity());
            Path path = null;
            if (this.backgroundCorner > 0) {
                if (this.backgroundCornerSpec != null && this.backgroundCornerSpec.length == 4) {
                    path = PathTools.createRoundedRect(
                            rectF.left, rectF.top, rectF.right, rectF.bottom,
                            this.backgroundCorner * VitualDom.getDensity(), this.backgroundCorner * VitualDom.getDensity(),
                            this.backgroundCornerSpec[0], this.backgroundCornerSpec[1], this.backgroundCornerSpec[2], this.backgroundCornerSpec[3], false);
                    canvas.drawPath(path, paint);
                } else {
                    canvas.drawRoundRect(rectF, this.backgroundCorner * VitualDom.getDensity(),
                            this.backgroundCorner * VitualDom.getDensity(), paint);
                }
            } else {
                canvas.drawRect(rectF, paint);
            }
            if (this.lineWeight > 0) {
                float strokeWidth = this.lineWeight * VitualDom.getDensity();
                RectF lineRect = new RectF(rectF);
                switch (lineStyle) {
                    case MIDDLE:
                        break;
                    case OUTSIDE:
                        lineRect.inset(-strokeWidth / 2f, -strokeWidth / 2f);
                        if (this.backgroundCornerSpec != null && this.backgroundCornerSpec.length == 4) {
                            path = PathTools.createRoundedRect(
                                    lineRect.left, lineRect.top, lineRect.right, lineRect.bottom,
                                    this.backgroundCorner * VitualDom.getDensity(), this.backgroundCorner * VitualDom.getDensity(),
                                    this.backgroundCornerSpec[0], this.backgroundCornerSpec[1],
                                    this.backgroundCornerSpec[2], this.backgroundCornerSpec[3], true);
                        }
                        break;
                    case INSIDE:
                        lineRect.inset(strokeWidth / 2f, strokeWidth / 2f);
                        if (this.backgroundCornerSpec != null && this.backgroundCornerSpec.length == 4) {
                            path = PathTools.createRoundedRect(
                                    lineRect.left, lineRect.top, lineRect.right, lineRect.bottom,
                                    this.backgroundCorner * VitualDom.getDensity(), this.backgroundCorner * VitualDom.getDensity(),
                                    this.backgroundCornerSpec[0], this.backgroundCornerSpec[1],
                                    this.backgroundCornerSpec[2], this.backgroundCornerSpec[3], true);
                        }
                        break;
                }
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(this.lineColor);
                paint.setStrokeWidth(strokeWidth);
                if (this.backgroundCorner > 0) {
                    if (this.backgroundCornerSpec != null && this.backgroundCornerSpec.length == 4) {
                        canvas.drawPath(path, paint);
                    } else {
                        canvas.drawRoundRect(lineRect, this.backgroundCorner * VitualDom.getDensity(),
                                this.backgroundCorner * VitualDom.getDensity(), paint);
                    }
                } else {
                    canvas.drawRect(lineRect, paint);
                }
            }
            // reset alpha
            paint.setAlpha(newAlpha);
        }

        drawInherit(canvasLayout, canvas, paint);
        // animate
        if (this.onAnimationUpdateListener != null) {
            boolean isFinish = this.onAnimationUpdateListener.onUpdate(this, this.data);
            if (!isFinish) {
                canvasLayout.postInvalidateDelayed(16);
            }
        }
        //
        if (VitualDom.GLOBAL_DEBUG) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1 * VitualDom.getDensity());
            paint.setColor(0x88ff0000);
            RectF rectF = new RectF(0, 0, this.getLayoutWidth() * VitualDom.getDensity(),
                    this.getLayoutHeight() * VitualDom.getDensity());
            canvas.drawRect(rectF, paint);
            paint.setAlpha(newAlpha);
        }
        // draw children
        for (int i = 0; i < getChildCount(); i++) {
            ((ViewNode) getChildAt(i)).onDraw(canvasLayout, canvas, paint); // only root has layout ref
        }
        canvas.restore();
        paint.setAlpha(oldAlpha);
    }

    protected void drawInherit(CanvasLayout canvasLayout, Canvas canvas, Paint paint) {

    }

    public boolean dispatchTouchEvent(MotionEvent event, float parentLeft, float parentTop) {
        boolean result = false;
        if (!visible) {
            return result;
        }
        float localX = event.getX() - parentLeft;
        float localY = event.getY() - parentTop;
        float thisLeft = this.getLayoutX() * VitualDom.getDensity();
        float thisTop = this.getLayoutY() * VitualDom.getDensity();
        boolean isContain = localX >= thisLeft && localX <= (this.getLayoutX() + this.getLayoutWidth()) * VitualDom.getDensity()
                && localY >= thisTop && localY <= (this.getLayoutY() + this.getLayoutHeight()) * VitualDom.getDensity();
        for (int i = getChildCount() - 1; i >= 0; i--) {
            ViewNode child = (ViewNode) getChildAt(i);
            if (event.getAction() != MotionEvent.ACTION_CANCEL) {
                if (!isContain) {
                    continue;
                }
            }
            if (result = child.dispatchTouchEvent(event, parentLeft + thisLeft, parentTop + thisTop)) {
                break;
            }
        }
        if (!result) {
            boolean touchHandled = false;
            if (isContain) {
                if (this.onTouchListener != null) {
                    touchHandled = this.onTouchListener.onTouch(this, event, this.data);
                }
                if (this.onClickListener != null || this.onLongClickListener != null) {
                    result = true;
                }
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // reset state
                    isPressed = false;
                    isMoved = false;
                    oldDownRawX = event.getRawX();
                    oldDownRawY = event.getRawY();
                    //
                    if (isContain) {
                        isPressed = true;
                        VitualDom.removeCallbacksMainThread(checkLongClickRunnable);
                        VitualDom.postMainThreadDelay(checkLongClickRunnable, 750);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isPressed) break; // only the pressed one handle this event
                    if (!isMoved) {
                        boolean isSticky = isSticky(event.getRawX(), event.getRawY(), oldDownRawX, oldDownRawY);
                        boolean isInScrollingContainer = isInScrollingContainer();
                        boolean isValid = isInScrollingContainer ? isSticky && isContain : isContain;
                        if (!isValid) {
                            isMoved = true;
                            VitualDom.removeCallbacksMainThread(checkLongClickRunnable);
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    if (!isPressed) break; // only the pressed one handle this event
                    VitualDom.removeCallbacksMainThread(checkLongClickRunnable);
                    break;
                case MotionEvent.ACTION_UP:
                    if (!isPressed) break; // only the pressed one handle this event
                    VitualDom.removeCallbacksMainThread(checkLongClickRunnable);
                    boolean isSticky = isSticky(event.getRawX(), event.getRawY(), oldDownRawX, oldDownRawY);
                    boolean isInScrollingContainer = isInScrollingContainer();
                    boolean isValid = isInScrollingContainer ? isSticky && isContain : isContain;
                    if (isValid) {
                        if (this.onClickListener != null) {
                            this.onClickListener.onClick(this, this.data);
                        }
                    }
                    isPressed = false;
                    isMoved = false;
                    break;
            }
            result |= touchHandled;
        }
        return result;
    }

    public void callYoga() {
        if (isMeasureDefined()) {
            dirty();
        }
        CanvasLayout canvasLayout = layoutRef != null ? layoutRef.get() : null;
        if (canvasLayout != null) {
            canvasLayout.requestLayout(); // do real cal
        } else {
            ViewNode node = (ViewNode) getParent();
            if (node != null) {
                node.callYoga();
            }
        }
    }

    public void invalidate(){
        CanvasLayout canvasLayout = layoutRef != null ? layoutRef.get() : null;
        if (canvasLayout != null) {
            canvasLayout.invalidate(); // do real cal
        } else {
            ViewNode node = (ViewNode) getParent();
            if (node != null) {
                node.invalidate();
            }
        }
    }

    public Context getContext() {
        CanvasLayout canvasLayout = layoutRef != null ? layoutRef.get() : null;
        if (canvasLayout != null) {
            return canvasLayout.getContext();
        } else {
            ViewNode node = (ViewNode) getParent();
            return node != null ? node.getContext() : null;
        }
    }

    public void onAttachedToWindow() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            ViewNode child = (ViewNode) getChildAt(i);
            child.onAttachedToWindow();
        }
    }

    public void onDetachedFromWindow() {
        VitualDom.removeCallbacksMainThread(checkLongClickRunnable);
        for (int i = getChildCount() - 1; i >= 0; i--) {
            ViewNode child = (ViewNode) getChildAt(i);
            child.onDetachedFromWindow();
        }
    }

    private Runnable checkLongClickRunnable = new Runnable() {
        @Override
        public void run() {
            if (ViewNode.this.onLongClickListener != null) {
                ViewNode.this.onLongClickListener.onLongClick(ViewNode.this, ViewNode.this.data);
            }
            isPressed = false;
            isMoved = false;
        }
    };

    public static boolean isSticky(float upX, float upY, float downX, float downY) {
        return upX > downX - CLICK_RANGE && upX < downX + CLICK_RANGE && upY > downY - CLICK_RANGE && upY < downY + CLICK_RANGE;
    }

    private boolean isInScrollingContainer() {
        CanvasLayout canvasLayout = layoutRef != null ? layoutRef.get() : null;
        return canvasLayout != null ? canvasLayout.isInScrollingContainer : true;
    }


    /**
     * yogaNode扩展
     */
    public void addChild(ViewNode child) {
        addChildAt(child, getChildCount());
    }

    public <E extends ViewNode> E findViewById(String id) {
        if (id.equals(this.id)) {
            return (E) this;
        }
        // find in children
        for (int i = 0; i < getChildCount(); i++) {
            E result = ((ViewNode) getChildAt(i)).findViewById(id);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public Location getLocationInRootNode() {
        YogaNode yogaNode = this;
        float x = yogaNode.getLayoutX();
        float y = yogaNode.getLayoutY();
        while (yogaNode.getParent() != null) {
            yogaNode = yogaNode.getParent();
            x += yogaNode.getLayoutX();
            y += yogaNode.getLayoutY();
        }

        return new Location(x, y);
    }

    public class Location {
        public float x;
        public float y;

        public Location(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public static final int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;
}
