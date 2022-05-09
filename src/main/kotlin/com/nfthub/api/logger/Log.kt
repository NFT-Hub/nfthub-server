package com.nfthub.api.logger

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nfthub.api.EMPTY_STRING
import java.time.LocalDateTime

sealed class AbstractLog(
    open var date: String,
    open var resStatus: Int = 0,
    open var method: String = "",
    open var requestUrl: String = "",
    open var userIp: String = "",
    open var userAgent: String = ""
) {
    companion object {
        val mapper = jacksonObjectMapper()
    }

    fun serialize(): String {
        return mapper.writeValueAsString(this)
    }
}

data class InfoLog(
    override var date: String = LocalDateTime.now().toString(),
    override var resStatus: Int = 0,
    override var method: String = "",
    override var requestUrl: String = "",
    override var userIp: String = "",
    override var userAgent: String = ""
) : AbstractLog(date, resStatus, method, requestUrl, userIp, userAgent)

data class ErrorLog(
    override var date: String = LocalDateTime.now().toString(),
    override var resStatus: Int = 0,
    override var method: String = "",
    override var requestUrl: String = "",
    override var userIp: String = "",
    override var userAgent: String = "",
    var exception: String = EMPTY_STRING,
    var message: String? = EMPTY_STRING
) : AbstractLog(date, resStatus, method, requestUrl, userIp, userAgent)

data class InternalServerErrorLog(
    override var date: String = LocalDateTime.now().toString(),
    override var resStatus: Int = 0,
    override var method: String = "",
    override var requestUrl: String = "",
    override var userIp: String = "",
    override var userAgent: String = "",
    var exception: String = EMPTY_STRING,
    var message: String? = EMPTY_STRING,
    var errorStack: String = EMPTY_STRING
) : AbstractLog(date, resStatus, method, requestUrl, userIp, userAgent)
