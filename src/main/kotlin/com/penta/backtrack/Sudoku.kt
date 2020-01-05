package com.penta.backtrack

class Sudoku {

    val cells = arrayOf(
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0))

    private fun checkRow(i: Int, j: Int, value: Int): Boolean {
        for (k in 0..8)
            if (k != j && value == cells[i][k])
                return false
        return true
    }

    private fun checkColumn(i: Int, j: Int, value: Int): Boolean {
        for (k in 0  until 9)
            if (k != i && value == cells[k][j])
                return false
        return true
    }

    private fun checkGrid(i: Int, j: Int, value: Int): Boolean {
        val idx = getGridIndexe(i, 0)
        val jdx = getGridIndexe(j, 0)

        for (k in idx until idx + 3)
            for (p in jdx until jdx + 3)
                if (i != k && j != p && value == cells[k][p])
                    return false
        return true
    }

    private fun getGridIndexe(value: Int, i: Int): Int {
        return if (value < i) i - 3 else getGridIndexe(value, i + 3)
    }

    fun checkCell(i: Int, j: Int, value: Int): Boolean {
        return checkRow(i, j, value) && checkColumn(i, j, value) &&
                checkGrid(i, j, value)
    }

    override fun toString(): String {
        var str = ""
        for (i in 0..8) {
            for (j in 0..8)
                str += cells[i][j].toString() + ","
            str += "\n"
        }
        return str
    }

    fun isValidSudoku(): Boolean {
        for (i in 0..8)
            for (j in 0..8)
                if (cells[i][j] != 0)
                    if (!checkCell(i, j, cells[i][j]))
                        return false
        return true
    }
}