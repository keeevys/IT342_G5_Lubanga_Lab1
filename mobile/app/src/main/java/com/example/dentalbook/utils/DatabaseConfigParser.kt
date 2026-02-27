package com.example.dentalbook.utils

import android.content.Context
import android.content.res.XmlResourceParser
import com.example.dentalbook.R

object DatabaseConfigParser {
    
    data class ApiConfig(
        val baseUrl: String,
        val loginEndpoint: String,
        val registerEndpoint: String,
        val profileEndpoint: String
    )
    
    fun parseConfig(context: Context): ApiConfig {
        val parser: XmlResourceParser = context.resources.getXml(R.xml.database_config)
        var baseUrl = ""
        var loginEndpoint = ""
        var registerEndpoint = ""
        var profileEndpoint = ""
        
        var eventType = parser.eventType
        var currentTag = ""
        
        while (eventType != XmlResourceParser.END_DOCUMENT) {
            when (eventType) {
                XmlResourceParser.START_TAG -> {
                    currentTag = parser.name
                }
                XmlResourceParser.TEXT -> {
                    when (currentTag) {
                        "base-url" -> baseUrl = parser.text.trim()
                        "login" -> loginEndpoint = parser.text.trim()
                        "register" -> registerEndpoint = parser.text.trim()
                        "profile" -> profileEndpoint = parser.text.trim()
                    }
                }
            }
            eventType = parser.next()
        }
        parser.close()
        
        return ApiConfig(baseUrl, loginEndpoint, registerEndpoint, profileEndpoint)
    }
}
