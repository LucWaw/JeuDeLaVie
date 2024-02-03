package model.tdd

class Cellule(var estVivante: Boolean = false) {

    fun meurtDeSolitude() {
        estVivante = false
    }

    fun changeEtat(
        cellule0: Cellule?,
        cellule1: Cellule?,
        cellule2: Cellule?,
        cellule3: Cellule?,
        cellule4: Cellule?,
        cellule5: Cellule?,
        cellule6: Cellule?,
        cellule7: Cellule?
    ) {
        var cptEstVivante = 0
        for (cellule in listOf(cellule0, cellule1, cellule2, cellule3, cellule4, cellule5, cellule6, cellule7)) {
            cellule?.let {
                if (cellule.estVivante) {
                    cptEstVivante++
                }
            }

        }
        estVivante = (estVivante && cptEstVivante == 2) || cptEstVivante == 3
    }

}
