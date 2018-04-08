package ge.dev.baqari.imagetextview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class ImageTextView(context: Context?, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    private var textSize: Float? = null
    private var textColor: Int? = null
    private var text: String? = null
    private var textVerticalGravity: Int? = null
    private var textHorizontalGravity: Int? = null
    private val paint: Paint by lazy { Paint() }
    private val sizeRect: Rect by lazy { Rect() }
    private var viewHeight: Int? = null

    private var xPos = 0f
    private var yPos = 0f

    init {
        val attributes = context?.obtainStyledAttributes(attrs, R.styleable.ImageTextView, 0, 0)

        textSize = attributes?.getDimension(R.styleable.ImageTextView_textSize, 10f)
        textColor = attributes?.getColor(R.styleable.ImageTextView_textColor, Color.BLACK)
        text = attributes?.getString(R.styleable.ImageTextView_text)
        textVerticalGravity = attributes?.getInteger(R.styleable.ImageTextView_textVerticalGravity, 0)
        textHorizontalGravity = attributes?.getInteger(R.styleable.ImageTextView_textHorizontalGravity, 0)

        paint.color = textColor!!
        paint.textSize = textSize!!

        attributes?.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        paint.getTextBounds(text, 0, text?.length!!, sizeRect)

        //when gravities not set
        if (textVerticalGravity == null || textVerticalGravity == 0) {
            yPos = textSize!!
        }
        if (textHorizontalGravity == null || textHorizontalGravity == 0) {
            xPos = 5f
        }

        //check vertical gravity
        if (textVerticalGravity == CENTER) {
            yPos = (viewHeight!!.div(2f) - ((paint.descent() + paint.ascent()) / 2))
        }
        if (textVerticalGravity == TOP) {
            yPos = textSize!!
        }
        if (textVerticalGravity == BOTTOM) {
            yPos = viewHeight!! - textSize?.div(2.5f)!!
        }


        //check horizontal gravity
        if (textHorizontalGravity == LEFT) {
            xPos = 5f
        }
        if (textHorizontalGravity == CENTER) {
            xPos = canvas?.width?.div(2)?.toFloat()!! - sizeRect.width().div(2)
        }
        if (textHorizontalGravity == RIGHT) {
            xPos = canvas?.width?.toFloat()!! - sizeRect.width()
        }

        canvas?.drawText(text, xPos, yPos, paint)
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        viewHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setTextSize(size: Float) {
        paint.textSize = size.dip()
        invalidate()
    }

    fun setTextColor(color: Int) {
        paint.color = color
        invalidate()
    }

    fun setText(newText: String) {
        text = newText
        invalidate()
    }

    private fun Float.dip(): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources?.displayMetrics)

    companion object {
        const val LEFT = 1
        const val TOP = 2
        const val RIGHT = 3
        const val BOTTOM = 4
        const val CENTER = 5
    }
}
