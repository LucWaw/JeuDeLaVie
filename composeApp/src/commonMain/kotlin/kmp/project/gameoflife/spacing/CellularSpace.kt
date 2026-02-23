package kmp.project.gameoflife.spacing

class CellularSpace private constructor(
    private val tailleX: Int,
    private val tailleY: Int,
    espace: MutableMap<Pair<Int, Int>, Cell>
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

    fun resetGrid() {
        for (i in 0..tailleX) {
            for (j in 0..tailleY) {
                this[Pair(i, j)]?.isAlive = false
            }
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
                if (x == tailleX && y == tailleY) {
                    // Coin en bas à droite
                    this[Pair(x, y)]?.changeState(
                        initialCellularSpace[Pair(x - 1, y - 1)],
                        initialCellularSpace[Pair(x - 1, y)],
                        initialCellularSpace[Pair(x - 1, 0)], // Boucle en haut
                        initialCellularSpace[Pair(x, y - 1)],
                        initialCellularSpace[Pair(x, 0)], // Boucle à gauche
                        initialCellularSpace[Pair(0, y - 1)], // Boucle en haut à gauche
                        initialCellularSpace[Pair(0, y)], // Boucle à gauche
                        initialCellularSpace[Pair(0, 0)] // Boucle en haut à gauche
                    )
                } else if (x == 0 && y == 0) {
                    // Coin en haut à gauche
                    this[Pair(x, y)]?.changeState(
                        initialCellularSpace[Pair(tailleX, tailleY)], // Boucle en bas à droite
                        initialCellularSpace[Pair(tailleX, y)], // Boucle à droite
                        initialCellularSpace[Pair(tailleX, 0 + 1)], // Boucle en bas à droite
                        initialCellularSpace[Pair(x, tailleY)], // Boucle en bas
                        initialCellularSpace[Pair(x, 0 + 1)], // En bas
                        initialCellularSpace[Pair(0 + 1, tailleY)], // En bas à droite
                        initialCellularSpace[Pair(0 + 1, y)], // À droite
                        initialCellularSpace[Pair(0 + 1, 0 + 1)] // En bas à droite
                    )
                } else if (x == tailleX && y == 0) {
                    // Coin en haut à droite
                    this[Pair(x, y)]?.changeState(
                        initialCellularSpace[Pair(x - 1, tailleY)], // Boucle en bas
                        initialCellularSpace[Pair(x - 1, y)], // À gauche
                        initialCellularSpace[Pair(x - 1, 0 + 1)], // En bas à gauche
                        initialCellularSpace[Pair(x, tailleY)], // Boucle en bas
                        initialCellularSpace[Pair(x, 0 + 1)], // En bas
                        initialCellularSpace[Pair(0, tailleY)], // Boucle en bas à gauche
                        initialCellularSpace[Pair(0, y)], // Boucle à gauche
                        initialCellularSpace[Pair(0, 0 + 1)] // Boucle en bas à gauche
                    )
                } else if (x == 0 && y == tailleY) {
                    // Coin en bas à gauche
                    this[Pair(x, y)]?.changeState(
                        initialCellularSpace[Pair(tailleX, y - 1)], // Boucle à droite
                        initialCellularSpace[Pair(tailleX, y)], // Boucle à droite
                        initialCellularSpace[Pair(tailleX, 0)], // Boucle en haut à droite
                        initialCellularSpace[Pair(x, y - 1)], // En haut
                        initialCellularSpace[Pair(x, 0)], // Boucle en haut
                        initialCellularSpace[Pair(0 + 1, y - 1)], // En haut à droite
                        initialCellularSpace[Pair(0 + 1, y)], // À droite
                        initialCellularSpace[Pair(0 + 1, 0)] // Boucle en haut à droite
                    )
                } else if (x == 0) {
                    // Bord gauche
                    this[Pair(x, y)]?.changeState(
                        initialCellularSpace[Pair(tailleX, y - 1)], // Boucle à droite
                        initialCellularSpace[Pair(tailleX, y)], // Boucle à droite
                        initialCellularSpace[Pair(tailleX, y + 1)], // Boucle en bas à droite
                        initialCellularSpace[Pair(x, y - 1)], // En haut
                        initialCellularSpace[Pair(x, y + 1)], // En bas
                        initialCellularSpace[Pair(0 + 1, y - 1)], // En haut à droite
                        initialCellularSpace[Pair(0 + 1, y)], // À droite
                        initialCellularSpace[Pair(0 + 1, y + 1)] // En bas à droite
                    )
                } else if (y == 0) {
                    // Bord supérieur
                    this[Pair(x, y)]?.changeState(
                        initialCellularSpace[Pair(x - 1, tailleY)], // Boucle en bas
                        initialCellularSpace[Pair(x - 1, y)], // À gauche
                        initialCellularSpace[Pair(x - 1, 0 + 1)], // En bas à gauche
                        initialCellularSpace[Pair(x, tailleY)], // Boucle en bas
                        initialCellularSpace[Pair(x, 0 + 1)], // En bas
                        initialCellularSpace[Pair(x + 1, tailleY)], // Boucle en bas à droite
                        initialCellularSpace[Pair(x + 1, y)], // À droite
                        initialCellularSpace[Pair(x + 1, 0 + 1)] // En bas à droite
                    )
                } else if (x == tailleX) {
                    // Bord droit
                    this[Pair(x, y)]?.changeState(
                        initialCellularSpace[Pair(x - 1, y - 1)],
                        initialCellularSpace[Pair(x - 1, y)],
                        initialCellularSpace[Pair(x - 1, y + 1)],
                        initialCellularSpace[Pair(x, y - 1)],
                        initialCellularSpace[Pair(x, y + 1)],
                        initialCellularSpace[Pair(0, y - 1)], // Boucle à gauche
                        initialCellularSpace[Pair(0, y)], // Boucle à gauche
                        initialCellularSpace[Pair(0, y + 1)] // Boucle en bas à gauche
                    )
                } else if (y == tailleY) {
                    // Bord inférieur
                    this[Pair(x, y)]?.changeState(
                        initialCellularSpace[Pair(x - 1, y - 1)],
                        initialCellularSpace[Pair(x - 1, y)],
                        initialCellularSpace[Pair(x - 1, 0)], // Boucle en haut
                        initialCellularSpace[Pair(x, y - 1)],
                        initialCellularSpace[Pair(x, 0)], // Boucle en haut
                        initialCellularSpace[Pair(x + 1, y - 1)],
                        initialCellularSpace[Pair(x + 1, y)],
                        initialCellularSpace[Pair(x + 1, 0)] // Boucle en haut à droite
                    )
                } else {
                    // Cas général, cellules internes
                    this[Pair(x, y)]?.changeState(
                        initialCellularSpace[Pair(x - 1, y - 1)],
                        initialCellularSpace[Pair(x - 1, y)],
                        initialCellularSpace[Pair(x - 1, y + 1)],
                        initialCellularSpace[Pair(x, y - 1)],
                        initialCellularSpace[Pair(x, y + 1)],
                        initialCellularSpace[Pair(x + 1, y - 1)],
                        initialCellularSpace[Pair(x + 1, y)],
                        initialCellularSpace[Pair(x + 1, y + 1)]
                    )
                }
            }
        }

    }
}