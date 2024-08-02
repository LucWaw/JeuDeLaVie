package model.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class EspaceCellulaireTest {

    @Test
    fun testToStringEstTaille1() {
        //Arrange
        val grid = CellularSpace(1, 1)

        //Act
        val result = grid.toString()

        //Assert
        assertEquals("O \n", result)
    }

    @Test
    fun testToStringEstTaille2() {
        //Arrange
        val grid = CellularSpace(2, 2)

        //Act
        val result = grid.toString()

        //Assert
        assertEquals("O O \nO O \n", result)
    }

    @Test
    fun testToStringEstTaille100() {
        //Arrange
        val grid = CellularSpace(100, 100)

        //Act
        val result = grid.toString()

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
        val grid = CellularSpace(5, 5)

        //Act
        val result = grid.toString()

        //Assert
        assertEquals("O O O O O \nO O O O O \nO O O O O \nO O O O O \nO O O O O \n", result)
    }

    @Test
    fun testToStringNegative() {
        // Arrange & Act & Assert
        val exception = assertThrows(IllegalArgumentException::class.java) {
            CellularSpace(-1, -1)
        }

        // Assert
        assertEquals("tailleX doit être positif.", exception.message)
    }

    @Test
    fun testToStringZero() {
        // Arrange & Act & Assert
        val exception = assertThrows(IllegalArgumentException::class.java) {
            CellularSpace(0, 0)
        }

        // Assert
        assertEquals("tailleX doit être positif.", exception.message)
    }


    @Test
    fun evoluerUnNombreDeFois() {
        //Arrange
        val grid = CellularSpace(2, 2)
        grid[Pair(1, 1)]?.isAlive = true
        grid[Pair(1, 0)]?.isAlive = true
        grid[Pair(0, 1)]?.isAlive = true

        //Act
        grid.evolveMultipleTimes(1)

        //Assert
        assertEquals("X X \nX X \n", grid.toString())
    }

    @Test
    fun evoluerUnNombreDeFois2() {
        //Arrange
        val grid = CellularSpace(5, 5)
        grid[Pair(1, 2)]?.isAlive = true
        grid[Pair(2, 2)]?.isAlive = true
        grid[Pair(3, 2)]?.isAlive = true
        //Act
        grid.evolveMultipleTimes(2)

        //Assert
        assertEquals(
            "O O O O O \nO O X O O \nO O X O O \nO O X O O \nO O O O O \n",
            grid.toString()
        )
    }

    @Test
    fun evoluerUnNombreDeFoisZero() {
        //Arrange
        val grid = CellularSpace(5, 5)
        //Un carré
        grid[Pair(1, 1)]?.isAlive = true
        grid[Pair(1, 2)]?.isAlive = true
        grid[Pair(2, 1)]?.isAlive = true
        grid[Pair(2, 2)]?.isAlive = true

        //Act
        grid.evolveMultipleTimes(0)

        //Assert
        assertEquals(
            "O O O O O \nO X X O O \nO X X O O \nO O O O O \nO O O O O \n",
            grid.toString()
        )
    }

    @Test
    fun evoluerUnNombreDeFoisNegative() {
        //Arrange
        val grid = CellularSpace(5, 5)

        //Act & Assert
        val exception = assertThrows(IllegalArgumentException::class.java) {
            grid.evolveMultipleTimes(-1)
        }

        // Assert
        assertEquals("nombreEvolution doit être positif.", exception.message)
    }


    @Test
    fun nullAutour() {
        //Arrange
        val grid = CellularSpace(1, 1)
        grid.setAliveCells(Pair(0, 0))

        //Act
        grid.evolveMultipleTimes(1)
        val result = grid.toString()

        //Assert
        assertEquals("O \n", result)
    }


}