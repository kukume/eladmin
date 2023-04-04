package me.zhengjie.controller

import jakarta.servlet.http.HttpServletResponse
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.context.ApplicationContext
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.StandardCharsets
import java.util.Objects

@RestController
class ExportController(
    private val applicationContext: ApplicationContext
) {

    private val packageName = "me.kuku.test"

    @PostMapping("/export")
    fun export(@RequestBody param: ExportParam, response: HttpServletResponse) {
        val dynamicParam = param.dynamicParam
        val sp = dynamicParam.toPredicate()
        val sort = dynamicParam.toSort()
        val name = dynamicParam.name
        val prefix = name.substring(0, 1).uppercase() + name.substring(1)
        val repositoryClass = Class.forName("$packageName.pojo.${prefix}Repository")
        val repository = applicationContext.getBean(repositoryClass)
        val method =
            repositoryClass.getMethod("findAll", Specification::class.java, Sort::class.java)
        val list = method.invoke(repository, sp, sort) as List<*>
        val headers = param.headers
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("sheet1")
        val headerRow = sheet.createRow(0)
        for ((index, excelHead) in headers.withIndex()) {
            val cell = headerRow.createCell(index)
            cell.setCellValue(excelHead.title)
        }
        for ((i, any) in list.withIndex()) {
            val row = sheet.createRow(i + 1)
            for ((index, excelHead) in headers.withIndex()) {
                val arr = excelHead.value.split(".")
                var temp = any
                arr.forEach { temp = getField(temp, it) }
                val cell = row.createCell(index)
                for (r in excelHead.rules) {
                    if (r.value == temp) {
                        temp = r.text
                        break
                    }
                }
                cell.setCellValue(Objects.toString(temp, ""))
            }
        }
        response.contentType = "application/octet-stream"
        response.setHeader("Content-Disposition", "attachment; filename=${String("ss.xlsx".toByteArray(), StandardCharsets.ISO_8859_1)}")
        response.outputStream.use {
            workbook.write(it)
        }
    }

}

private fun getField(any: Any?, name: String): Any? {
    if (any == null) return null
    val clazz = any::class.java
    val getMethodName = "get${name.substring(0, 1).uppercase()}${name.substring(1)}"
    val method = clazz.getMethod(getMethodName)
    return method.invoke(any)
}

class ExportParam {
    var headers = mutableListOf<ExcelHead>()
    var dynamicParam: DynamicParam = DynamicParam()
}

class ExcelHead {
    var title: String = ""
    var value: String = ""
    var rules = mutableListOf<Rule>()
}

class Rule {
    var value: String = ""
    var text: String = ""
}
