package me.zhengjie.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpInputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
import org.springframework.stereotype.Service
import org.springframework.util.StreamUtils
import java.lang.reflect.Type
import java.net.URLDecoder
import java.nio.charset.Charset

@Service
class MappingJackson2HttpMessageFormConverter(
    objectMapper: ObjectMapper
): AbstractJackson2HttpMessageConverter(objectMapper, MediaType.APPLICATION_FORM_URLENCODED) {

    override fun read(type: Type, contextClass: Class<*>?, inputMessage: HttpInputMessage): Any {
        val inputStream = StreamUtils.nonClosing(inputMessage.body);
        val body = String(inputStream.readAllBytes())
        val objectNode = objectMapper.createObjectNode()
        val split = body.split("&")
        for (single in split) {
            val keyValue = single.split("=")
            if (keyValue.size == 2) {
                val prefix = URLDecoder.decode(keyValue[0], Charset.defaultCharset())
                val suffix = URLDecoder.decode(keyValue[1], Charset.defaultCharset())
                objectNode.put(prefix, suffix)
            }
        }
        val javaType = getJavaType(type, contextClass);
        return objectMapper.convertValue(objectNode, javaType)
    }

    override fun canWrite(clazz: Class<*>, mediaType: MediaType?): Boolean {
        return super.canWrite(clazz, mediaType ?: return false)
    }
}
