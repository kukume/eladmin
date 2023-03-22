package me.zhengjie.modules.security.config

import cn.dev33.satoken.jwt.StpLogicJwtForSimple
import cn.dev33.satoken.stp.StpInterface
import me.zhengjie.modules.system.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SaTokenConfig {

    @Bean
    fun getStpLogicJwt() = StpLogicJwtForSimple()



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

