package fr.mustafin.demo.config

import MkplAppSettings
import ProfileRepoInMemory
import ProfileRepoStub
import RepoProfileSQL
import SqlProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.marketplace.biz.MkplProfileProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.repo.IProfileRepository

@Configuration
@EnableConfigurationProperties(SqlPropertiesEx::class)
class AppConfig {

    @Bean
    fun processor(corSettings: MkplCorSettings) = MkplProfileProcessor(corSettings)


    @Bean
    fun corSettings(
        @Qualifier("prodRepository") prodRepository: IProfileRepository?,
        @Qualifier("testRepository") testRepository: IProfileRepository,
        @Qualifier("stubRepository") stubRepository: IProfileRepository,
    ): MkplCorSettings = MkplCorSettings(
        repoStub = stubRepository,
        repoTest = testRepository,
        repoProd = prodRepository ?: testRepository,
    )

    @Bean
    fun appSettings(corSettings: MkplCorSettings, processor: MkplProfileProcessor) = MkplAppSettings(
        processor = processor,
        corSettings = corSettings
    )

    @Bean(name = ["prodRepository"])
    @ConditionalOnProperty(value = ["prod-repository"], havingValue = "sql")
    fun prodRepository(sqlProperties: SqlProperties) = RepoProfileSQL(sqlProperties)

    @Bean
    fun testRepository() = ProfileRepoInMemory()

    @Bean
    fun stubRepository() = ProfileRepoStub()
}