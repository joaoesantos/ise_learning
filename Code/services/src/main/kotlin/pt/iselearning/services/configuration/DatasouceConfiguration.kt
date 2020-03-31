package pt.iselearning.services.configuration

import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

/**
 * Class with datasource's configuration
 */
@Configuration
class DatasourceConfiguration {

    /**
     * Bean for implementing provider datasource
     */
    @Bean
    fun createDataSource() : DataSource {
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName(DatasourceProperties.props.driverClassName)
        dataSourceBuilder.url("${DatasourceProperties.props.providerUrl}//" +
                "${DatasourceProperties.props.host}:${DatasourceProperties.props.port}" +
                "/${DatasourceProperties.props.database}")
        dataSourceBuilder.username(DatasourceProperties.props.username)
        dataSourceBuilder.password(DatasourceProperties.props.password)

        return dataSourceBuilder.build()
    }

}