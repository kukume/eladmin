package me.zhengjie.modules.security.config

import cn.dev33.satoken.dao.SaTokenDaoRedisJackson
import cn.dev33.satoken.jwt.StpLogicJwtForSimple
import cn.dev33.satoken.stp.StpInterface
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module
import me.zhengjie.modules.system.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SaTokenConfig(
    saTokenDaoRedisJackson: SaTokenDaoRedisJackson,
//    hibernate5JakartaModule: Hibernate5JakartaModule
    hibernate6Module: Hibernate6Module
) {

    @Bean
    fun getStpLogicJwt(): StpLogicJwtForSimple {
        return StpLogicJwtForSimple()
    }

    init {
        saTokenDaoRedisJackson.objectMapper.registerModule(hibernate6Module)
    }


}


@Component
class StpInterfaceImpl(
    private val userService: UserService
): StpInterface {

    @Transactional
    override fun getPermissionList(loginId: Any, loginType: String): MutableList<String> {
        val userEntity = userService.findById(loginId.toString().toLong())
        val list = mutableListOf<String>()
        val roles = userEntity.roles
        for (role in roles) {
            for (menu in role.menus) {
                list.add(menu.permission)
            }
        }
        return list
    }

    override fun getRoleList(loginId: Any, loginType: String): MutableList<String> {
        val userEntity = userService.findById(loginId.toString().toLong())
        val list = mutableListOf<String>()
        return userEntity.roles.map { it.name }.toMutableList()
    }
}

