package org.ivangrod.circuit_breaker.controller

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

@RestController
@RequestMapping(value = ["/api"])
class CircuitBreakerController {

    private val log: Logger = LoggerFactory.getLogger(CircuitBreakerController::class.java)


    @GetMapping(value = ["/timeDelay/{delay}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @CircuitBreaker(name = RESILIENCE4J_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
    fun timeDelay(@PathVariable delay: Int): ResponseEntity<Boolean> {

        val now = Instant.now()
        while (Instant.now().isBefore(now.plus(delay.toLong(), ChronoUnit.SECONDS))){
            continue
        }

        log.info("Executing after [$delay]")
        return toOkResponse()
    }

    fun fallback(ex: Exception?): ResponseEntity<Boolean> {
        return toResponse(HttpStatus.INTERNAL_SERVER_ERROR, java.lang.Boolean.FALSE)
    }

    private fun toOkResponse(): ResponseEntity<Boolean> {
        return toResponse(HttpStatus.OK, true)
    }

    private fun toResponse(httpStatus: HttpStatus, result: Boolean): ResponseEntity<Boolean> {
        return ResponseEntity.status(httpStatus).body(result)
    }

    companion object {
        private const val RESILIENCE4J_INSTANCE_NAME = "my-app"
        private const val FALLBACK_METHOD = "fallback"
    }
}
