package fr.mustafin.demo.config

import SqlProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

//Annotation @ConstructorBinding is needed, it cannot be placed over the c @Bean method, one way as workaround
@ConfigurationProperties("sql")
class SqlPropertiesEx @ConstructorBinding constructor(
    url: String,
    user: String,
    password: String,
    schema: String,
    dropDatabase: Boolean
) : SqlProperties(url, user, password, schema, dropDatabase)