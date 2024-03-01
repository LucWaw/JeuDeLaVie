package model.tdd

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test

class CelluleTest {

    @Test
    fun `est vivante`() {
        val cellule = Cellule(true)
        assertThat(cellule.estVivante).isTrue()
    }


    @Test
    fun `est morte`() {
        val cellule = Cellule(false)
        assertThat(cellule.estVivante).isFalse()
    }


    @Test
    fun `cellule vivante meurt de solitude`() {
        val cellule = Cellule(true)
        cellule.meurtDeSolitude()
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule sans voisin meurt`() {

        //GIVEN
        val cellule = Cellule(true)

        //WHEN
        cellule.changeEtat(Cellule(), Cellule(), Cellule(), Cellule(), Cellule(), Cellule(), Cellule(), Cellule())

        //THEN
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule avec 2 voisin reste vivante`() {
        val cellule = Cellule(true)
        cellule.changeEtat(
            Cellule(true),
            Cellule(true),
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule()
        )
        assertThat(cellule.estVivante).isTrue()
    }

    @Test
    fun `cellule avec 1 voisin meurt`() {
        val cellule = Cellule(true)
        cellule.changeEtat(Cellule(true), Cellule(), Cellule(), Cellule(), Cellule(), Cellule(), Cellule(), Cellule())
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule avec 1 voisin meurt 2`() {
        val cellule = Cellule(true)
        cellule.changeEtat(Cellule(), Cellule(), Cellule(), Cellule(), Cellule(), Cellule(true), Cellule(), Cellule())
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule avec deux voisins reste vivante avec des voisins pas au meme endroit`() {
        val cellule = Cellule(true)
        cellule.changeEtat(
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule(true),
            Cellule(),
            Cellule(true),
            Cellule()
        )
        assertThat(cellule.estVivante).isTrue()
    }

    @Test
    fun `cellule avec trois voisins reste vivante avec des voisins pas au meme endroit`() {
        val cellule = Cellule(true)
        cellule.changeEtat(
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule()
        )
        assertThat(cellule.estVivante).isTrue()
    }

    @Test
    fun `cellule avec quatre voisins meurt avec des voisins pas au meme endroit`() {
        val cellule = Cellule(true)
        cellule.changeEtat(
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true)
        )
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule avec cinq voisins meurt avec des voisins pas au meme endroit`() {
        val cellule = Cellule(true)
        cellule.changeEtat(
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true)
        )
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule avec six voisins meurt avec des voisins pas au meme endroit`() {
        val cellule = Cellule(true)
        cellule.changeEtat(
            Cellule(),
            Cellule(),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true)
        )
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule avec sept voisins meurt avec des voisins pas au meme endroit`() {
        val cellule = Cellule(true)
        cellule.changeEtat(
            Cellule(),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true)
        )
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule avec huit voisins meurt avec des voisins pas au meme endroit`() {
        val cellule = Cellule(true)
        cellule.changeEtat(
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true)
        )
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule avec 3 voisins nait`() {
        val cellule = Cellule(false)
        cellule.changeEtat(
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule()
        )
        assertThat(cellule.estVivante).isTrue()
    }

    @Test
    fun `cellule avec 2 voisin ne nait pas`() {
        val cellule = Cellule(false)
        cellule.changeEtat(
            Cellule(true),
            Cellule(true),
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule()
        )
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule avec 1 voisin ne nait pas`() {
        val cellule = Cellule(false)
        cellule.changeEtat(Cellule(true), Cellule(), Cellule(), Cellule(), Cellule(), Cellule(), Cellule(), Cellule())
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule avec 4 voisin ne nait pas`() {
        val cellule = Cellule(false)
        cellule.changeEtat(
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(),
            Cellule(),
            Cellule(),
            Cellule()
        )
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule avec 5 voisin ne nait pas`() {
        val cellule = Cellule(false)
        cellule.changeEtat(
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(),
            Cellule(),
            Cellule()
        )
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule avec 6 voisin ne nait pas`() {
        val cellule = Cellule(false)
        cellule.changeEtat(
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(),
            Cellule()
        )
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule avec 7 voisin ne nait pas`() {
        val cellule = Cellule(false)
        cellule.changeEtat(
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule()
        )
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule avec 8 voisin ne nait pas`() {
        val cellule = Cellule(false)
        cellule.changeEtat(
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true),
            Cellule(true)
        )
        assertThat(cellule.estVivante).isFalse()
    }

    @Test
    fun `cellule dans un coin qui na que 3 voisin`() {
        val cellule = Cellule(false)
        cellule.changeEtat(Cellule(true), Cellule(true), Cellule(true), null, null, null, null, null)
        assertThat(cellule.estVivante).isTrue()
    }


}