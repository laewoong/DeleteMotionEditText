package com.laewoong.deletemotionedittext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.os.Parcelable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import com.laewoong.deletemotionedittext.particle.ParticleManager
import java.util.*

class DeleteMotionEditText: AppCompatEditText, OnTouchListener {

    fun interface OnEndDeleteMotionListener {
        fun onEndDeleteMotion()
    }

    private var callback: OnEndDeleteMotionListener? = null

    private val particlePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textShader: Shader? = null

    private val particleManager by lazy { ParticleManager() }
    private var isTextMotionInProgress = false
    private var isParticleMotionInProgress = false

    var visibleArea = 0f
        set(area) {
            field = area
            val areaInt = area.toInt()
            particleManager.createSineMoveParticle(areaInt)
            particleManager.createSineMoveParticle(areaInt)
            particleManager.createLinearMoveParticle(areaInt)
            particleManager.createLinearMoveParticle(areaInt)
            particleManager.moveParticle()
            invalidateAlphaShader(area)
            invalidate()
        }
    private var viewWidth = 0
    private var inputtedTextWidth = 0f
    private var inputtedTextHeight = 0f
    private var inputtedTextHalfHeight = 0
    private var editTextViewHalfHeight = 0
    private var curInputtedString: String? = null
    private var originalTextColor = 0
    private var deleteIconDrawable = compoundDrawables[2]
    private var deleteIconDrawableVisible = false
    var drawableAlpha = 0
        set(alpha) {
            field = alpha
            deleteIconDrawable.alpha = drawableAlpha
        }
    private var drawableAlphaAnimator: ObjectAnimator? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        isFocusable = true
        isFocusableInTouchMode = true
        setSingleLine()
        ellipsize = TextUtils.TruncateAt.END
        deleteIconDrawableVisible = false
        deleteIconDrawable.alpha = 0
        deleteIconDrawable.setBounds(0, 0, 50, 50)
        setCompoundDrawables(null, null, null, null)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setDeleteIconDrawableVisible(s.isNotEmpty())
            }
        })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        disableClipOnParents(this)
        (parent as? View)?.setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private fun disableClipOnParents(view: View?) {
        if (view == null) return

        (view as? ViewGroup)?.let {
            it.clipChildren = false
        }

        disableClipOnParents(view.parent as? View)
    }

    private fun setDeleteIconDrawableVisible(isVisible: Boolean) {
        if (deleteIconDrawableVisible == isVisible) {
            return
        }
        deleteIconDrawableVisible = isVisible
        drawableAlphaAnimator?.cancel()

        if (isVisible) {
            setCompoundDrawables(null, null, deleteIconDrawable, null)
            drawableAlphaAnimator =
                ObjectAnimator.ofInt(this, "drawableAlpha", deleteIconDrawable.alpha, 255)
            setOnTouchListener(this)
        } else {
            drawableAlphaAnimator =
                ObjectAnimator.ofInt(this, "drawableAlpha", deleteIconDrawable.alpha, 0)
            setOnTouchListener(null)
        }
        drawableAlphaAnimator?.duration = 300
        drawableAlphaAnimator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                drawableAlphaAnimator = null
            }
        })
        drawableAlphaAnimator?.start()
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        super.onRestoreInstanceState(state)
        text?.let {
            if (it.isNotEmpty()) {
                deleteIconDrawable.alpha = 255
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (isTextMotionInProgress) {
            paint.shader = textShader
            super.onDraw(canvas)
            paint.shader = null
        } else {
            super.onDraw(canvas)
        }

        if (isParticleMotionInProgress) {
            if (isTextMotionInProgress || inputtedTextWidth < viewWidth) {
                particleManager.particleList.forEach { particle ->
                    particlePaint.alpha = (particle.opacity * 100).toInt()
                    canvas.drawCircle(
                        particle.x.toFloat(),
                        particle.y.toFloat(),
                        particle.r,
                        particlePaint
                    )
                }
            } else {
                particleManager.particleList.forEach { particle ->
                    particlePaint.alpha = (particle.opacity * 100).toInt()
                    canvas.drawCircle(
                        (particle.x - particle.originalX).toFloat(),
                        particle.y.toFloat(),
                        particle.r,
                        particlePaint
                    )
                }
            }
            if (particleManager.particleList.isEmpty()) {
                isParticleMotionInProgress = false
                particleManager.clearParticle()
                callback?.onEndDeleteMotion()
            } else {
                if (isTextMotionInProgress == false) {
                    particleManager.moveParticle()
                    invalidate()
                }
            }
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val DRAWABLE_LEFT = 0
        val DRAWABLE_TOP = 1
        val DRAWABLE_RIGHT = 2
        val DRAWABLE_BOTTOM = 3
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= this@DeleteMotionEditText.right - this@DeleteMotionEditText.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                startDeleteMotion()
                return true
            }
        }
        return false
    }

    fun setOnEndDeleteMotionListener(listener: OnEndDeleteMotionListener?) {
        callback = listener
    }

    private fun invalidateAlphaShader(visibleWidth: Float) {
        val transparentGradient = intArrayOf(originalTextColor, originalTextColor and 0x00FFFFFF)
        val transparentGradientWidth =
            floatArrayOf(visibleWidth / inputtedTextWidth - 0.1f, visibleWidth / inputtedTextWidth)
        textShader = LinearGradient(
            0f, 0f, inputtedTextWidth, inputtedTextHeight, transparentGradient,
            transparentGradientWidth, Shader.TileMode.CLAMP
        )
    }

    private fun startDeleteMotion() {
        if (isTextMotionInProgress == false && isParticleMotionInProgress == false) {
            isActivated = false
            originalTextColor = paint.color
            particlePaint.color = originalTextColor
            curInputtedString = text.toString()
            val bounds = Rect()
            val textPaint: Paint = paint
            textPaint.getTextBounds(curInputtedString, 0, curInputtedString!!.length, bounds)
            inputtedTextHeight = bounds.height().toFloat()
            inputtedTextWidth = bounds.width().toFloat()
            inputtedTextHalfHeight = (inputtedTextHeight / 2).toInt()
            editTextViewHalfHeight = measuredHeight / 2
            var endValue = 0f
            viewWidth = measuredWidth
            if (viewWidth < inputtedTextWidth) {
                endValue = inputtedTextWidth - viewWidth + 100
            }
            val totalTime =
                if (viewWidth < inputtedTextWidth) viewWidth else inputtedTextWidth.toInt()
            val rectf = Rect()
            getLocalVisibleRect(rectf)
            particleManager.updateBound(endValue, editTextViewHalfHeight, inputtedTextHalfHeight)

            val animator = ObjectAnimator.ofFloat(this, "visibleArea", inputtedTextWidth, endValue)
            animator.duration = (totalTime * 10 / 2).toLong()
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                    clearFocus()
                    isCursorVisible = false
                    isTextMotionInProgress = true
                    isParticleMotionInProgress = true
                }

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    isTextMotionInProgress = false
                    setText("")
                    isActivated = true
                    isCursorVisible = true
                    requestFocus()
                }
            })
            animator.start()
        }
    }
}