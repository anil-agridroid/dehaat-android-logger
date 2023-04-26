package com.dehaat.logger.attributes

object AppInfoAttributeKey {
    const val VERSION_CODE = "version_code"
    const val VERSION_NAME = "version_name"
    const val BUILD_TYPE = "build_type"
}

object ConnectionInfoAttributeKey {
    const val CONNECTED_TO_NETWORK = "connectedToNetwork"
}

object UserInfoAttributeKey {
    const val LOGGED_IN_USER_ID = "loggedInUserId"
    const val LOGGED_IN_USER_NAME = "loggedInUserName"
}

object DeviceInfoAttributeKey {
    const val ANDROID_DEVICE_ID = "android_device_id"
    const val GOOGLE_AD_ID = "google_ad_id"
}