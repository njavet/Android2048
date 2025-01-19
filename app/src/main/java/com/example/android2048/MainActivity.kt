package com.example.android2048

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import kotlin.math.abs


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameScreen()
        }
    }
}

@Composable
fun GameScreen() {
    val gameLogic = remmeber { GameLogic() }
    var board by remember { mutableStateOf(gameLogic.board)}
    var score by remember { mutableStateOf(gameLogic.totalScore)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .pointerInput(Unit){
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val (dx, dy) = dragAmount
                    if (abs(dx) > abs(dy)) {
                        if (dx > 0) {
                            gameLogic.swipeRight()
                        } else {
                            gameLogic.swipeLeft()
                        }
                    } else {
                        if (dy > 0) {
                            gameLogic.swipeDown()
                        } else {
                            gameLogic.swipeUp()
                        }
                    }
                    board = gameLogic.board
                    score = gameLogic.totalScore
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Score: $score", fontSize = 24.sp, modifier = Modifier.padding(16.dp))
        BoardView(board = board)
    }
}

@Composable
fun BoardView(board: Array<IntArray>) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (row in board) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (cell in row) {
                    CellView(value = cell)
                }
            }
        }
    }
}

@Composable
fun CellView(value: Int) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(if (value == 0) Color.Gray else Color.Yellow),
        contentAlignment = Alignment.Center
    ) {
        BasicText(text = if (value == 0) "" else value.toString(), fontSize = 24.sp)
    }
}
