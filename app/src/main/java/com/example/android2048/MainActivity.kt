package com.example.android2048

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.android2048.ui.theme.Android2048Theme

class MainActivity : ComponentActivity() {
    private lateinit var gameLogic: GameLogic
    private lateinit var gameBoard: GridLayout
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        gameLogic = GameLogic()

        gestureDetector = GestureDetector(this, GestureListener())

        drawBoard()
    }

    private fun drawBoard() {
        gameBoard.removeAllViews()
        for (r in 0 until gameLogic.board.size) {
            for (c in 0 until gameLogic.board[r].size) {
                val value = gameLogic.board[r][c]
                val tile = TextView(this).apply {
                    text = if (value > 0) value.toString() else ""
                    textSize = 24f
                    setPadding(16, 16, 16, 16)
                }
                gameBoard.addView(tile)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val swipeThreshold = 100
        private val swipeVelocityThreshold = 100

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1 == null || e2 == null) return false
            val diffX = e2.x - e1.x
            val diffY = e2.y - e1.y
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > swipeThreshold && Math.abs(velocityX) > swipeVelocityThreshold) {
                    if (diffX > 0) {
                        gameLogic.swipeRight()
                    } else {
                        gameLogic.swipeLeft()
                    }
                    drawBoard()
                    return true
                }
            } else {
                if (Math.abs(diffY) > swipeThreshold && Math.abs(velocityY) > swipeVelocityThreshold) {
                    if (diffY > 0) {
                        gameLogic.swipeDown()
                    } else {
                        gameLogic.swipeUp()
                    }
                    drawBoard()
                    return true
                }
            }
            return false
        }
    }
}