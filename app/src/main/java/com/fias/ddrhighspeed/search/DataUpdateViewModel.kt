package com.fias.ddrhighspeed.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fias.ddrhighspeed.data.IDataVersionDataStore
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.fias.ddrhighspeed.shared.spreadsheet.FailureResult
import com.fias.ddrhighspeed.shared.spreadsheet.ISpreadSheetService
import com.fias.ddrhighspeed.shared.spreadsheet.SuccessResult
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class DataUpdateViewModel(
    private val db: IDatabase,
    private val spreadSheetService: ISpreadSheetService,
    private val versionDataStore: IDataVersionDataStore
) : ViewModel() {

    val updateAvailable: LiveData<Boolean> get() = _updateAvailable
    private val _updateAvailable = MutableLiveData(false)

    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLoading = MutableLiveData(false)

    // localDataVersion にデータを set する場合は setLocalDataVersion を経由すること
    val localDataVersion: LiveData<Int> get() = _localDataVersion
    private val _localDataVersion = MutableLiveData<Int>()
    private suspend fun setLocalDataVersion(version: Int) {
        _localDataVersion.value = version
        versionDataStore.saveDataVersionStore(version)
        setUpdateAvailable()
    }

    init {
        viewModelScope.launch {
            val localVersion = versionDataStore.getDataVersion()
            _localDataVersion.value = localVersion
            if (localVersion == 0) {
                downloadSongData()
            } else {
                checkNewDataVersionAvailable()
            }
        }
    }

    suspend fun checkNewDataVersionAvailable() {
        setUpdateAvailable()
    }

    val errorMessage = MutableLiveData<String>()

    suspend fun downloadSongData() {
        if(_isLoading.value == true) return
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
                db.reinitializeMovies(movies)

//                sourceDataVersion = version
                setLocalDataVersion(version)
            }
        }
//        _baseSongDataList.value = getNewSongsFromDb()
        _isLoading.value = false
    }

    private suspend fun setUpdateAvailable() {
//        _updateAvailable.value = sourceDataVersion > (localDataVersion.value ?: Int.MIN_VALUE)

        val sourceVersion = spreadSheetService.getNewDataVersion()
        val localVersion = versionDataStore.getDataVersion()
        _updateAvailable.value =  sourceVersion > localVersion

    }

}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class DataUpdateViewModelFactory(
    private val db: IDatabase,
    private val spreadSheetService: ISpreadSheetService,
    private val versionDataStore: IDataVersionDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataUpdateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return DataUpdateViewModel(db, spreadSheetService, versionDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}