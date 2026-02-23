package kmp.project.gameoflife.spacing

class Cell(var isAlive: Boolean = false) {

    fun changeState(
        cell0: Cell?,
        cell1: Cell?,
        cell2: Cell?,
        cell3: Cell?,
        cell4: Cell?,
        cell5: Cell?,
        cell6: Cell?,
        cell7: Cell?
    ) {
        var aliveCount = 0
        for (cellule in listOf(cell0, cell1, cell2, cell3, cell4, cell5, cell6, cell7)) {
            cellule?.let {
                if (cellule.isAlive) {
                    aliveCount++
                }
            }

        }
        isAlive = (isAlive && aliveCount == 2) || aliveCount == 3
    }

}
