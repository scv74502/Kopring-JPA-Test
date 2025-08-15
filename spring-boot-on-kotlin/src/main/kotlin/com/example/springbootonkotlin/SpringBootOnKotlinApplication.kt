package com.example.springbootonkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootOnKotlinApplication

fun main(args: Array<String>) {
    runApplication<SpringBootOnKotlinApplication>(*args)
}
