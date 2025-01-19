package com.example.android2048


class GameLogic {
    private val size = 4
    var board = Array(size) { IntArray(size) }
    var totalScore: Int = 0
    var score: Int = 0

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

    fun swipeLeft() {
        for (r in 0 until size) {
            val row = board[r]
            val (merged, newScore) = mergeRowLeft(row.filter({x -> x != 0}))
            for (c in 0 until size)
                board[r][c] = merged[c]
            totalScore += score
            score = newScore
        }
        spawnTile()
    }

    fun swipeRight() {
        for (r in 0 until size){
            val row = board[r].reversed()
            val (merged, newScore) = mergeRowLeft(row.filter({x -> x != 0}))
            val reme = merged.reversed()
            for (c in 0 until size)
                board[r][c] = reme[c]
            totalScore += score
            score = newScore
        }
        spawnTile()
    }
    fun swipeUp() {
        for (c in 0 until size) {
            val column = IntArray(size) { board[it][c] }
            val (merged, newScore) = mergeRowLeft(column.filter({x -> x != 0}))
            for (r in 0 until size) {
                board[r][c] = merged[r]
            }
            totalScore += score
            score = newScore
        }
        spawnTile()
    }

    fun swipeDown() {
        for (c in 0 until size) {
            val column = IntArray(size) { board[it][c] }.reversedArray()
            val (merged, newScore) = mergeRowLeft(column.filter({x -> x != 0}))
            val reme = merged.reversed()
            for (r in 0 until size) {
                board[r][c] = reme[r]
            }
            totalScore += score
            score = newScore
        }
        spawnTile()
    }

    private fun mergeRowLeft(row: List<Int>,
                             acc: List<Int> = emptyList(),
                             score: Int = 0): Pair<List<Int>, Int> {
        if (row.isEmpty())
            return acc to score
        val x = row[0]
        if (row.size == 1) {
            return acc + x to score
        }
        return if (x == row[1]) {
            val newRow = row.drop(2)
            val newAcc = acc + (2 * x)
            val newScore = score + 2 * x
            mergeRowLeft(newRow, newAcc, newScore)
        } else {
            val newRow = row.drop(1)
            val newAcc = acc + x
            mergeRowLeft(newRow, newAcc, score)
        }
    }
}