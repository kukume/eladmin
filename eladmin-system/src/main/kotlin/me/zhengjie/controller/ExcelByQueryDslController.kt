package me.zhengjie.controller

import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.servlet.http.HttpServletResponse
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.StandardCharsets
import java.util.*

@RestController
class ExcelByQueryDslController(
    private val jpaQueryFactory: JPAQueryFactory
) {

    @PostMapping("/export/byQuerydsl")
    fun export(@RequestBody sql: ExportSql, response: HttpServletResponse) {
        val from = sql.from
        val primary = from.value
        val qPrimary = qEntity(primary)
        val jpaQuery = jpaQueryFactory.from(qPrimary)
        sql.from(jpaQuery)
        sql.where(jpaQuery)
        sql.orderBy(jpaQuery)
        val select = sql.select
        val selectQArray = select.map { convert(it.value) }.toTypedArray()
        if (selectQArray.isNotEmpty()) jpaQuery.select(*selectQArray)
        val fetch = jpaQuery.fetch()
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("sheet1")
        val headerRow = sheet.createRow(0)
        for ((index, excelHead) in select.withIndex()) {
            val cell = headerRow.createCell(index)
            cell.setCellValue(excelHead.title)
        }
        for ((rowNum, f) in fetch.withIndex()) {
            val row = sheet.createRow(rowNum + 1)
            if (f is Tuple) {
                for ((i, path) in selectQArray.withIndex()) {
                    f.get(path)?.let {
                        val cell = row.createCell(i)
                        cell.setCellValue(Objects.toString(it, ""))
                    }
                }
            }
        }
        response.contentType = "application/octet-stream"
        response.setHeader("Content-Disposition", "attachment; filename=${String("ss.xlsx".toByteArray(), StandardCharsets.ISO_8859_1)}")
        response.outputStream.use {
            workbook.write(it)
        }

    }

}


class ExportSql: BaseSql() {
    var select: MutableList<ExcelHead> = mutableListOf()
}
