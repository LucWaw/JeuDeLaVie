import game.EspaceCellulaire

fun main() {
    println("Bonjour Conway! \n")

    val plateau = EspaceCellulaire(5, 5)

    //planeur en haut a gauche
    plateau[Pair(0, 1)]?.estVivante = true
    plateau[Pair(1, 2)]?.estVivante = true
    plateau[Pair(2, 0)]?.estVivante = true
    plateau[Pair(2, 1)]?.estVivante = true
    plateau[Pair(2, 2)]?.estVivante = true

    println(plateau)

    plateau.evoluerUnNombreDeFois(6)

    println(plateau)
}


