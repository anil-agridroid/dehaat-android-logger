package com.dehaat.logger.infoprovider

interface IUserInfoProvider {
    fun provideLoggedInUserName(): String
    fun provideLoggedInUserId(): String
}