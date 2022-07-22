package com.ficko.rssfeed.data.local.database.dao

import android.os.Build
import androidx.room.Room
import com.ficko.rssfeed.data.local.database.FavoritesDatabase
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.M])
abstract class BaseDaoTest {

    protected lateinit var testDatabase: FavoritesDatabase

    @Before
    fun setUp() {
        val context = RuntimeEnvironment.getApplication()
        testDatabase = Room.inMemoryDatabaseBuilder(context, FavoritesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        testDatabase.close()
    }
}