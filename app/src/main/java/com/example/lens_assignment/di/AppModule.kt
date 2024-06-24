package com.example.lens_assignment.di

import android.content.Context
import androidx.room.Room
import com.example.lens_assignment.data.local.database.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context : Context) = Room.databaseBuilder(context,TaskDatabase::class.java,"TaskDatabase").fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun provideNoteDao(db:TaskDatabase) = db.taskDao()


}