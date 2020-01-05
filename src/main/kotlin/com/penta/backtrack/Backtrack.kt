package com.penta.backtrack

class Backtrack<T>(val item: ItemTrack<T>) {

    fun solve(index: Int = 0): Boolean {
        if (item.isComplete(index)) return true

        if (item.play(index)) {
            if (solve(index + 1)) return true

            item.reroll(index)
        }
        return false
    }
}
/*

private fun resolve(idx: Int): Boolean {

    if (idx >= 81)
        return true

    val counter = idx / 9
    val j = idx % 9
    count++

    if (cells[counter][j] !== 0)
        return resolve(idx + 1)
    else {
        for (a in 1..9) {
            if (checkCell(counter, j, a)) {
                cells[counter][j] = a
                if (resolve(idx + 1))
                    return true
            }
        }
        cells[counter][j] = 0
    }
    return false
}

*/
