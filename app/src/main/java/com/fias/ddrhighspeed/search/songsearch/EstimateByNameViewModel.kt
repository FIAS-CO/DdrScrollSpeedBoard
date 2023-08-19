package com.fias.ddrhighspeed.search.songsearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.fias.ddrhighspeed.shared.cache.Song
import com.fias.ddrhighspeed.shared.spreadsheet.FailureResult
import com.fias.ddrhighspeed.shared.spreadsheet.ISpreadSheetService
import com.fias.ddrhighspeed.shared.spreadsheet.SuccessResult
import kotlinx.coroutines.coroutineScope

// TODO:テストを足す
class EstimateByNameViewModel(
    private val db: IDatabase, private val spreadSheetService: ISpreadSheetService
) : ViewModel() {
    val searchWord = MutableLiveData<String>()

    val updateAvailable: LiveData<Boolean> get() = _updateAvailable
    private val _updateAvailable = MutableLiveData(false)

    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLoading = MutableLiveData(false)

    val localDataVersion: LiveData<Int> get() = _localDataVersion
    private val _localDataVersion = MutableLiveData<Int>()
    private fun setLocalDataVersion(version: Int) {
        _localDataVersion.value = version
        setUpdateAvailable()
    }

    val songDataList: LiveData<List<SongData>> get() = _songDataList
    private val _songDataList = MutableLiveData<List<SongData>>()

    var sourceDataVersion = 0
        private set(value) {
            field = value
            setUpdateAvailable()
        }

    init {
        _songDataList.value = getNewSongs()
    }

    fun getNewSongs(): List<SongData> {
        return db.getNewSongs().map { convertToSongData(it) }
    }

    fun setSongDataListBySearchWord() {
        _songDataList.value = searchSongsByName()
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
                deleted,
                difficulty_label ?: ""
            )
        }
    }

    suspend fun checkNewDataVersionAvailable(localVersion: Int) {
        setLocalDataVersion(localVersion)

        val dataVersionResult = spreadSheetService.getNewDataVersion()

        // データ更新後に内部バージョンを書き換えるために値を残しておく
        sourceDataVersion = sourceDataVersion.coerceAtLeast(dataVersionResult)
    }

    val errorMessage = MutableLiveData<String>()
    suspend fun downloadSongData() {
        coroutineScope {
            _isLoading.value = true

            val allDataResult = spreadSheetService.createAllData()

            // いずれかのリストの取得が失敗したら中断
            if (allDataResult is FailureResult) {
                errorMessage.value =
                    "データの取得に失敗しました。\nしばらく後に再実施していただくか、左上アイコンまたはTwitter(@sig_re)から開発にご連絡ください。"
                Log.e(
                    "EstimateByNameViewModel", "データの取得に失敗しました", allDataResult.exceptions.first()
                )
                return@coroutineScope
            }

            (allDataResult as SuccessResult).apply {
                // 各リストのサイズが違うのはおかしいので中断
                // musicPropertiesだけはsongNamesよりレコードが多いのでそこだけ判定
                if (songNames.size > musicProperties.size || songNames.size != shockArrowExists.size || songNames.size != webMusicIds.size) {
                    errorMessage.value =
                        "データに不整合があります。\n左上アイコンまたはTwitter(@sig_re)から開発にご連絡ください。"
                    return@coroutineScope
                }

                db.reinitializeSongNames(songNames)
                db.reinitializeSongProperties(musicProperties)
                db.reinitializeShockArrowExists(shockArrowExists)
                db.reinitializeWebMusicIds(webMusicIds)

                sourceDataVersion = version
                setLocalDataVersion(version)
            }
        }
        _songDataList.value = searchSongsByName()
        _isLoading.value = false
    }

    private fun setUpdateAvailable() {
        _updateAvailable.value = sourceDataVersion > (localDataVersion.value ?: Int.MIN_VALUE)
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class EstimateByNameViewModelFactory(
    private val db: IDatabase, private val spreadSheetService: ISpreadSheetService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstimateByNameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return EstimateByNameViewModel(db, spreadSheetService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}