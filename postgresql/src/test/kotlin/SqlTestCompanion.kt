package ru.otus.otuskotlin.marketplace.backend.repo.sql

import RepoProfileSQL
import SqlProperties
import com.benasher44.uuid.uuid4
import org.testcontainers.containers.PostgreSQLContainer
import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import java.time.Duration

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "marketplace-pass"
    private const val SCHEMA = "marketplace"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(
        initObjects: Collection<MkplProfile> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): RepoProfileSQL {
        return RepoProfileSQL(SqlProperties(url, USER, PASS, SCHEMA, dropDatabase = true),
            initObjects, randomUuid = randomUuid)
    }
}
