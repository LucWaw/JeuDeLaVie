package game

import tdd.Cellule

class EspaceCellulaire private constructor(
    private val tailleX: Int,
    private val tailleY: Int,
    private val ignoredMap: MutableMap<Pair<Int, Int>, Cellule>
) : MutableMap<Pair<Int, Int>, Cellule> by ignoredMap {

    /**
     * La taille de l'espace ne doit pas dépendre de 0 comme indice
     * Exemple tailleX = 5 et tailleY = 5.
     * Les indices vont de 0 à 4.
     */
    constructor(tailleX: Int, tailleY: Int) : this(
        requirePositive(tailleX, "tailleX"),
        requirePositive(tailleY, "tailleY"),
        mutableMapOf()
    )

    private companion object {
        fun requirePositive(value: Int, paramName: String): Int {
            require(value > 0) { "$paramName doit être positif." }
            return value - 1
        }
    }

    init {
        for (i in 0..tailleX) {
            for (j in 0..tailleY) {
                this[Pair(i, j)] = Cellule()
            }
        }
    }


    override fun toString(): String {
        var str = ""
        for (i in 0..tailleX) {
            for (j in 0..tailleY) {
                str += this[Pair(i, j)]?.estVivante?.let { if (it) "X" else "O" } ?: "O"
                str += " "
            }
            str += "\n"
        }
        return str
    }

    private fun clone(): Any {
        val espaceCellulaire = EspaceCellulaire(this.tailleX, this.tailleY)
        for (i in 0..tailleX) {
            for (j in 0..tailleY) {
                espaceCellulaire[Pair(i, j)] = this[Pair(i, j)]?.let { Cellule(it.estVivante) } ?: Cellule()
            }
        }
        return espaceCellulaire
    }

    fun evoluerUnNombreDeFois(nombreEvolution: Int) {
        require(nombreEvolution >= 0) { "nombreEvolution doit être positif." }
        for (i in 1..nombreEvolution) {
            evoluer()
        }
    }

    private fun evoluer() {
        val espaceCellulaireClone = this.clone() as EspaceCellulaire

        for (x in 0..tailleX) {
            for (y in 0..tailleY) {
                this[Pair(x, y)]?.changeEtat(
                    //Changer l'état des 8 cellules voisines
                    espaceCellulaireClone[Pair(x - 1, y - 1)],
                    espaceCellulaireClone[Pair(x - 1, y)],
                    espaceCellulaireClone[Pair(x - 1, y + 1)],
                    espaceCellulaireClone[Pair(x, y - 1)],
                    espaceCellulaireClone[Pair(x, y + 1)],
                    espaceCellulaireClone[Pair(x + 1, y - 1)],
                    espaceCellulaireClone[Pair(x + 1, y)],
                    espaceCellulaireClone[Pair(x + 1, y + 1)]
                )
            }
        }


    }
}