package ihm
import game.EspaceCellulaire
import javax.swing.JButton
import javax.swing.JPanel
import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.Timer

class MainPage(private val plateau: EspaceCellulaire) : JPanel() {
    private val cellSize = 30
    private var selectionMode = true

    init {
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (selectionMode) {
                    // En mode sélection, l'utilisateur peut définir les cellules de départ
                    val row = e.y / cellSize
                    val col = e.x / cellSize

                    plateau[Pair(row, col)]?.estVivante = !plateau[Pair(row, col)]?.estVivante!!

                    repaint()
                }
            }
        })

        val startButton = JButton("Commencer")
        startButton.addActionListener {
            // Passer en mode simulation lorsque l'utilisateur clique sur "Commencer"
            selectionMode = false
        }

        val timer = Timer(1000) {
            // Évoluer le plateau toutes les secondes en mode simulation
            if (!selectionMode) {
                plateau.evoluer()
                repaint()
            }
        }

        // Démarrer le timer seulement lorsqu'on est en mode simulation
        startButton.addActionListener {
            if (!timer.isRunning) {
                timer.start()
            }
        }

        add(startButton)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        // Dessiner la grille
        for (i in 0 until plateau.tailleX) {
            for (j in 0 until plateau.tailleY) {
                // Dessiner la case
                g.color = if (plateau[Pair(i, j)]?.estVivante == true) Color.BLUE else Color.WHITE
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize)

                // Dessiner la bordure de la case
                g.color = Color.BLACK
                g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize)
            }
        }
    }
}