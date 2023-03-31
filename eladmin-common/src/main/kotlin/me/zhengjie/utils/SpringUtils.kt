package me.zhengjie.utils

import jakarta.servlet.http.HttpServletRequest

fun HttpServletRequest.ip(): String {
    var ip = this.getHeader("x-forwarded-for")?.split(",")?.get(0)
    if (!ipOk(ip)) {
        ip = this.getHeader("Proxy-Client-IP")?.split(",")?.get(0)
        if (!ipOk(ip)) {
            ip = this.getHeader("WL-Proxy-Client-IP")?.split(",")?.get(0)
            if (!ipOk(ip)) {
                ip = this.getHeader("HTTP_CLIENT_IP")?.split(",")?.get(0)
                if (!ipOk(ip)) {
                    ip = this.getHeader("HTTP_X_FORWARDED_FOR")?.split(",")?.get(0)
                    if (!ipOk(ip)) {
                        ip = this.getHeader("X-Real-IP")?.split(",")?.get(0)
                        if (!ipOk(ip)) {
                            ip = this.remoteAddr
                        }
                    }
                }
            }
        }
    }
    return ip!!
}

fun HttpServletRequest.userAgent(): String {
    return this.getHeader("user-agent") ?: ""
}

private fun ipOk(ip: String?): Boolean {
    val b = ip.isNullOrEmpty() || "unknown".equals(ip, true)
    return !b
}
