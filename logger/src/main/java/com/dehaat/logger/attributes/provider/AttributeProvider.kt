package com.dehaat.logger.attributes.provider

import com.dehaat.logger.LoggerArgumentKey
import com.dehaat.logger.infoprovider.ILoggerInfoProvider
import com.dehaat.logger.attributes.AppInfoAttributes
import com.dehaat.logger.attributes.ConnectionInfoAttributes
import com.dehaat.logger.attributes.DeviceInfoAttributes
import com.dehaat.logger.attributes.UserInfoAttributes
import javax.inject.Inject

class AttributeProvider @Inject constructor(private val infoProvider: ILoggerInfoProvider) {

    private fun provideDeviceInfo() = DeviceInfoAttributes(
        androidDeviceId = infoProvider.provideAndroidDeviceId(),
        googleAdId = infoProvider.provideGoogleADId()
    )

    private fun provideAppInfo() = AppInfoAttributes(
        versionCode = infoProvider.provideVersionCode(),
        versionName = infoProvider.provideVersionName(),
        buildType = infoProvider.provideBuildType(),
    )

    private fun provideUserInfo() = UserInfoAttributes(
        userId = infoProvider.provideLoggedInUserId(),
        userName = infoProvider.provideLoggedInUserName()
    )

    private fun provideConnectionInfo() = ConnectionInfoAttributes(
        connectedToNetwork = infoProvider.isConnectedToNetwork(),
    )

    fun getCommonAttributeForEachLog() =
        mutableMapOf<String, Any?>().apply {
            put(LoggerArgumentKey.Log.LOCAL_LOGGING_TIME, System.currentTimeMillis())
            this.putAll(provideDeviceInfo())
            this.putAll(provideUserInfo())
            this.putAll(provideConnectionInfo())
        }
}


