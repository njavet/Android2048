package com.example.android2048

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Screen()
        }
    }
}

@Composable
fun Screen(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth()) {
        Image(
            painterResource(id = R.drawable.frame),
            contentDescription = "Frame",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight,
        )
        GameScreen()
    }
}

@Composable
fun GameScreen() {
    val gameLogic = remember { GameLogic() }
    val context = LocalContext.current
    val scoreData = readScoreToJson(context, "game_score.json")
    val score = scoreData?.get("score") as? Int ?: 0
    println("score: $score")
    gameLogic.totalScore = score
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .pointerInput(Unit){
                detectDragGestures(
                    onDragEnd = {
                        if (gameLogic.isGameOver()) {
                            val filePath = "/home/tosh/0x100/game_score.json"
                            CoroutineScope(Dispatchers.IO).launch {
                                saveScoreToJson(context, gameLogic.totalScore, filePath)

                            }
                        } else {
                            println("swiped")
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val threshold = 32f
                        val (dx, dy) = dragAmount
                        if (abs(dx) > abs(dy) && abs(dx) > threshold) {
                            if (dx > 0) {
                                gameLogic.swipeRight()
                            } else {
                                gameLogic.swipeLeft()
                            }
                        } else if (abs(dy) > threshold) {
                            if (dy > 0) {
                                gameLogic.swipeDown()
                            } else {
                                gameLogic.swipeUp()
                            }
                        }
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Score: ${gameLogic.totalScore}", fontSize = 24.sp, modifier = Modifier.padding(16.dp))
        Text("Best Score: ${gameLogic.totalScore}", fontSize = 24.sp, modifier = Modifier.padding(16.dp))
        BoardView(board = gameLogic.board)

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                gameLogic.swipeUp()
            }) {
                Text("swipe up")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    gameLogic.swipeLeft()
                }) {
                    Text("swipe left")
                }
                Button(onClick = {
                    gameLogic.swipeRight()
                }) {
                    Text("swipe right")
                }
            }
            Button(onClick = {
                gameLogic.swipeDown()
            }) {
                Text("swipe down")
            }
        }
    }
}

@Composable
fun BoardView(board: List<MutableList<Int>>) {
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
    val backgroundColor = tileColors[value] ?: Color.Black // Default to black if value isn't in the map

    Box(
        modifier = Modifier
            .size(80.dp)
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (value != 0) {
            Text(
                text = value.toString(),
                fontSize = 24.sp,
                color = Color.White
            )
        }
    }
}

val tileColors = mapOf(
    0 to Color(0xFFF3E5F5),  // Light purple
    2 to Color(0xFFE1BEE7),
    4 to Color(0xFFCE93D8),
    8 to Color(0xFFBA68C8),
    16 to Color(0xFFAB47BC),
    32 to Color(0xFF9C27B0),
    64 to Color(0xFF8E24AA),
    128 to Color(0xFF7B1FA2),
    256 to Color(0xFF6A1B9A),
    512 to Color(0xFF4A148C), // Dark purple
    1024 to Color(0xFF38006B), // Even darker purple
    2048 to Color(0xFF260044),
    4096 to Color(0xFF6312FF)// Blackish purple for extreme values
)
