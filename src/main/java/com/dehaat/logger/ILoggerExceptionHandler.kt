package com.dehaat.logger

interface ILoggerExceptionHandler{
    fun onExceptionCatch(ex: DehaatLoggerException)
}