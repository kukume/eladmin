package me.zhengjie.hibernate

import com.fasterxml.jackson.databind.ObjectMapper
import me.zhengjie.utils.SpringContextHolder
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.usertype.UserType
import java.io.Serializable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types
import java.util.Objects

class JsonType: UserType<Any> {

    override fun getSqlType(): Int {
        return Types.VARCHAR
    }

    override fun returnedClass(): Class<Any> {
        return Any::class.java
    }

    override fun isMutable(): Boolean {
        return false
    }

    override fun deepCopy(value: Any?): Any? {
        return value
    }

    override fun equals(x: Any?, y: Any?): Boolean {
        return Objects.equals(x, y)
    }

    override fun hashCode(x: Any?): Int {
        return Objects.hashCode(x)
    }

    override fun nullSafeGet(
        rs: ResultSet,
        position: Int,
        session: SharedSessionContractImplementor?,
        owner: Any
    ): Any? {
        if (rs.wasNull()) return null
        val value = rs.getString(position)
        val objectMapper = SpringContextHolder.getBean(ObjectMapper::class.java)
        return objectMapper.readValue(value, owner::class.java)
    }

    override fun nullSafeSet(
        st: PreparedStatement,
        value: Any?,
        index: Int,
        session: SharedSessionContractImplementor?
    ) {
        if (value == null) {
            st.setNull(index, Types.VARCHAR)
        } else {
            val objectMapper = SpringContextHolder.getBean(ObjectMapper::class.java)
            val str = objectMapper.writeValueAsString(value)
            st.setString(index, str)
        }
    }

    override fun disassemble(value: Any?): Serializable? {
        return null
    }

    override fun assemble(cached: Serializable?, owner: Any?): Any? {
        return null
    }

    override fun replace(detached: Any?, managed: Any?, owner: Any?): Any? {
        return null
    }
}
