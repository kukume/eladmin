package me.zhengjie.utils

import cn.dev33.satoken.stp.StpUtil
import com.fasterxml.jackson.databind.JsonNode
import me.kuku.utils.Jackson
import me.kuku.utils.toJsonNode

object SecurityUtils {

    private fun getCurrentUser(): JsonNode {
        return Jackson.toJsonString(StpUtil.getSession().get("user")).toJsonNode()
    }

    @JvmStatic
    fun getCurrentUsername(): String {
        return getCurrentUser()["username"].asText()
    }

}
