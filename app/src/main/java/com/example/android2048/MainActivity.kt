package com.example.android2048

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
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
import com.example.android2048.ui.theme.Android2048Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Android2048Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Screen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Screen(modifier: Modifier = Modifier) {
    val gameLogic = remember { GameLogic() }
    val context = LocalContext.current
    var bestScore = readScoreToJson(context)

    Box(modifier.fillMaxWidth()) {
        Image(
            painterResource(id = R.drawable.frame),
            contentDescription = "Frame",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(12.dp)
                .pointerInput(Unit){
                    detectDragGestures(
                        onDragEnd = {
                            bestScore = max(bestScore, gameLogic.totalScore)
                            if (gameLogic.isGameOver()) {
                                gameLogic.reset()
                                CoroutineScope(Dispatchers.IO).launch {
                                    saveScoreToJson(context, gameLogic.totalScore)
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            val threshold = 64f
                            val (dx, dy) = dragAmount
                            if (abs(dx) > abs(dy) && abs(dx) > threshold) {
                                println("dx: $dx")
                                println("dy: $dx")
                                if (dx > 0) {
                                    gameLogic.swipeRight()
                                    println("swiped right")
                                } else {
                                    gameLogic.swipeLeft()
                                    println("swiped left")
                                }
                            } else if (abs(dy) > threshold) {
                                if (dy > 0) {
                                    gameLogic.swipeDown()
                                    println("swiped down")
                                } else {
                                    gameLogic.swipeUp()
                                    println("swiped up")
                                }
                            }
                        }
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row(modifier = Modifier.fillMaxWidth())
        {
            Text(
                "Score: ${gameLogic.totalScore}",
                fontSize = 24.sp,
                color = Color(0xFF6312FF),
                modifier = Modifier.padding(16.dp))
        }
        Row(modifier = Modifier.fillMaxWidth()){
            Text(
                "Best Score: $bestScore",
                fontSize = 24.sp,
                color = Color(0xFF6312FF),
                modifier = Modifier.padding(16.dp)
            )
        }
            BoardView(board = gameLogic.board)
            Button(onClick = {gameLogic.reset()},
                modifier = modifier.padding(8.dp)) {
                Text("Restart")
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
