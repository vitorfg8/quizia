package com.vitorfg8.quizia.core.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateStarRatingUseCaseTest {

    private val useCase = CalculateStarRatingUseCase()

    @Test
    fun `a perfect score awards five stars`() {
        val actual = useCase(score = 10, total = 10)
        assertEquals(5, actual)
    }

    @Test
    fun `eighty percent awards four stars`() {
        val actual = useCase(score = 8, total = 10)
        assertEquals(4, actual)
    }

    @Test
    fun `sixty percent awards three stars`() {
        val actual = useCase(score = 6, total = 10)
        assertEquals(3, actual)
    }

    @Test
    fun `forty percent awards two stars`() {
        val actual = useCase(score = 4, total = 10)
        assertEquals(2, actual)
    }

    @Test
    fun `a single correct answer awards one star`() {
        val actual = useCase(score = 1, total = 10)
        assertEquals(1, actual)
    }

    @Test
    fun `missing every question awards no star`() {
        val actual = useCase(score = 0, total = 10)
        assertEquals(0, actual)
    }

    @Test
    fun `a score just below a tier stays on the lower tier`() {
        val actual = useCase(score = 7, total = 10)
        assertEquals(3, actual)
    }

    @Test
    fun `an empty quiz awards no star`() {
        val actual = useCase(score = 0, total = 0)
        assertEquals(0, actual)
    }

    @Test
    fun `a score above the total is clamped to five stars`() {
        val actual = useCase(score = 12, total = 10)
        assertEquals(5, actual)
    }

    @Test
    fun `a negative score is clamped to no star`() {
        val actual = useCase(score = -3, total = 10)
        assertEquals(0, actual)
    }
}
