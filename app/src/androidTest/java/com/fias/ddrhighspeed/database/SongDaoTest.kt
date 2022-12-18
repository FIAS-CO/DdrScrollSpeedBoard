package com.fias.ddrhighspeed.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SongDaoTest {
    private lateinit var songDao: SongDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        songDao = db.songDao()

        songDao.insert(
            listOf(
                Song(
                    0,
                    "name1",
                    null,
                    "version1",
                    "100",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                ),
                Song(
                    0,
                    "name2",
                    "comp2",
                    "version2",
                    "200",
                    -1.0,
                    1555.0,
                    100.0,
                    200.5,
                    12,
                    13,
                    14,
                    15,
                    16,
                    17,
                    18,
                    19,
                    20,
                    "Test",
                    1
                )
            )
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun search_MultiHit() {
        val byName = songDao.getByNameContainWord("name")
        assertThat(byName.size).isEqualTo(2)
        byName[0].apply {
            assertThat((id)).isEqualTo(1)
            assertThat(name).isEqualTo("name1")
            assertThat(composer).isEqualTo(null)
            assertThat(version).isEqualTo("version1")
            assertThat(displayBpm).isEqualTo("100")
            assertThat(minBpm).isNull()
            assertThat(maxBpm).isNull()
            assertThat(baseBpm).isNull()
            assertThat(subBpm).isNull()
            assertThat(besp).isNull()
            assertThat(bsp).isNull()
            assertThat(dsp).isNull()
            assertThat(esp).isNull()
            assertThat(csp).isNull()
            assertThat(bdp).isNull()
            assertThat(edp).isNull()
            assertThat(cdp).isNull()
            assertThat(shockArrow).isNull()
            assertThat(deleted).isNull()
        }
        byName[1].apply {
            assertThat((id)).isEqualTo(2)
            assertThat(name).isEqualTo("name2")
            assertThat(composer).isEqualTo("comp2")
            assertThat(version).isEqualTo("version2")
            assertThat(displayBpm).isEqualTo("200")
            assertThat(minBpm).isEqualTo(-1.0)
            assertThat(maxBpm).isEqualTo(1555.0)
            assertThat(baseBpm).isEqualTo(100.0)
            assertThat(subBpm).isEqualTo(200.5)
            assertThat(besp).isEqualTo(12)
            assertThat(bsp).isEqualTo(13)
            assertThat(dsp).isEqualTo(14)
            assertThat(esp).isEqualTo(15)
            assertThat(csp).isEqualTo(16)
            assertThat(bdp).isEqualTo(17)
            assertThat(ddp).isEqualTo(18)
            assertThat(edp).isEqualTo(19)
            assertThat(cdp).isEqualTo(20)
            assertThat(shockArrow).isEqualTo("Test")
            assertThat(deleted).isEqualTo(1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun search_SingleHit() {
        val byName = songDao.getByNameContainWord("2")
        assertThat(byName.size).isEqualTo(1)
        byName[0].apply {
            assertThat((id)).isEqualTo(2)
            assertThat(name).isEqualTo("name2")
            assertThat(composer).isEqualTo("comp2")
            assertThat(version).isEqualTo("version2")
            assertThat(displayBpm).isEqualTo("200")
            assertThat(minBpm).isEqualTo(-1.0)
            assertThat(maxBpm).isEqualTo(1555.0)
            assertThat(baseBpm).isEqualTo(100.0)
            assertThat(subBpm).isEqualTo(200.5)
            assertThat(besp).isEqualTo(12)
            assertThat(bsp).isEqualTo(13)
            assertThat(dsp).isEqualTo(14)
            assertThat(esp).isEqualTo(15)
            assertThat(csp).isEqualTo(16)
            assertThat(bdp).isEqualTo(17)
            assertThat(ddp).isEqualTo(18)
            assertThat(edp).isEqualTo(19)
            assertThat(cdp).isEqualTo(20)
            assertThat(shockArrow).isEqualTo("Test")
            assertThat(deleted).isEqualTo(1)
        }
    }

    @Test
    @Throws(Exception::class)
    fun search_NoHit() {
        val byName = songDao.getByNameContainWord("NoHit")
        assertThat(byName.size).isEqualTo(0)
    }

    @Test
    @Throws(Exception::class)
    fun search_SearchBlank_AllHit() {
        val byName = songDao.getByNameContainWord("")
        assertThat(byName.size).isEqualTo(2)
    }
}