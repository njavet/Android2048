package com.example.android2048

import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class GameLogic {
    private val size = 4
    var board by mutableStateOf(List(size) { MutableList(size) {0} })

    var totalScore by mutableIntStateOf(0)

    init {
        reset()
    }

    fun reset() {
        totalScore = 0
        for (row in board) {
            row.fill(0)
        }
        spawnTile()
        spawnTile()

    }

    fun isGameOver(): Boolean {
        val zeroCells = mutableListOf<Pair<Int, Int>>()
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (board[r][c] == 0)
                    zeroCells.add(r to c)
            }
        }
        if (zeroCells.isNotEmpty()) {
            return false
        }

        val (merged0, score0) = mergeLeft(board)
        if (merged0 != board) {
            return false
        }

        val (merged1, score1) = mergeDown(board)
        if (merged1 != board) {
            return false
        }
        val (merged2, score2) = mergeRight(board)
        if (merged2 != board) {
            return false
        }
        val (merged3, score3) = mergeUp(board)
        if (merged3 != board) {
            return false
        } else {
            return true
        }
    }

    private fun spawnTile() {
        val zeroCells = mutableListOf<Pair<Int, Int>>()
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (board[r][c] == 0)
                    zeroCells.add(r to c)
            }
        }

        if (zeroCells.isNotEmpty()) {
            val (r, c) = zeroCells.random()
            board[r][c] = if (Math.random() < 0.9) 2 else 4
        }
    }

    fun mergeLeft(grid: List<MutableList<Int>>): Pair<List<MutableList<Int>>, Int> {
        var mergeScore = 0
        val newBoard = grid.map { row ->
            val (merged, score) = mergeRowLeft(row.filter({x -> x != 0}).toMutableList())
            val zeroPadds = size - merged.size
            val paddedRow = (merged + List(zeroPadds) { 0 }).toMutableList()
            mergeScore += score
            paddedRow
        }
        return Pair(newBoard, mergeScore)
    }

    fun swipeLeft() {
        val (newBoard, score) = mergeLeft(board)
        if (newBoard != board) {
            totalScore += score
            board = newBoard
            spawnTile()
        }
    }

    fun mergeRight(grid: List<MutableList<Int>>): Pair<List<MutableList<Int>>, Int> {
        val newBoard = grid.map { it.reversed().toMutableList() }
        val (merged, score) = mergeLeft(newBoard)
        return Pair(merged.map { it.reversed().toMutableList() }, score)
    }

    fun swipeRight() {
        val (newBoard, score) = mergeRight(board)
        if (newBoard != board) {
            totalScore += score
            board = newBoard
            spawnTile()
        }
    }

    fun rotateMatrixClockwise(grid: List<MutableList<Int>>): List<MutableList<Int>> {
        val size = grid.size
        val newGrid = grid.map { it.toMutableList() }

        // Transpose the board
        for (i in 0 until size) {
            for (j in i + 1 until size) {
                val temp = grid[i][j]
                newGrid[i][j] = grid[j][i]
                newGrid[j][i] = temp
            }
        }
        // Reverse each row
        newGrid.forEach { it.reverse() }
        return newGrid
    }

    fun rotateMatrixCounterclockwise(grid: List<MutableList<Int>>): List<MutableList<Int>> {
        val size = grid.size
        val newGrid = grid.map { it.toMutableList() }

        for (i in 0 until size) {
            for (j in i + 1 until size) {
                val temp = grid[i][j]
                newGrid[i][j] = grid[j][i]
                newGrid[j][i] = temp
            }
        }
        return newGrid.reversed()
    }

    fun mergeUp(grid: List<MutableList<Int>>): Pair<List<MutableList<Int>>, Int> {
        val newGrid = rotateMatrixCounterclockwise(grid)
        val (merged, score) = mergeLeft(newGrid)
        val newGrid2 = rotateMatrixClockwise(merged)
        return Pair(newGrid2, score)
    }

    fun swipeUp() {
        val (newBoard, score) = mergeUp(board)
        if (newBoard != board) {
            totalScore += score
            board = newBoard
            spawnTile()
        }
    }

    fun mergeDown(grid: List<MutableList<Int>>): Pair<List<MutableList<Int>>, Int> {
        val newGrid = rotateMatrixClockwise(grid)
        val (merged, score) = mergeLeft(newGrid)
        val newGrid2 = rotateMatrixCounterclockwise(merged)
        return Pair(newGrid2, score)
    }

    fun swipeDown() {
        val (newBoard, score) = mergeDown(board)
        if (newBoard != board) {
            totalScore += score
            board = newBoard
            spawnTile()
        }
    }

    private fun mergeRowLeft(row: MutableList<Int>,
                             acc: List<Int> = emptyList(),
                             score: Int = 0): Pair<MutableList<Int>, Int> {
        if (row.isEmpty())
            return acc.toMutableList() to score
        val x = row[0]
        if (row.size == 1) {
            return (acc + x).toMutableList() to score
        }
        return if (x == row[1]) {
            val newRow = row.drop(2).toMutableList()
            val newAcc = acc + (2 * x)
            val newScore = score + 2 * x
            mergeRowLeft(newRow, newAcc, newScore)
        } else {
            val newRow = row.drop(1).toMutableList()
            val newAcc = acc + x
            mergeRowLeft(newRow, newAcc, score)
        }
    }
}

data class GameScore(val score: Int, val timestamp: String)

fun saveScoreToJson(context: Context, score: Int) {
    val file = context.resources.openRawResource(R.raw.game_score)

}

fun readScoreToJson(context: Context): Int {
    val file = context.resources.openRawResource(R.raw.game_score)
    val score = file.read()
    println(score)
    return 2

}