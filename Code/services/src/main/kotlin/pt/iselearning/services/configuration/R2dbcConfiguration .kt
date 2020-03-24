package pt.iselearning.services.configuration

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

/**
 * Class with configuration for the R2dbc repositories
 */
@Configuration
@EnableR2dbcRepositories
class R2dbcConfiguration  : AbstractR2dbcConfiguration() {

    /**
     * Bean to implement connection factory
     */
    @Bean
    override fun connectionFactory(): ConnectionFactory  {

        val config = PostgresqlConnectionConfiguration.builder()
                .host(DatabaseConfiguration.props.host)
                .port(DatabaseConfiguration.props.port)
                .database(DatabaseConfiguration.props.database)
                .username(DatabaseConfiguration.props.username)
                .password(DatabaseConfiguration.props.password)
                .build()

        return PostgresqlConnectionFactory(config)

    }

}