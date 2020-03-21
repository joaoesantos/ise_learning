package pt.iselearning.services.configuration

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import pt.iselearning.services.repository.UserRepository


@Configuration
@EnableR2dbcRepositories
class R2dbcConfiguration  : AbstractR2dbcConfiguration() {
    @Bean
    override fun connectionFactory(): PostgresqlConnectionFactory  {
        val config = PostgresqlConnectionConfiguration.builder()
                .host("localhost")
                .port(5432)
                .database("postgres")
                .username("postgres")
                .password("pass123")
                .build()

        return PostgresqlConnectionFactory(config)

    }

}