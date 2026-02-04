package com.zsasko.rawg_kmp

import com.joel.ktorfitdemo.core.di.modules.customViewModelModule
import com.zsasko.rawg_kmp.di.AppModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module

/**
 * Initializes koin on iOS platform.
 * Please invoke 'initialize' method in app delegate classes to start koin.
 */
object IosKoinHelper {
    fun initialize(appDeclaration: KoinAppDeclaration = {}) {
        startKoin {
            appDeclaration()
            modules(
                platformDbModule(),
                customViewModelModule(),
                AppModule().module
            )
        }
    }
}