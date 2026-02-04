package com.joel.ktorfitdemo.core.di.modules

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.zsasko.rawg_kmp.data.domain.repository")
class RepositoryModule