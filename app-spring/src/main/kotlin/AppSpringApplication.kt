package fr.mustafin.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AppSpringApplication

fun main(args: Array<String>) {
	runApplication<AppSpringApplication>(*args)
}
