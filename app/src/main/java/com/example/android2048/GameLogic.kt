package com.example.android2048

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class GameLogic {
    private val size = 4
    var board by mutableStateOf(List(size) { MutableList(size) {0} })

    var totalScore by mutableIntStateOf(0)

    init {
        reset()
    }

    fun reset() {
        for (row in board) {
            row.fill(0)
        }
        spawnTile()
        spawnTile()

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
        totalScore += score
        board = newBoard
        spawnTile()
    }

    fun mergeRight(grid: List<MutableList<Int>>): Pair<List<MutableList<Int>>, Int> {
        val newBoard = grid.map { it.reversed().toMutableList() }
        val (merged, score) = mergeLeft(newBoard)
        return Pair(merged.map { it.reversed().toMutableList() }, score)
    }

    fun swipeRight() {
        val (newBoard, score) = mergeRight(board)
        totalScore += score
        board = newBoard
        spawnTile()
    }

    fun rotateMatrixClockwise() {
        val size = board.size

        // Transpose the board
        for (i in 0 until size) {
            for (j in i + 1 until size) {
                val temp = board[i][j]
                board[i][j] = board[j][i]
                board[j][i] = temp
            }
        }
        // Reverse each row
        board.forEach { it.reverse() }
    }

    fun rotateMatrixCounterclockwise() {
        val size = board.size

        for (i in 0 until size) {
            for (j in i + 1 until size) {
                val temp = board[i][j]
                board[i][j] = board[j][i]
                board[j][i] = temp
            }
        }
        board = board.reversed()
    }

    fun mergeUp() {
        rotateMatrixCounterclockwise()
        mergeLeft()
        rotateMatrixClockwise()
    }

    fun swipeUp() {
        mergeUp()
        spawnTile()
    }

    fun mergeDown() {
        rotateMatrixClockwise()
        mergeLeft()
        rotateMatrixCounterclockwise()
    }

    fun swipeDown() {
        mergeDown()
        spawnTile()
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