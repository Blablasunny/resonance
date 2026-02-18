package com.example.resonance.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class ResonanceLogger(cl: Class<*>?) {
    private val logger: Logger = LoggerFactory.getLogger(cl)

    fun error(msg: String?) {
        logger.error("\u001B[31m$msg\u001B[0m")
    }
}