package com.fias.ddrhighspeed.search.coursesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fias.ddrhighspeed.search.songsearch.EstimateByNameViewModel
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.fias.ddrhighspeed.shared.spreadsheet.ISpreadSheetService

class EstimateCourseViewModel {
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class EstimateCourseViewModelFactory(
    private val db: IDatabase, private val spreadSheetService: ISpreadSheetService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstimateByNameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return EstimateByNameViewModel(db, spreadSheetService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}