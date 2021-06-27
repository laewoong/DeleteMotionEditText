package com.laewoong.deletemotionedittext;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.laewoong.deletemotionedittext.particle.Particle;
import com.laewoong.deletemotionedittext.particle.ParticleFactory;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DeleteMotionEditText extends AppCompatEditText implements View.OnTouchListener{

    public interface OnEndDeleteMotionListener {
        void onEndDeleteMotion();
    }

    private Paint particlePaint;
    private Shader textShader;
    private OnEndDeleteMotionListener callback;
    private boolean isTextMotionStart;
    private boolean isParticleMotionStart;
    private float visibleArea;
    private int viewWidth;
    private float inputedTextWidth;
    private float inputedTextHeight;
    private int inputedTextHalfHeight;
    private int editTextViewHalfHeight;
    private String curInputedString;
    private ParticleFactory particleFactory;
    private int originalTextColor;
    private List<Particle> particleList = new LinkedList<>();
    private Drawable deleteIconDrawable;
    private boolean deleteIconDrawableVisible;
    private int drawableAlpha;
    private ObjectAnimator drawableAlphaAnimator;

    public DeleteMotionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        particlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        isTextMotionStart = false;
        isParticleMotionStart = false;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);

        deleteIconDrawableVisible = false;
        deleteIconDrawable = getCompoundDrawables()[2];
        deleteIconDrawable.setAlpha(0);
        deleteIconDrawable.setBounds(0, 0, 50, 50);
        setCompoundDrawables(null, null, null, null);

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setDeleteIconDrawableVisible((s.length() > 0));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setDeleteIconDrawableVisible(boolean isVisible) {

        if(deleteIconDrawableVisible == isVisible) {
            return;
        }

        deleteIconDrawableVisible = isVisible;

        if(drawableAlphaAnimator != null) {
            drawableAlphaAnimator.cancel();
        }

        if(isVisible) {
            setCompoundDrawables(null, null, deleteIconDrawable, null);
            drawableAlphaAnimator = ObjectAnimator.ofInt(this, "drawableAlpha", deleteIconDrawable.getAlpha(), 255);
            setOnTouchListener(this);
        }
        else {
            drawableAlphaAnimator = ObjectAnimator.ofInt(this, "drawableAlpha", deleteIconDrawable.getAlpha(), 0);
            setOnTouchListener(null);
        }

        drawableAlphaAnimator.setDuration(300);
        drawableAlphaAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                drawableAlphaAnimator = null;
            }
        });

        drawableAlphaAnimator.start();
    }

    public void setDrawableAlpha(int alpha) {
        drawableAlpha = alpha;
        deleteIconDrawable.setAlpha(drawableAlpha);
    }

    public int getDrawableAlpha() {
        return drawableAlpha;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        return parcelable;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if(getText().length() > 0) {
            deleteIconDrawable.setAlpha(255);
        }
    }
int count = 0;
    @Override
    protected void onDraw(Canvas canvas) {

        if(isTextMotionStart == true) {
            getPaint().setShader(textShader);
            super.onDraw(canvas);
            getPaint().setShader(null);
        }
        else {
            super.onDraw(canvas);
        }

        if(isParticleMotionStart == true) {

            if(isTextMotionStart || (inputedTextWidth < viewWidth)) {
                for(Particle particle: particleList) {
                    particlePaint.setAlpha((int)(particle.opacity*100));
                    canvas.drawCircle(particle.x, particle.y, particle.r, particlePaint);
                }
            }
            else {
                int x= 0;
                for(Particle particle: particleList) {
                    particlePaint.setAlpha((int)(particle.opacity*100));
                    x = particle.x-particle.originalX;
                    canvas.drawCircle(x, particle.y, particle.r, particlePaint);
                }
            }

            if(particleList.isEmpty()) {
                isParticleMotionStart = false;

                particleList.clear();

                if(callback != null) {
                    callback.onEndDeleteMotion();
                }

            } else {
                if(isTextMotionStart == false) {
                    moveParticle();
                    invalidate();
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;

        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(event.getRawX() >= (DeleteMotionEditText.this.getRight() - DeleteMotionEditText.this.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                startDeleteMotion();
                return true;
            }
        }
        return false;
    }

    public void setOnEndDeleteMotionListener(OnEndDeleteMotionListener listener) {
        this.callback = listener;
    }



    public void setVisibleArea(float area) {
        visibleArea = area;

        int areaInt = (int) area;
        particleList.add(particleFactory.createSineMoveParticle(areaInt));
        particleList.add(particleFactory.createSineMoveParticle(areaInt));
        particleList.add(particleFactory.createLinearMoveParticle(areaInt));
        particleList.add(particleFactory.createLinearMoveParticle(areaInt));

        moveParticle();
        makeAlpahShader(area);
        invalidate();
    }

    private void moveParticle() {
        for(Particle particle: particleList) {
           particle.onMove();
        }

        // Clear invalid particles.
        Iterator<Particle> i = particleList.iterator();
        while (i.hasNext()) {
            Particle particle = i.next();
            if(particle.opacity <= 0f || particle.r <= 0f) {
                i.remove();
                particle.recycle();
            }
        }

    }

    private void makeAlpahShader(float width) {
        int[] gradient = {originalTextColor, originalTextColor&0x00FFFFFF};
        float[] position = {width/inputedTextWidth-0.1f, width/inputedTextWidth};
        textShader = new LinearGradient(0, 0, inputedTextWidth, inputedTextHeight, gradient,
                position, Shader.TileMode.CLAMP);
    }

    public float getVisibleArea() {
        return visibleArea;
    }

    public void startDeleteMotion() {

        if(isTextMotionStart == false && isParticleMotionStart == false) {

            setActivated(false);
            originalTextColor = getPaint().getColor();
            particlePaint.setColor(originalTextColor);
            curInputedString = getText().toString();

            Rect bounds = new Rect();
            Paint textPaint = this.getPaint();
            textPaint.getTextBounds(curInputedString,0,curInputedString.length(),bounds);

            inputedTextHeight = bounds.height();
            inputedTextWidth = bounds.width();
            inputedTextHalfHeight = (int)(inputedTextHeight / 2);
            editTextViewHalfHeight = (getMeasuredHeight() / 2);

            float endValue = 0;
            viewWidth = getMeasuredWidth();
            if(viewWidth < inputedTextWidth) {
                endValue = inputedTextWidth - viewWidth+100;
            }

            int totalTime = (viewWidth < inputedTextWidth)? viewWidth : (int)inputedTextWidth;

            Rect rectf = new Rect();
            this.getLocalVisibleRect(rectf);
            particleFactory = new ParticleFactory(endValue, editTextViewHalfHeight, inputedTextHalfHeight);

            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "visibleArea", inputedTextWidth, endValue);
            animator.setDuration(totalTime*10/2);
            animator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    clearFocus();
                    setCursorVisible(false);
                    isTextMotionStart = true;
                    isParticleMotionStart = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    isTextMotionStart = false;
                    setText("");
                    setActivated(true);
                    setCursorVisible(true);
                    requestFocus();
                }
            });

            animator.start();
        }
    }
}