package com.fias.ddrhighspeed.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("NonAsciiCharacters", "TestFunctionName")
@RunWith(AndroidJUnit4::class)
class ResultRowSetFactoryTestForDetail {

    companion object {
        private lateinit var factory: ResultRowSetFactory

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            factory = ResultRowSetFactory()
        }
    }

    @Test
    fun createForDetail() {
        factory.createForDetail(400, "test", 150.0).apply {
            assertThat(category).isEqualTo("test")
            assertThat(bpm).isEqualTo("150.0")
            assertThat(highSpeed).isEqualTo("× 2.5")
            assertThat(scrollSpeed).isEqualTo("= 375.0")
        }
    }

    @Test
    fun createForDetail_ScrollSpeedが最大値() {
        factory.createForDetail(2000, "", 300.0).apply {
            assertThat(category).isEqualTo("")
            assertThat(bpm).isEqualTo("300.0")
            assertThat(highSpeed).isEqualTo("× 6.5")
            assertThat(scrollSpeed).isEqualTo("= 1950.0")
        }
    }

    @Test
    fun createForDetail_ScrollSpeedが最小値() {
        factory.createForDetail(30, "", 27.5).apply {
            assertThat(category).isEqualTo("")
            assertThat(bpm).isEqualTo("27.5")
            assertThat(highSpeed).isEqualTo("× 1.0")
            assertThat(scrollSpeed).isEqualTo("= 27.5")
        }
    }

    @Test
    fun createForDetail_ScrollSpeedが最小値_BPMが大きい場合() {
        factory.createForDetail(30, "", 300.0).apply {
            assertThat(category).isEqualTo("")
            assertThat(bpm).isEqualTo("300.0")
            assertThat(highSpeed).isEqualTo("× 0.25")
            assertThat(scrollSpeed).isEqualTo("= 75.0")
        }
    }

    @Test
    fun createForDetail_ScrollSpeedが30未満() {
        factory.createForDetail(29, "test", 150.0).apply {
            assertThat(category).isEqualTo("test")
            assertThat(bpm).isEqualTo("150.0")
            assertThat(highSpeed).isEqualTo("-")
            assertThat(scrollSpeed).isEqualTo("-")
        }
    }

    @Test
    fun createForDetail_ScrollSpeedが2000より大きい() {
        factory.createForDetail(2001, "test", 150.0).apply {
            assertThat(category).isEqualTo("test")
            assertThat(bpm).isEqualTo("150.0")
            assertThat(highSpeed).isEqualTo("-")
            assertThat(scrollSpeed).isEqualTo("-")
        }
    }

    @Test
    fun compare_大きい() {
        val target = factory.createForDetail(2001, "test", 150.1)
        val compared = factory.createForDetail(2001, "test", 150.0)

        assertThat(target.compareTo(compared)).isGreaterThan(0)
    }

    @Test
    fun compare_小さい() {
        val target = factory.createForDetail(30, "test", 15.1)
        val compared = factory.createForDetail(30, "test", 15.11)

        assertThat(target.compareTo(compared)).isLessThan(0)
    }

    @Test
    fun compare_等しい() {
        val target = factory.createForDetail(30, "test", 2001.11)
        val compared = factory.createForDetail(30, "test", 2001.11)

        assertThat(target.compareTo(compared)).isEqualTo(0)
    }
}