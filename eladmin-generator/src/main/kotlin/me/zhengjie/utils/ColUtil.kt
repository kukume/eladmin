package me.zhengjie.utils

object ColUtil {
    @JvmStatic
    fun cloToJava(type: String): String {
        return when (type) {
            "tinyint" -> "Integer"
            "smallint" -> "Integer"
            "mediumint" -> "Integer"
            "int" -> "Integer"
            "integer" -> "Integer"
            "bigint" -> "Long"
            "float" -> "Float"
            "double" -> "Double"
            "decimal" -> "BigDecimal"
            "bit" -> "Boolean"
            "char" -> "String"
            "varchar" -> "String"
            "tinytext" -> "String"
            "text" -> "String"
            "mediumtext" -> "String"
            "longtext" -> "String"
            "date" -> "LocalDate"
            "datetime" -> "LocalDateTime"
            "timestamp" -> "Timestamp"
            else -> "unknownType"
        }
    }

}
