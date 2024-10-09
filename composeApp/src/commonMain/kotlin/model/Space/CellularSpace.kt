package model.Space

import model.tdd.Cell

class CellularSpace private constructor(
    private val tailleX: Int,
    private val tailleY: Int,
    private val espace: MutableMap<Pair<Int, Int>, Cell>
) : MutableMap<Pair<Int, Int>, Cell> by espace {

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
                this[Pair(i, j)] = Cell()
            }
        }
    }

    fun setAliveCells(vararg aliveCells: Pair<Int, Int>) {
        for (cell in aliveCells) {
            this[cell]?.isAlive = true
        }
    }

    fun getAliveCells(): List<Pair<Int, Int>> {
        val aliveCells = mutableListOf<Pair<Int, Int>>()
        for (i in 0..tailleX) {
            for (j in 0..tailleY) {
                if (this[Pair(i, j)]?.isAlive == true) {
                    aliveCells.add(Pair(i, j))
                }
            }
        }
        return aliveCells
    }

    override fun toString(): String {
        var str = ""
        for (i in 0..tailleX) {
            for (j in 0..tailleY) {
                str += this[Pair(i, j)]?.isAlive?.let { if (it) "X" else "O" } ?: "O"
                str += " "
            }
            str += "\n"
        }
        return str
    }

    private fun clone(): Any {
        val cellularSpace = CellularSpace(this.tailleX + 1, this.tailleY + 1)
        for (i in 0..tailleX) {
            for (j in 0..tailleY) {
                cellularSpace[Pair(i, j)] = this[Pair(i, j)]?.let { Cell(it.isAlive) } ?: Cell()
            }
        }
        return cellularSpace
    }

    fun evolveMultipleTimes(evolutionCount: Int) {
        require(evolutionCount >= 0) { "nombreEvolution doit être positif." }
        for (i in 1..evolutionCount) {
            evolve()
        }
    }

    fun evolve() {
        val initialCellularSpace = this.clone() as CellularSpace

        for (x in 0..tailleX) {
            for (y in 0..tailleY) {
                if (tailleX > 0 && tailleY > 0) {
                    this[Pair(x, y)]?.changeState(
                        //Changer l'état des 8 cellules voisines, passer a droite fera arriver a gauche
                        initialCellularSpace[Pair(
                            (x - 1 + tailleX) % tailleX,
                            (y - 1 + tailleY) % tailleY
                        )],
                        initialCellularSpace[Pair(x, (y - 1 + tailleY) % tailleY)],
                        initialCellularSpace[Pair((x + 1) % tailleX, (y - 1 + tailleY) % tailleY)],
                        initialCellularSpace[Pair((x - 1 + tailleX) % tailleX, y)],
                        initialCellularSpace[Pair((x + 1) % tailleX, y)],
                        initialCellularSpace[Pair((x - 1 + tailleX) % tailleX, (y + 1) % tailleY)],
                        initialCellularSpace[Pair(x, (y + 1) % tailleY)],
                        initialCellularSpace[Pair((x + 1) % tailleX, (y + 1) % tailleY)]
                    )
                } else {
                    this[Pair(x, y)]?.changeState(
                        //Changer l'état des 8 cellules voisines
                        initialCellularSpace[Pair(x, y)],
                        initialCellularSpace[Pair(x, y)],
                        initialCellularSpace[Pair(x, y)],
                        initialCellularSpace[Pair(x, y)],
                        initialCellularSpace[Pair(x, y)],
                        initialCellularSpace[Pair(x, y)],
                        initialCellularSpace[Pair(x, y)],
                        initialCellularSpace[Pair(x, y)]
                    )
                }
            }
        }
    }
}