package com.example.ddrscrollspeedboard.model

import com.example.ddrscrollspeedboard.data.ResultRowsDataSource
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ResultRowSetFactoryTest {
    private val listSize = 24

    @Test
    fun create_ScrollSpeed_400() {
        val result = ResultRowSetFactory().create(400)

        assertThat(result.size).isEqualTo(listSize)

        assertThat(result).isEqualTo(ResultRowsDataSource.list_400)
    }

    @Test
    fun create_ScrollSpeed_29() {
        val result = ResultRowSetFactory().create(29)

        assertThat(result.size).isEqualTo(0)
    }

    @Test
    fun create_ScrollSpeed_30() {
        val result = ResultRowSetFactory().create(30)

        assertThat(result.size).isEqualTo(listSize)

        assertThat(result).containsExactly(
            ResultRow(61, 120, 0.25, 15.25, 30.00),
            ResultRow(41, 60, 0.50, 20.50, 30.00),
            ResultRow(31, 40, 0.75, 23.25, 30.00),
            ResultRow(25, 30, 1.00, 25.00, 30.00),
            ResultRow(21, 24, 1.25, 26.25, 30.00),
            ResultRow(18, 20, 1.50, 27.00, 30.00),
            ResultRow(16, 17, 1.75, 28.00, 29.75),
            ResultRow(14, 15, 2.00, 28.00, 30.00),
            ResultRow(13, 13, 2.25, 29.25, 29.25),
            ResultRow(11, 12, 2.50, 27.50, 30.00),
            ResultRow(11, 10, 2.75, 30.25, 27.50),
            ResultRow(10, 10, 3.00, 30.00, 30.00),
            ResultRow(9, 9, 3.25, 29.25, 29.25),
            ResultRow(9, 8, 3.50, 31.50, 28.00),
            ResultRow(8, 8, 3.75, 30.00, 30.00),
            ResultRow(7, 7, 4.00, 28.00, 28.00),
            ResultRow(7, 6, 4.50, 31.50, 27.00),
            ResultRow(6, 6, 5.00, 30.00, 30.00),
            ResultRow(6, 5, 5.50, 33.00, 27.50),
            ResultRow(5, 5, 6.00, 30.00, 30.00),
            ResultRow(5, 4, 6.50, 32.50, 26.00),
            ResultRow(5, 4, 7.00, 35.00, 28.00),
            ResultRow(4, 4, 7.50, 30.00, 30.00),
            ResultRow(1, 3, 8.00, 8.00, 24.00),
        )
    }

    @Test
    fun create_ScrollSpeed_2000() {

        val result = ResultRowSetFactory().create(2000)

        assertThat(result.size).isEqualTo(listSize)

        assertThat(result).containsExactly(
            ResultRow(4001, 8000, 0.25, 1000.25, 2000.00),
            ResultRow(2667, 4000, 0.50, 1333.50, 2000.00),
            ResultRow(2001, 2666, 0.75, 1500.75, 1999.50),
            ResultRow(1601, 2000, 1.00, 1601.00, 2000.00),
            ResultRow(1334, 1600, 1.25, 1667.50, 2000.00),
            ResultRow(1143, 1333, 1.50, 1714.50, 1999.50),
            ResultRow(1001, 1142, 1.75, 1751.75, 1998.50),
            ResultRow(889, 1000, 2.00, 1778.00, 2000.00),
            ResultRow(801, 888, 2.25, 1802.25, 1998.00),
            ResultRow(728, 800, 2.50, 1820.00, 2000.00),
            ResultRow(667, 727, 2.75, 1834.25, 1999.25),
            ResultRow(616, 666, 3.00, 1848.00, 1998.00),
            ResultRow(572, 615, 3.25, 1859.00, 1998.75),
            ResultRow(534, 571, 3.50, 1869.00, 1998.50),
            ResultRow(501, 533, 3.75, 1878.75, 1998.75),
            ResultRow(445, 500, 4.00, 1780.00, 2000.00),
            ResultRow(401, 444, 4.50, 1804.50, 1998.00),
            ResultRow(364, 400, 5.00, 1820.00, 2000.00),
            ResultRow(334, 363, 5.50, 1837.00, 1996.50),
            ResultRow(308, 333, 6.00, 1848.00, 1998.00),
            ResultRow(286, 307, 6.50, 1859.00, 1995.50),
            ResultRow(267, 285, 7.00, 1869.00, 1995.00),
            ResultRow(251, 266, 7.50, 1882.50, 1995.00),
            ResultRow(1, 250, 8.00, 8.00, 2000.00),
        )
    }

    @Test
    fun create_ScrollSpeed_2001() {
        val result = ResultRowSetFactory().create(2001)

        assertThat(result.size).isEqualTo(0)
    }
}