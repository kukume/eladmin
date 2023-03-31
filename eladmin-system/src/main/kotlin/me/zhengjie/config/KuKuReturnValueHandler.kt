package me.zhengjie.config

import me.zhengjie.utils.SpringContextHolder
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewMethodReturnValueHandler
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor

class KuKuReturnValueHandler: HandlerMethodReturnValueHandler {

    private val requestMappingHandlerAdapter by lazy {
        SpringContextHolder.getBean(RequestMappingHandlerAdapter::class.java)
    }

    private val returnValueHandlers by lazy {
        val composite =
            requestMappingHandlerAdapter::class.java.getDeclaredField("returnValueHandlers")
                .also { it.isAccessible = true }
                .get(requestMappingHandlerAdapter) as HandlerMethodReturnValueHandlerComposite
        composite.handlers
    }

    private inline fun <reified T: Any> find(): T {
        return returnValueHandlers.find { it::class == T::class } as T
    }

    override fun supportsReturnType(returnType: MethodParameter): Boolean {
        val paramType = returnType.parameterType
        return paramType == KuKuResult::class.java
    }

    override fun handleReturnValue(
        returnValue: Any?,
        returnType: MethodParameter,
        mavContainer: ModelAndViewContainer,
        webRequest: NativeWebRequest
    ) {
        val accept = webRequest.getHeader("accept")?.toString() ?: return find<RequestResponseBodyMethodProcessor>().handleReturnValue(returnValue, returnType, mavContainer, webRequest)
        val kuKuResult = returnValue as KuKuResult
        if (accept.startsWith("text/html")) {
            find<ModelAndViewMethodReturnValueHandler>().handleReturnValue(ModelAndView(kuKuResult.template ?: "", kuKuResult.any as Map<String, Any>), returnType, mavContainer, webRequest)
        } else {
            find<RequestResponseBodyMethodProcessor>().handleReturnValue(kuKuResult.any, returnType, mavContainer, webRequest)
        }
    }
}

data class KuKuResult(val any: Any, val template: String? = null)
