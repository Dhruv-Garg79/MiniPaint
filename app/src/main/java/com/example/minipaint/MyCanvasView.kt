package com.example.minipaint

import android.content.Context
import android.drm.DrmStore
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat

class MyCanvasView (context: Context) : View(context){

    private lateinit var frame: Rect

    // curPath representing the drawing so far
    private val drawing = Path()

    // curPath representing what's currently being drawn
    private val curPath = Path()

    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)
    private val drawColor = ResourcesCompat.getColor(resources, R.color.colorPaint, null)

    private val inset = 40
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f

    private val paint = Paint().apply {
        color = drawColor
        strokeWidth = STROKE_WIDTH
        isAntiAlias = true // smooths out edges of what is drawn without affecting shape
        isDither = true // Dithering affects how colors with higher-precision than the device are down-sampled.
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawColor(backgroundColor)
            it.drawPath(drawing, paint)
            it.drawPath(curPath, paint)
            it.drawRect(frame, paint)
        }
        Log.d(LOG, "onDraw")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        motionTouchEventX = event?.x ?: 0f
        motionTouchEventY = event?.y ?: 0f

        when(event?.action){
            MotionEvent.ACTION_UP -> touchUp()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_DOWN -> touchStart()
        }

        return true
    }


    private fun touchStart(){
        curPath.reset()
        curPath.moveTo(motionTouchEventX, motionTouchEventY)

        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove(){
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY - currentY)

        if (dx >= touchTolerance || dy >= touchTolerance){
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            curPath.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)

            currentX = motionTouchEventX
            currentY = motionTouchEventY
        }

        invalidate()
    }

    private fun touchUp(){
        // Add the current path to the drawing so far
        drawing.addPath(curPath)
        // Reset the curPath so it doesn't get drawn again.
        curPath.reset()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        frame = Rect(inset, inset, width - inset, height - inset)
        Log.d(LOG, "onSIzeChanged")
    }

    companion object {
        private const val STROKE_WIDTH = 10f
        private const val LOG = "LOGGEDINFO"
    }

}