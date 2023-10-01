package com.fias.ddrhighspeed.search.coursesearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fias.ddrhighspeed.CourseData
import com.fias.ddrhighspeed.search.convertToCourseData
import com.fias.ddrhighspeed.shared.cache.IDatabase

class EstimateCourseViewModel(
    private val db: IDatabase
) : ViewModel() {
    val searchWord = MutableLiveData<String>()

    val baseCourseDataList: LiveData<List<CourseData>> get() = _baseCourseDataList
    private val _baseCourseDataList = MutableLiveData<List<CourseData>>()

    init {
        loadAllCourses()
    }

    fun loadAllCourses() {
        _baseCourseDataList.value = getNewCoursesFromDb()
    }

    fun searchSongsByCourse(): List<CourseData> {
        val word = searchWord.value ?: ""

        val songData = baseCourseDataList.value ?: listOf()
        return if (word == "") songData
        else songData.filter { it.name.contains(word, true) }
    }

    fun resetSearchWord() {
        searchWord.value = ""
    }

    private fun getNewCoursesFromDb(): List<CourseData> {
        return db.getNewCourses().map { it.convertToCourseData() }
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class EstimateCourseViewModelFactory(
    private val db: IDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstimateCourseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return EstimateCourseViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}