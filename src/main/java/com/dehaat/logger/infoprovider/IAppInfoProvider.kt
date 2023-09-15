package com.dehaat.logger.infoprovider

interface IAppInfoProvider {
    fun provideVersionCode(): String
    fun provideVersionName(): String
    fun provideBuildType(): String
}