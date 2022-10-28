package com.example.ddrscrollspeedboard.model

import com.example.ddrscrollspeedboard.data.ResultRowsDataSource
import com.google.common.truth.Truth.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ResultRowSetFactoryTest(
    private val expectedScrollSpeed: Int,
    private val expectedResultRows: List<ResultRow>
) {

    companion object {
        private lateinit var factory: ResultRowSetFactory

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            factory = ResultRowSetFactory()
        }

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(
                    400,
                    ResultRowsDataSource.list_400,
                ),
                arrayOf(
                    2000,
                    ResultRowsDataSource.list_2000,
                ),
                arrayOf(
                    30,
                    ResultRowsDataSource.list_30,
                ),
                arrayOf(
                    29,
                    ResultRowsDataSource.list_out_of_range,
                ),
                arrayOf(
                    2001,
                    ResultRowsDataSource.list_out_of_range,
                ),
            )
        }
    }

    @Test
    fun create_ResultRowList() {
        val result = factory.create(expectedScrollSpeed)

        assertThat(result.size).isEqualTo(expectedResultRows.size)
        assertThat(result).isEqualTo(expectedResultRows)
    }
}