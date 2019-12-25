package com.example.minipaint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myCanvas = MyCanvasView(this)
        myCanvas.apply {
            systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
            contentDescription = resources.getString(R.string.canvasContentDescription)
        }
        setContentView(myCanvas)
    }
}
