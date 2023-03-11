package com.fias.ddrhighspeed.shared.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("NonAsciiCharacters", "TestFunctionName")
class ResultRowSetFactoryTestForDetail {

    @Test
    fun createForDetail() {
        val factory = ResultRowSetFactory()
        factory.createForDetail(400, "test", 150.0).apply {
            assertEquals(category, "test")
            assertEquals(bpm, "150.0")
            assertEquals(highSpeed, "× 2.5")
            assertEquals(scrollSpeed, "= 375.0")
        }
    }

    @Test
    fun createForDetail_ScrollSpeedが最大値() {
        val factory = ResultRowSetFactory()
        factory.createForDetail(2000, "", 300.0).apply {
            assertEquals(category, "")
            assertEquals(bpm, "300.0")
            assertEquals(highSpeed, "× 6.5")
            assertEquals(scrollSpeed, "= 1950.0")
        }
    }

    @Test
    fun createForDetail_ScrollSpeedが最小値() {
        val factory = ResultRowSetFactory()
        factory.createForDetail(30, "", 27.5).apply {
            assertEquals(category, "")
            assertEquals(bpm, "27.5")
            assertEquals(highSpeed, "× 1.0")
            assertEquals(scrollSpeed, "= 27.5")
        }
    }

    @Test
    fun createForDetail_ScrollSpeedが最小値_BPMが大きい場合() {
        val factory = ResultRowSetFactory()
        factory.createForDetail(30, "", 300.0).apply {
            assertEquals(category, "")
            assertEquals(bpm, "300.0")
            assertEquals(highSpeed, "× 0.25")
            assertEquals(scrollSpeed, "= 75.0")
        }
    }

    @Test
    fun createForDetail_ScrollSpeedが30未満() {
        val factory = ResultRowSetFactory()
        factory.createForDetail(29, "test", 150.0).apply {
            assertEquals(category, "test")
            assertEquals(bpm, "150.0")
            assertEquals(highSpeed, "-")
            assertEquals(scrollSpeed, "-")
        }
    }

    @Test
    fun createForDetail_ScrollSpeedが2000より大きい() {
        val factory = ResultRowSetFactory()
        factory.createForDetail(2001, "test", 150.0).apply {
            assertEquals(category, "test")
            assertEquals(bpm, "150.0")
            assertEquals(highSpeed, "-")
            assertEquals(scrollSpeed, "-")
        }
    }

    @Test
    fun compare_大きい() {
        val factory = ResultRowSetFactory()
        val target = factory.createForDetail(2001, "test", 150.1)
        val compared = factory.createForDetail(2001, "test", 150.0)

        assertTrue(target > compared)
    }

    @Test
    fun compare_小さい() {
        val factory = ResultRowSetFactory()
        val target = factory.createForDetail(30, "test", 15.1)
        val compared = factory.createForDetail(30, "test", 15.11)

        assertTrue(target < compared)
    }

    @Test
    fun compare_等しい() {
        val factory = ResultRowSetFactory()
        val target = factory.createForDetail(30, "test", 2001.11)
        val compared = factory.createForDetail(30, "test", 2001.11)

        assertEquals(target, compared)
    }
}
