package com.dehaat.logger.infoprovider

interface ICommonMessageMapProvider {
    fun provideCommonMapForEachLogMessage(): Map<String, String>
}