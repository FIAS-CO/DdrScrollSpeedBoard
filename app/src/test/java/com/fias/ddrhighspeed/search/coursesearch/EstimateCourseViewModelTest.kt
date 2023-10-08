package com.fias.ddrhighspeed.search.coursesearch

import com.fias.ddrhighspeed.CourseData
import com.fias.ddrhighspeed.shared.cache.Course
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class EstimateCourseViewModelTest {
    private lateinit var viewModel: EstimateCourseViewModel

    @Before
    fun setUp() {
        val mockDatabase = mock<IDatabase>()

        val mockSongs = listOf(
            Course(
                1, "Test Course 1", 1,
                1, 2, 3, 4, 5,
                6, 7, 8, 1
            ),
            Course(
                1, "Test Course 2", 0,
                1, 2, 3, 4, 5,
                6, 7, 8, 0
            ),
        )
        whenever(mockDatabase.getNewCourses()).thenReturn(mockSongs)

        viewModel = EstimateCourseViewModel(mockDatabase)
    }

    @Test
    fun searchSongsByName_returns_correct_list_when_searchWord_is_not_empty() {
        viewModel.searchWord.value = "Course 1"

        // When
        val result = viewModel.searchSongsByCourse()

        val expected = listOf(
            CourseData(
                1, "Test Course 1", true,
                1, 2, 3, 4, 5,
                6, 7, 8, true
            ),
        )
        // Then
        assertEquals(expected, result)
    }

    @Test
    fun searchSongsByName_returns_correct_list_when_searchWord_is_empty() {
        viewModel.searchWord.value = ""

        // When
        val result = viewModel.searchSongsByCourse()

        val expected = listOf(
            CourseData(
                1, "Test Course 1", true,
                1, 2, 3, 4, 5,
                6, 7, 8, true
            ),
            CourseData(
                1, "Test Course 2", false,
                1, 2, 3, 4, 5,
                6, 7, 8, false
            ),
        )
        // Then
        assertEquals(expected, result)
    }

    @Test
    fun resetSearchWord() {
        viewModel.searchWord.value = "test"

        // When
        viewModel.resetSearchWord()

        // Then
        assertEquals("", viewModel.searchWord.value)
    }
}