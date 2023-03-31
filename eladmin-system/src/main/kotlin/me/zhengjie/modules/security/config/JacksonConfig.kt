package me.zhengjie.modules.security.config

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class JacksonConfig {

//    @Bean
//    fun hibernate5JakartaModule(): Hibernate5JakartaModule {
//        return Hibernate5JakartaModule().apply {
//            enable(Hibernate5JakartaModule.Feature.REPLACE_PERSISTENT_COLLECTIONS)
//        }
//    }

    @Bean
    fun ss(): Hibernate6Module {
        return Hibernate6Module().apply {
            enable(Hibernate6Module.Feature.REPLACE_PERSISTENT_COLLECTIONS)
        }
    }

}
