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
    fun createDataSource(dataSourceProperties: DatasourceProperties) : DataSource {
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName(dataSourceProperties.driverClassName)
        dataSourceBuilder.url("${dataSourceProperties.providerUrl}//" +
                "${dataSourceProperties.host}:${dataSourceProperties.port}" +
                "/${dataSourceProperties.database}")
        dataSourceBuilder.username(dataSourceProperties.username)
        dataSourceBuilder.password(dataSourceProperties.password)

        return dataSourceBuilder.build()
    }

}