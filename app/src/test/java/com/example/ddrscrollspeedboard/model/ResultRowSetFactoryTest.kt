package com.example.ddrscrollspeedboard.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ResultRowSetFactoryTest {
    private val listSize = 23

    @Test
    fun create_ScrollSpeed_400() {
        val result = ResultRowSetFactory().create(400)

        assertThat(result.size).isEqualTo(listSize)

        assertThat(result).containsExactly(
            ResultRow(801, 1600, 0.25, 200.25, 400.0),
            ResultRow(801, 1600, 0.5, 200.25, 400.0),
            ResultRow(801, 1600, 0.75, 200.25, 400.0),
            ResultRow(801, 1600, 1.0, 200.25, 400.0),
            ResultRow(801, 1600, 1.25, 200.25, 400.0),
            ResultRow(801, 1600, 1.5, 200.25, 400.0),
            ResultRow(801, 1600, 1.75, 200.25, 400.0),
            ResultRow(801, 1600, 2.0, 200.25, 400.0),
            ResultRow(801, 1600, 2.25, 200.25, 400.0),
            ResultRow(801, 1600, 2.5, 200.25, 400.0),
            ResultRow(801, 1600, 2.75, 200.25, 400.0),
            ResultRow(801, 1600, 3.0, 200.25, 400.0),
            ResultRow(801, 1600, 3.25, 200.25, 400.0),
            ResultRow(801, 1600, 3.55, 200.25, 400.0),
            ResultRow(801, 1600, 3.75, 200.25, 400.0),
            ResultRow(801, 1600, 4.0,200.25, 400.0),
            ResultRow(801, 1600, 4.5, 200.25, 400.0),
            ResultRow(801, 1600, 5.0, 200.25, 400.0),
            ResultRow(801, 1600, 5.5, 200.25, 400.0),
            ResultRow(801, 1600, 6.0, 200.25, 400.0),
            ResultRow(801, 1600, 6.5, 200.25, 400.0),
            ResultRow(801, 1600, 7.0, 200.25, 400.0),
            ResultRow(801, 1600, 7.5, 200.25, 400.0),
            ResultRow(801, 1600, 8.0, 200.25, 400.0),
        )
    }

    @Test
    fun create_ScrollSpeed_0() {
    }
}