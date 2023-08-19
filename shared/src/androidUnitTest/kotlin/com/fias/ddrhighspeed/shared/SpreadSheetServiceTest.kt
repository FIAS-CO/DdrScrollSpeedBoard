package com.fias.ddrhighspeed.shared

import com.fias.ddrhighspeed.shared.cache.ShockArrowExists
import com.fias.ddrhighspeed.shared.cache.SongName
import com.fias.ddrhighspeed.shared.cache.SongProperty
import com.fias.ddrhighspeed.shared.cache.WebMusicId
import com.fias.ddrhighspeed.shared.spreadsheet.TestSpreadSheetService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class SpreadSheetServiceTest {
    lateinit var spreadSheetService: TestSpreadSheetService

    // TODO テスト関連の文字列をどこかに集約したい
    private val correctUrlBase =
        "https://docs.google.com/spreadsheets/d/1BPqdsURvQYveHLNhREJIUC440TFPj5JzwizKkhdIKQY/export?format=tsv&gid="

    @Before
    fun setup() {
        spreadSheetService = TestSpreadSheetService(correctUrlBase)
    }

    @After
    fun teardown() {
        spreadSheetService.closeClient()
    }

    @Test
    fun testFetchFileVersion() {
        runBlocking {
            launch {
                assertTrue(
                    spreadSheetService.fetchFileVersion().startsWith("2023061121")
                )
            }
        }
    }

    @Test
    fun testCreateSongNames() {
        val firstSongName = SongName(
            0, "MAKE IT BETTER", "MAKE IT BETTER", "1st",
            119, 119, 3, 7, 9, 11, 0,
            7, 9, 11, 0
        )
        val lastSongName = SongName(
            1242, "メンタンピンドラドラ", "menntannpinndoradora", "A3",
            93, 185, 2, 7, 11, 16, 0, 7, 11, 16, 0
        )
        runBlocking {
            launch {
                val songNames = spreadSheetService.createSongNames()
                assertEquals(1195, songNames.size)
                assertEquals(firstSongName, songNames[0])
                assertEquals(lastSongName, songNames[1194])
            }
        }
    }

    @Test
    fun testFetchSongNames() {
        runBlocking {
            launch {
                val fetchSongNames = spreadSheetService.fetchSongNames()
                assertTrue(
                    fetchSongNames.startsWith(
                        "0\tMAKE IT BETTER\tMAKE IT BETTER\t1st\t119\t119\t3\t7\t9\t11\t0\t7\t9\t11\t0"
                    )
                )
                assertTrue(
                    fetchSongNames.endsWith(
                        "1242\tメンタンピンドラドラ\tmenntannpinndoradora\tA3\t93\t185\t2\t7\t11\t16\t0\t7\t11\t16\t0"
                    )
                )
            }
        }
    }

    @Test
    fun testCreateShockArrowExists() {
        val firstShock = ShockArrowExists(0, "xx")
        val no966Shock = ShockArrowExists(966, "11")
        val lastShock = ShockArrowExists(1242, "xx")
        runBlocking {
            launch {
                val shocks = spreadSheetService.createShockArrowExists()
                assertEquals(1195, shocks.size)
                assertEquals(firstShock, shocks[0])
                assertEquals(no966Shock, shocks[918])
                assertEquals(lastShock, shocks[1194])
            }
        }
    }

    @Test
    fun testFetchShockArrowExists() {
        runBlocking {
            launch {
                val shockArrows = spreadSheetService.fetchShockArrowExists()
                assertTrue(
                    shockArrows.startsWith("0\txx") &&
                            shockArrows.endsWith("1242\txx")
                )
            }
        }
    }

    @Test
    fun testCreateWebMusicIds() {
        val firstId = WebMusicId(0, "8Il6980di8P89lil1PDIqqIbiq1QO8lQ", "MAKE IT BETTER", 0)
        val lastId = WebMusicId(1242, "8q96ObOdqPq8blPob9QdDq9b1iQoIQOI", "メンタンピンドラドラ", 0)
        runBlocking {
            launch {
                val ids = spreadSheetService.createWebMusicIds()
                assertEquals(1195, ids.size)
                assertEquals(firstId, ids[0])
                assertEquals(lastId, ids[1194])
            }
        }
    }

    @Test
    fun testFetchWebMusicIds() {
        runBlocking {
            launch {
                val fetchWebMusicIds = spreadSheetService.fetchWebMusicIds()
                assertTrue(
                    fetchWebMusicIds.startsWith(
                        "0\t8Il6980di8P89lil1PDIqqIbiq1QO8lQ\tMAKE IT BETTER\t0"
                    )
                )
                assertTrue(
                    fetchWebMusicIds.endsWith(
                        "1242\t8q96ObOdqPq8blPob9QdDq9b1iQoIQOI\tメンタンピンドラドラ\t0"
                    )
                )
            }
        }
    }

    @Test
    fun testCreateMusicProperties() {
        val firstProp = SongProperty(0, 0, "mitsu-O!", 119.0, 0.0, 0.0, 0.0, "")
        val lastProp =
            SongProperty(1195, 1238, "jun feat.Godis (Heather Twede)", 157.0, 0.0, 0.0, 0.0, "Test")
        runBlocking {
            launch {
                val props = spreadSheetService.createMusicProperties()
                assertEquals(1196, props.size)
                assertEquals(firstProp, props[0])
                assertEquals(lastProp, props[1195])
            }
        }
    }

    @Test
    fun testFetchMusicProperties() {
        runBlocking {
            launch {
                val fetchWebMusicIds = spreadSheetService.fetchSongProperties()
                assertTrue(
                    fetchWebMusicIds.startsWith(
                        "id\tsong_id\tcomposer\tbase_bpm\tsub_bpm\tmin_bpm\tmax_bpm\tdifficulty_label\tMusicNamesのB列"
                    )
                )
                // 1195	1238	jun feat.Godis (Heather Twede)	157	0	0	0	Test	Racing with Time (NAOKI's 999 remix)
                assertTrue(
                    fetchWebMusicIds.endsWith(
                        "1195\t1238\tjun feat.Godis (Heather Twede)\t157\t0\t0\t0\tTest\tRacing with Time (NAOKI's 999 remix)"
                    )
                )
            }
        }
    }

    @Test
    fun testException() {
        assertThrows(RuntimeException::class.java) {
            runBlocking {
                launch {
                    spreadSheetService.fetchData("https://dummy.google.com/spreadsheets/d/1US3RPohSR_rlraBqq")
                }
            }
        }
    }
}
