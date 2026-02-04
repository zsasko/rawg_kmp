package com.zsasko.rawg_kmp.di

import com.joel.ktorfitdemo.core.di.modules.RepositoryModule
import com.joel.ktorfitdemo.core.di.modules.UseCaseModule
import com.joel.ktorfitdemo.core.di.modules.ViewModelModule
import com.joel.ktorfitdemo.core.di.modules.customViewModelModule
import com.zsasko.rawg_kmp.di.modules.DaoModule
import com.zsasko.rawg_kmp.di.modules.NetworkModule
import com.zsasko.rawg_kmp.platformDbModule
import org.koin.core.KoinApplication
import org.koin.core.annotation.Module
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module

fun initApplication(appDeclaration: KoinAppDeclaration = {}): KoinApplication {
    return startKoin {
        appDeclaration()
        modules(
            platformDbModule(),
            AppModule().module,
            customViewModelModule()
        )
    }
}

@Module(
    includes = [
        NetworkModule::class,
        UseCaseModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
        DaoModule::class
    ]
)
class AppModule

@Suppress("unused") //using in iOS
fun initKoin() = initApplication {}