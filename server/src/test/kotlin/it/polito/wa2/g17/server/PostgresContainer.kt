package it.polito.wa2.g17.server

import org.testcontainers.containers.PostgreSQLContainer

object PostgresContainer {
    val instance: PostgreSQLContainer<Nothing> by lazy {
        PostgreSQLContainer<Nothing>("postgres:latest").apply { start() }
    }
}