package com.dehaat.logger.attributes.provider

import com.dehaat.logger.infoprovider.ILoggerInfoProvider
import com.dehaat.logger.attributes.AppInfoAttributes
import com.dehaat.logger.attributes.ConnectionInfoAttributes
import com.dehaat.logger.attributes.DeviceInfoAttributes
import com.dehaat.logger.attributes.UserInfoAttributes
import javax.inject.Inject

class AttributeProvider @Inject constructor(private val infoProvider: ILoggerInfoProvider) {

    fun provideDeviceInfo() = DeviceInfoAttributes(
        androidDeviceId = infoProvider.provideAndroidDeviceId(),
        googleAdId = infoProvider.provideGoogleADId()
    )

    fun provideAppInfo() = AppInfoAttributes(
        versionCode = infoProvider.provideVersionCode(),
        versionName = infoProvider.provideVersionName(),
        buildType = infoProvider.provideBuildType(),
    )

    fun provideUserInfo() = UserInfoAttributes(
        userId = infoProvider.provideLoggedInUserId(),
        userName = infoProvider.provideLoggedInUserName()
    )

    fun provideConnectionInfo() = ConnectionInfoAttributes(
        connectedToNetwork = infoProvider.isConnectedToNetwork(),
    )
}


