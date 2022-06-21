package org.ivangrod.circuit_breaker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(value = [Config::class])
class CircuitBreakerApplication

fun main(args: Array<String>) {
	runApplication<CircuitBreakerApplication>(*args)
}
