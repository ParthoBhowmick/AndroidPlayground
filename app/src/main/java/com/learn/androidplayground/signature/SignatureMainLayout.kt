package com.learn.androidplayground.signature

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min

class SignatureMainLayout(context: Context?) : LinearLayout(context), View.OnClickListener {
    var buttonsLayout: LinearLayout
    var signatureView: SignatureView

    init {
        this.orientation = VERTICAL

        this.buttonsLayout = this.buttonsLayout()
        this.signatureView = SignatureView(context)


        // add the buttons and signature views
        this.addView(this.buttonsLayout)
        this.addView(signatureView)
    }

    private fun buttonsLayout(): LinearLayout {
        // create the UI programatically

        val linearLayout = LinearLayout(this.context)
        val clearBtn = Button(this.context)


        // set orientation
        linearLayout.orientation = HORIZONTAL


        clearBtn.text = "Clear"
        clearBtn.tag = "Clear"
        clearBtn.setOnClickListener(this)

        linearLayout.addView(clearBtn)


        // return the whoe layout
        return linearLayout
    }

    // the on click listener of 'save' and 'clear' buttons
    override fun onClick(v: View) {
        val tag = v.tag.toString().trim { it <= ' ' }


        // save the signature
        if (tag.equals("save", ignoreCase = true)) {
            this.saveImage(signatureView.signature)
        } else {
            signatureView.clearSignature()
        }
    }

    /**
     * save the signature to an sd card directory
     * @param signature bitmap
     */
    fun saveImage(signature: Bitmap) {
        val root = Environment.getExternalStorageDirectory().toString()


        // the directory where the signature will be saved
        val myDir = File("$root/saved_signature")


        // make the directory if it does not exist yet
        if (!myDir.exists()) {
            myDir.mkdirs()
        }


        // set the file name of your choice
        val fname = "signature.png"


        // in our case, we delete the previous file, you can remove this
        val file = File(myDir, fname)
        if (file.exists()) {
            file.delete()
        }

        try {
            // save the signature

            val out = FileOutputStream(file)
            signature.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()

            Toast.makeText(this.context, "Signature saved.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * The View where the signature will be drawn
     */
    inner class SignatureView(context: Context?) : View(context) {
        private val paint = Paint()
        private val path = Path()

        private var lastTouchX = 0f
        private var lastTouchY = 0f
        private val dirtyRect = RectF()

        init {
            paint.isAntiAlias = true
            paint.color = Color.BLACK
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeWidth = Companion.STROKE_WIDTH


            // set the bg color as white
            this.setBackgroundColor(Color.WHITE)


            // width and height should cover the screen
            this.layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }

        val signature: Bitmap
            /**
             * Get signature
             *
             * @return
             */
            get() {
                var signatureBitmap: Bitmap? = null


                // set the signature bitmap
                if (signatureBitmap == null) {
                    signatureBitmap = Bitmap.createBitmap(
                        this.width,
                        this.height, Bitmap.Config.RGB_565
                    )
                }


                // important for saving signature
                val canvas = Canvas(signatureBitmap)
                this.draw(canvas)

                return signatureBitmap
            }

        /**
         * clear signature canvas
         */
        fun clearSignature() {
            path.reset()
            this.invalidate()
        }

        // all touch events during the drawing
        override fun onDraw(canvas: Canvas) {
            canvas.drawPath(this.path, this.paint)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val eventX = event.x
            val eventY = event.y

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    path.moveTo(eventX, eventY)

                    lastTouchX = eventX
                    lastTouchY = eventY
                    return true
                }

                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                    resetDirtyRect(eventX, eventY)
                    val historySize = event.historySize
                    var i = 0
                    while (i < historySize) {
                        val historicalX = event.getHistoricalX(i)
                        val historicalY = event.getHistoricalY(i)

                        expandDirtyRect(historicalX, historicalY)
                        path.lineTo(historicalX, historicalY)
                        i++
                    }
                    path.lineTo(eventX, eventY)
                }

                else -> return false
            }
            invalidate(
                (dirtyRect.left - Companion.HALF_STROKE_WIDTH).toInt(),
                (dirtyRect.top - Companion.HALF_STROKE_WIDTH).toInt(),
                (dirtyRect.right + Companion.HALF_STROKE_WIDTH).toInt(),
                (dirtyRect.bottom + Companion.HALF_STROKE_WIDTH).toInt()
            )

            lastTouchX = eventX
            lastTouchY = eventY

            return true
        }

        private fun expandDirtyRect(historicalX: Float, historicalY: Float) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY
            }
        }

        private fun resetDirtyRect(eventX: Float, eventY: Float) {
            dirtyRect.left = min(lastTouchX.toDouble(), eventX.toDouble()).toFloat()
            dirtyRect.right = max(lastTouchX.toDouble(), eventX.toDouble()).toFloat()
            dirtyRect.top = min(lastTouchY.toDouble(), eventY.toDouble()).toFloat()
            dirtyRect.bottom = max(lastTouchY.toDouble(), eventY.toDouble()).toFloat()
        }
    }
    companion object {
        // set the stroke width
        private const val STROKE_WIDTH = 7f
        private const val HALF_STROKE_WIDTH = STROKE_WIDTH / 2
    }
}