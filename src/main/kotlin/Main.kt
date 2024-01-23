import game.EspaceCellulaire
import ihm.MainPage
import javax.swing.JFrame

/*fun main() {
    println("Bonjour Conway! \n")

    val plateau = EspaceCellulaire(5, 5)

    //planeur en haut à gauche
    plateau[Pair(0, 1)]?.estVivante = true
    plateau[Pair(1, 2)]?.estVivante = true
    plateau[Pair(2, 0)]?.estVivante = true
    plateau[Pair(2, 1)]?.estVivante = true
    plateau[Pair(2, 2)]?.estVivante = true

    println(plateau)

    plateau.evoluerUnNombreDeFois(6)

    println(plateau)



}*/


fun main() {
    val plateau = EspaceCellulaire(5, 5)

    val frame = JFrame("Grid Application")
    val canvas = MainPage(plateau)
    frame.contentPane.add(canvas)

    frame.setSize(300, 300)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isVisible = true
}

