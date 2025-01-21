package com.example.android2048

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


class MergeUnitTest {
    @Test
    fun merge_left() {
        val gameLogic = GameLogic()
        gameLogic.board = listOf(
            mutableListOf(2, 2, 2, 2),
            mutableListOf(2, 4, 4, 16),
            mutableListOf(0, 0, 4, 0),
            mutableListOf(0, 4, 0, 2)
        )

        val (merged, score) = gameLogic.mergeLeft(gameLogic.board)
        assertEquals(
            listOf(
                mutableListOf(4, 4, 0, 0),
                mutableListOf(2, 8, 16, 0),
                mutableListOf(4, 0, 0, 0),
                mutableListOf(4, 2, 0, 0)
            ), merged
        )
    }

    @Test
    fun merge_right() {
        val gameLogic = GameLogic()
        gameLogic.board = listOf(
            mutableListOf(2, 2, 2, 2),
            mutableListOf(2, 4, 4, 16),
            mutableListOf(0, 0, 4, 0),
            mutableListOf(0, 4, 0, 2)
        )
        val (merged, score) = gameLogic.mergeRight(gameLogic.board)
        assertEquals(
            listOf(
                mutableListOf(0, 0, 4, 4),
                mutableListOf(0, 2, 8, 16),
                mutableListOf(0, 0, 0, 4),
                mutableListOf(0, 0, 4, 2)
            ), merged
        )
    }

    @Test
    fun merge_up() {
        val gameLogic = GameLogic()
        gameLogic.board = listOf(
            mutableListOf(2, 2, 2, 2),
            mutableListOf(2, 4, 4, 16),
            mutableListOf(0, 0, 4, 0),
            mutableListOf(0, 4, 0, 2)
        )
        val (merged, score) = gameLogic.mergeUp(gameLogic.board)
        assertEquals(
            listOf(
                mutableListOf(4, 2, 2, 2),
                mutableListOf(0, 8, 8, 16),
                mutableListOf(0, 0, 0, 2),
                mutableListOf(0, 0, 0, 0)
            ), merged
        )
    }

    @Test
    fun merge_down() {
        val gameLogic = GameLogic()
        gameLogic.board = listOf(
            mutableListOf(2, 2, 2, 2),
            mutableListOf(2, 4, 4, 16),
            mutableListOf(0, 0, 4, 0),
            mutableListOf(0, 4, 0, 2)
        )
        val (merged, score) = gameLogic.mergeDown(gameLogic.board)
        assertEquals(
            listOf(
                mutableListOf(0, 0, 0, 0),
                mutableListOf(0, 0, 0, 2),
                mutableListOf(0, 2, 2, 16),
                mutableListOf(4, 8, 8, 2)
            ), merged
        )

    }
}