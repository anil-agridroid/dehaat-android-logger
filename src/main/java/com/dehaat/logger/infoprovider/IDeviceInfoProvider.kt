package com.dehaat.logger.infoprovider

interface IDeviceInfoProvider {
    fun provideAndroidDeviceId(): String
    fun provideGoogleADId(): String
}