package game

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class EspaceCellulaireTest {



    @Test
    fun testToStringEstTaille1() {
        //Arrange
        val plateau = EspaceCellulaire(1, 1)

        //Act
        val result = plateau.toString()

        //Assert
        assertEquals("O \n", result)
    }

    @Test
    fun testToStringEstTaille2() {
        //Arrange
        val plateau = EspaceCellulaire(2, 2)

        //Act
        val result = plateau.toString()

        //Assert
        assertEquals("O O \nO O \n", result)
    }

    @Test
    fun testToStringEstTaille100() {
        //Arrange
        val plateau = EspaceCellulaire(100, 100)

        //Act
        val result = plateau.toString()

        //Assert
        val gridSize = 100
        val row = buildString {
            repeat(gridSize) {
                append("O ")
            }
            appendLine()
        }

        val resultSplit = buildString {
            repeat(gridSize) {
                append(row)
            }
        }



        assertEquals(resultSplit, result)

    }

    @Test
    fun testToString() {
        //Arrange
        val plateau = EspaceCellulaire(5, 5)

        //Act
        val result = plateau.toString()

        //Assert
        assertEquals("O O O O O \nO O O O O \nO O O O O \nO O O O O \nO O O O O \n", result)
    }

    @Test
    fun testToStringNegative() {
        // Arrange & Act & Assert
        val exception = assertThrows(IllegalArgumentException::class.java) {
            EspaceCellulaire(-1, -1)
        }

        // Assert
        assertEquals("tailleX doit être positif.", exception.message)
    }

    @Test
    fun testToStringZero() {
        // Arrange & Act & Assert
        val exception = assertThrows(IllegalArgumentException::class.java) {
            EspaceCellulaire(0, 0)
        }

        // Assert
        assertEquals("tailleX doit être positif.", exception.message)
    }








    @Test
    fun evoluerUnNombreDeFois() {
        //Arrange
        val plateau = EspaceCellulaire(2, 2)
        plateau[Pair(1, 1)]?.estVivante = true
        plateau[Pair(1, 0)]?.estVivante = true
        plateau[Pair(0, 1)]?.estVivante = true

        //Act
        plateau.evoluerUnNombreDeFois(1)

        //Assert
        assertEquals("X X \nX X \n", plateau.toString())
    }

    @Test
    fun evoluerUnNombreDeFois2() {
        //Arrange
        val plateau = EspaceCellulaire(5, 5)
        plateau[Pair(1, 2)]?.estVivante = true
        plateau[Pair(2, 2)]?.estVivante = true
        plateau[Pair(3, 2)]?.estVivante = true
        //Act
        plateau.evoluerUnNombreDeFois(2)

        //Assert
        assertEquals("O O O O O \nO O X O O \nO O X O O \nO O X O O \nO O O O O \n", plateau.toString())
    }

    @Test
    fun evoluerUnNombreDeFoisZero() {
        //Arrange
        val plateau = EspaceCellulaire(5, 5)
        //Un carré
        plateau[Pair(1, 1)]?.estVivante = true
        plateau[Pair(1, 2)]?.estVivante = true
        plateau[Pair(2, 1)]?.estVivante = true
        plateau[Pair(2, 2)]?.estVivante = true

        //Act
        plateau.evoluerUnNombreDeFois(0)

        //Assert
        assertEquals("O O O O O \nO X X O O \nO X X O O \nO O O O O \nO O O O O \n",plateau.toString())
    }

    @Test
    fun evoluerUnNombreDeFoisNegative() {
        //Arrange
        val plateau = EspaceCellulaire(5, 5)

        //Act & Assert
        val exception = assertThrows(IllegalArgumentException::class.java) {
            plateau.evoluerUnNombreDeFois(-1)
        }

        // Assert
        assertEquals("nombreEvolution doit être positif.", exception.message)
    }
}