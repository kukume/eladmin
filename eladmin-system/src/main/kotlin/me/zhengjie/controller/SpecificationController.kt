@file:Suppress("UNCHECKED_CAST")

package me.zhengjie.controller

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import me.kuku.utils.DateTimeFormatterUtils
import org.springframework.context.ApplicationContext
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
class GeneralController(
    private val applicationContext: ApplicationContext
) {

    private val packageName = "me.kuku.test"

    @PostMapping("/bySpecification")
    fun queryBySpecification(@RequestBody dynamicParam: DynamicParam): Any {
        val sp = dynamicParam.toPredicate()
        val dynamicPageable = dynamicParam.pageable
        val page = dynamicPageable.page
        val size = dynamicPageable.size
        val pageable = PageRequest.of(page, size, dynamicParam.toSort())
        val name = dynamicParam.name
        val prefix = name.substring(0, 1).uppercase() + name.substring(1)
        val repositoryClass = Class.forName("$packageName.pojo.${prefix}Repository")
        val repository = applicationContext.getBean(repositoryClass)
        val method =
            repositoryClass.getMethod("findAll", Specification::class.java, Pageable::class.java)
        return method.invoke(repository, sp, pageable)
    }

}


class DynamicParam {
    // entity的名字
    var name: String = ""
    var query: List<DynamicQuery> = mutableListOf()
    var pageable: DynamicPageable = DynamicPageable()

    fun toPredicate(): Specification<Any> {
        return Specification<Any> { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            for (dynamicQuery in query) {
                val field = dynamicQuery.field
                val fieldList = field.split(".")
                val firstField = fieldList[0]
                var path = root.get<Any>(firstField)
                fieldList.stream().skip(1).forEach {
                    path = path.get(it)
                }
                val type = dynamicQuery.type
                val value = dynamicQuery.value
                val predicate = when (type) {
                    "eq" -> criteriaBuilder.equal(path, value)
                    "like" -> criteriaBuilder.like(path as Expression<String>, "%$value%")
                    "between" -> {
                        val valueList = dynamicQuery.valueList
                        if (valueList.size != 2) error("参数错误，类型为between，valueList数组必须是2个")
                        val first = valueList[0]
                        val second = valueList[1]
                        convert(criteriaBuilder, path, first, second)
                    }
                    "startWith" -> criteriaBuilder.like(path as Expression<String>, "%$value")
                    "endWith" -> criteriaBuilder.like(path as Expression<String>, "$value%")
                    "gt" -> criteriaBuilder.gt(path as Expression<out Number>, value.toInt())
                    "ge" -> criteriaBuilder.ge(path as Expression<out Number>, value.toInt())
                    "lt" -> criteriaBuilder.lt(path as Expression<out Number>, value.toInt())
                    "le" -> criteriaBuilder.le(path as Expression<out Number>, value.toInt())
                    "ne" -> criteriaBuilder.notEqual(path, value)
                    "greaterThan" -> criteriaBuilder.greaterThan(path as Expression<String>, value)
                    "lessThan" -> criteriaBuilder.lessThan(path as Expression<String>, value)
                    "greaterThanOrEqualTo" -> criteriaBuilder.greaterThanOrEqualTo(path as Expression<String>, value)
                    "lessThanOrEqualTo" -> criteriaBuilder.lessThanOrEqualTo(path as Expression<String>, value)
                    else -> continue
                }
                predicate?.let {
                    predicates.add(it)
                }
            }
            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }

    fun toSort(): Sort {
        val sortList = pageable.sort
        val sortOrderList = mutableListOf<Sort.Order>()
        for (dynamicSort in sortList) {
            val order = dynamicSort.order
            val field = dynamicSort.field
            if (order.uppercase() == "asc") sortOrderList.add(Sort.Order.asc(field))
            else if (order.uppercase() == "desc") sortOrderList.add(Sort.Order.desc(field))
        }
        return Sort.by(sortOrderList)
    }

}

class DynamicQuery {
    var field: String = ""
    var value: String = ""
    var valueList: List<String> = mutableListOf()
    // eq、like、between、startWith、endWith、gt、ge、lt、le、ne、greaterThan、lessThan、greaterThanOrEqualTo
    var type: String = "eq"
}


class DynamicSort {
    var field: String = ""
    var order: String = "asc"
}

class DynamicPageable {
    var page: Int = 0
    var size: Int = 20
    var sort: List<DynamicSort> = mutableListOf()
}


private fun convert(build: CriteriaBuilder, path: Path<*>, text1: String, text2: String): Predicate? {
    return when (path.javaType) {
        LocalDate::class.java -> {
            build.between(path as Expression<LocalDate>,
                DateTimeFormatterUtils.parseToLocalDate(text1, "yyyy-MM-dd"),
                DateTimeFormatterUtils.parseToLocalDate(text2, "yyyy-MM-dd")
            )
        }
        LocalDateTime::class.java -> {
            build.between(path as Expression<LocalDateTime>,
                DateTimeFormatterUtils.parseToLocalDateTime(text1, "yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatterUtils.parseToLocalDateTime(text2, "yyyy-MM-dd HH:mm:ss")
            )
        }
        String::class.java -> {
            build.between(path as Expression<String>, text1, text2)
        }
        else -> null
    }
}
