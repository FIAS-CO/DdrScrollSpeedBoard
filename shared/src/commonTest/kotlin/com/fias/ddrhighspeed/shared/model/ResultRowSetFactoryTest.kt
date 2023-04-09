package com.fias.ddrhighspeed.shared.model

import kotlin.test.Test
import kotlin.test.assertEquals

class ResultRowSetFactoryTest {

    @Test
    fun create_ResultRowList_BPM400() {
        create_ResultRowList(400, ResultRowsDataSource.list_400)
    }

    @Test
    fun create_ResultRowList_BPM2000() {
        create_ResultRowList(2000, ResultRowsDataSource.list_2000)
    }

    @Test
    fun create_ResultRowList_BPM30() {
        create_ResultRowList(30, ResultRowsDataSource.list_30)
    }

    @Test
    fun create_ResultRowList_BPM2001() {
        create_ResultRowList(2001, ResultRowsDataSource.list_out_of_range)
    }

    @Test
    fun create_ResultRowList_BPM29() {
        create_ResultRowList(29, ResultRowsDataSource.list_out_of_range)
    }

    private fun create_ResultRowList(scrollSpeed: Int, expectedResultRows: List<ResultRow>) {
        val factory = ResultRowSetFactory()
        val result = factory.create(scrollSpeed)

        assertEquals(result.size, expectedResultRows.size)
        assertEquals(result, expectedResultRows)
    }
}