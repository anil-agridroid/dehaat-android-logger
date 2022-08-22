package com.dehaat.logger.attributes

class UserInfoAttributes(
    userId: String,
    userName: String,
) : LinkedHashMap<String, String>() {

    init {
        put(UserInfoAttributeKey.LOGGED_IN_USER_ID, userId)
        put(UserInfoAttributeKey.LOGGED_IN_USER_NAME, userName)
    }
}