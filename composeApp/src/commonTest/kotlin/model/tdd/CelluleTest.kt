package model.tdd

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.Test

class CelluleTest {

    @Test
    fun `est vivante`() {
        val cell = Cell(true)
        assertThat(cell.isAlive).isTrue()
    }


    @Test
    fun `est morte`() {
        val cell = Cell(false)
        assertThat(cell.isAlive).isFalse()
    }


    @Test
    fun `cellule vivante meurt de solitude`() {
        val cell = Cell(true)
        cell.meurtDeSolitude()
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule sans voisin meurt`() {

        //GIVEN
        val cell = Cell(true)

        //WHEN
        cell.changeState(Cell(), Cell(), Cell(), Cell(), Cell(), Cell(), Cell(), Cell())

        //THEN
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule avec 2 voisin reste vivante`() {
        val cell = Cell(true)
        cell.changeState(
            Cell(true),
            Cell(true),
            Cell(),
            Cell(),
            Cell(),
            Cell(),
            Cell(),
            Cell()
        )
        assertThat(cell.isAlive).isTrue()
    }

    @Test
    fun `cellule avec 1 voisin meurt`() {
        val cell = Cell(true)
        cell.changeState(Cell(true), Cell(), Cell(), Cell(), Cell(), Cell(), Cell(), Cell())
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule avec 1 voisin meurt 2`() {
        val cell = Cell(true)
        cell.changeState(Cell(), Cell(), Cell(), Cell(), Cell(), Cell(true), Cell(), Cell())
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule avec deux voisins reste vivante avec des voisins pas au meme endroit`() {
        val cell = Cell(true)
        cell.changeState(
            Cell(),
            Cell(),
            Cell(),
            Cell(),
            Cell(true),
            Cell(),
            Cell(true),
            Cell()
        )
        assertThat(cell.isAlive).isTrue()
    }

    @Test
    fun `cellule avec trois voisins reste vivante avec des voisins pas au meme endroit`() {
        val cell = Cell(true)
        cell.changeState(
            Cell(),
            Cell(),
            Cell(),
            Cell(),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell()
        )
        assertThat(cell.isAlive).isTrue()
    }

    @Test
    fun `cellule avec quatre voisins meurt avec des voisins pas au meme endroit`() {
        val cell = Cell(true)
        cell.changeState(
            Cell(),
            Cell(),
            Cell(),
            Cell(),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true)
        )
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule avec cinq voisins meurt avec des voisins pas au meme endroit`() {
        val cell = Cell(true)
        cell.changeState(
            Cell(),
            Cell(),
            Cell(),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true)
        )
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule avec six voisins meurt avec des voisins pas au meme endroit`() {
        val cell = Cell(true)
        cell.changeState(
            Cell(),
            Cell(),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true)
        )
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule avec sept voisins meurt avec des voisins pas au meme endroit`() {
        val cell = Cell(true)
        cell.changeState(
            Cell(),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true)
        )
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule avec huit voisins meurt avec des voisins pas au meme endroit`() {
        val cell = Cell(true)
        cell.changeState(
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true)
        )
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule avec 3 voisins nait`() {
        val cell = Cell(false)
        cell.changeState(
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(),
            Cell(),
            Cell(),
            Cell(),
            Cell()
        )
        assertThat(cell.isAlive).isTrue()
    }

    @Test
    fun `cellule avec 2 voisin ne nait pas`() {
        val cell = Cell(false)
        cell.changeState(
            Cell(true),
            Cell(true),
            Cell(),
            Cell(),
            Cell(),
            Cell(),
            Cell(),
            Cell()
        )
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule avec 1 voisin ne nait pas`() {
        val cell = Cell(false)
        cell.changeState(Cell(true), Cell(), Cell(), Cell(), Cell(), Cell(), Cell(), Cell())
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule avec 4 voisin ne nait pas`() {
        val cell = Cell(false)
        cell.changeState(
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(),
            Cell(),
            Cell(),
            Cell()
        )
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule avec 5 voisin ne nait pas`() {
        val cell = Cell(false)
        cell.changeState(
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(),
            Cell(),
            Cell()
        )
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule avec 6 voisin ne nait pas`() {
        val cell = Cell(false)
        cell.changeState(
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(),
            Cell()
        )
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule avec 7 voisin ne nait pas`() {
        val cell = Cell(false)
        cell.changeState(
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell()
        )
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule avec 8 voisin ne nait pas`() {
        val cell = Cell(false)
        cell.changeState(
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true),
            Cell(true)
        )
        assertThat(cell.isAlive).isFalse()
    }

    @Test
    fun `cellule dans un coin qui na que 3 voisin`() {
        val cell = Cell(false)
        cell.changeState(Cell(true), Cell(true), Cell(true), null, null, null, null, null)
        assertThat(cell.isAlive).isTrue()
    }


}