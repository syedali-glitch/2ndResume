package com.resumearchitect.di

import android.content.Context
import com.resumearchitect.export.ExportManager
import com.resumearchitect.data.repository.ResumeRepository
import com.resumearchitect.pdf.PdfGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun providePdfGenerator(
        @ApplicationContext context: Context
    ): PdfGenerator {
        return PdfGenerator(context)
    }
    
    @Provides
    @Singleton
    fun provideExportManager(
        @ApplicationContext context: Context,
        pdfGenerator: PdfGenerator,
        repository: ResumeRepository
    ): ExportManager {
        return ExportManager(context, pdfGenerator, repository)
    }
}
