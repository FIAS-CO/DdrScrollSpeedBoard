package com.fias.ddrhighspeed.search.songsearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.data.DataVersionDataStore
import com.fias.ddrhighspeed.shared.SpreadSheetUtil
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.fias.ddrhighspeed.shared.cache.Song
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.nio.channels.UnresolvedAddressException
// TODO:テストを足す
class EstimateByNameViewModel(private val db: IDatabase) : ViewModel() {
    val searchWord = MutableLiveData<String>()
    private val _result = MutableStateFlow<Boolean?>(null)
    val result: StateFlow<Boolean?> get() = _result

    var sourceDataVersion = 0

    init {
        viewModelScope.launch {
            sourceDataVersion = getNewDataVersion()
        }
    }

    fun getNewSongs(): List<SongData> {
        return db.getNewSongs().map { convertToSongData(it) }
    }

    fun searchSongsByName(): List<SongData> {
        val word = searchWord.value ?: ""

        return if (word == "") getNewSongs()
        else db.searchSongsByName(word).map { convertToSongData(it) }
    }

    fun resetSearchWord() {
        searchWord.value = ""
    }

    private fun convertToSongData(song: Song): SongData {
        with(song) {
            return SongData(
                id,
                name,
                composer,
                version,
                display_bpm,
                min_bpm,
                max_bpm,
                base_bpm,
                sub_bpm,
                besp,
                bsp,
                dsp,
                esp,
                csp,
                bdp,
                ddp,
                edp,
                cdp,
                shock_arrow,
                deleted
            )
        }
    }

    fun checkNewDataVersionAvailable(localVersion: Int) {
        // データ更新後に内部バージョンを書き換えるために値を残しておく
        viewModelScope.launch {
            val dataVersionResult = safeAsync { getNewDataVersion() }.await()
            if (dataVersionResult.isFailure) {
                return@launch
            }
            sourceDataVersion = dataVersionResult.getOrNull() ?: 0
            _result.value = sourceDataVersion > localVersion
        }
    }

    // TODO：resultつかう
    private suspend fun getNewDataVersion(): Int {
        var sourceVersion = -1
        try {
            val version = SpreadSheetUtil().fetchFileVersion()
            sourceVersion = version.toInt()
        } catch (_: NumberFormatException) {
            // なにもしない
        } catch (_: UnresolvedAddressException) {
            // なにもしない
        }

        return sourceVersion
    }

    val errorMessage = MutableLiveData<String>()
    suspend fun downloadSongData(versionDataStore: DataVersionDataStore) {
        coroutineScope {
            val songNameDeferred = safeAsync { SpreadSheetUtil().createSongNames() }
            val musicPropertyDeferred = safeAsync { SpreadSheetUtil().createMusicProperties() }
            val shockArrowDeferred = safeAsync { SpreadSheetUtil().createShockArrowExists() }
            val webMusicIdDeferred = safeAsync { SpreadSheetUtil().createWebMusicIds() }
            val songNamesResult = songNameDeferred.await()
            val musicPropertiesResult = musicPropertyDeferred.await()
            val shockArrowExistsResult = shockArrowDeferred.await()
            val webMusicIdsResult = webMusicIdDeferred.await()

            // いずれかのリストの取得が失敗したら中断
            if (songNamesResult.isFailure || musicPropertiesResult.isFailure || shockArrowExistsResult.isFailure || webMusicIdsResult.isFailure) {
                // TODO エラーメッセージ
                return@coroutineScope
            }

            val songNames = songNamesResult.getOrNull() ?: emptyList()
            val musicProperties = musicPropertiesResult.getOrNull() ?: emptyList()
            val shockArrowExists = shockArrowExistsResult.getOrNull() ?: emptyList()
            val webMusicIds = webMusicIdsResult.getOrNull() ?: emptyList()

            // 各リストのサイズが違うのはおかしいので中断
            if (songNames.size != musicProperties.size || musicProperties.size != shockArrowExists.size || shockArrowExists.size != webMusicIds.size) {
                // TODO エラーメッセージ
                return@coroutineScope
            }

            db.reinitializeSongNames(songNames)
            db.reinitializeSongProperties(musicProperties)
            db.reinitializeShockArrowExists(shockArrowExists)
            db.reinitializeWebMusicIds(webMusicIds)

            versionDataStore.saveDataVersionStore(sourceDataVersion)
        }
    }
}

private suspend fun <T> safeAsync(block: suspend () -> T): Deferred<Result<T>> {
    return coroutineScope { async { runCatching { block() } } }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class EstimateByNameViewModelFactory(private val db: IDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstimateByNameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EstimateByNameViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}