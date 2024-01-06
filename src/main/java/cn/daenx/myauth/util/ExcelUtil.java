package cn.daenx.myauth.util;

import cn.daenx.myauth.base.exception.MyException;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * EasyExcel导出工具类
 *
 * @author DaenMax
 */
@Slf4j
public class ExcelUtil {
    

    /**
     * 导出XLSX（只有一个sheet）
     *
     * @param response
     * @param fileName    导出的文件名，不需要加.xlsx
     * @param sheetName   工作表名
     * @param list<T>
     * @param entityClass
     */
    public static <T> void exportXlsx(HttpServletRequest request, HttpServletResponse response, String fileName, String sheetName, List<T> list, Class entityClass) {
        request.getSession();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        } catch (UnsupportedEncodingException e) {
            throw new MyException("导出XLS时发生错误");
        }
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            throw new MyException("导出XLS时发生错误");
        }
        ExcelWriterBuilder write = EasyExcel.write(outputStream, entityClass);
        ExcelWriterSheetBuilder sheet = write.sheet(sheetName);
        sheet.doWrite(list);
    }

    /**
     * 创建xlsx导出开始（适用于多个sheet）
     * 使用案例：
     * ExcelWriter writer = ExcelUtil.createExport(response, "网签合同列表");
     * ExcelUtil.writeSheet(writer, "合同列表", list, ContractPageDto.class);
     * ExcelUtil.writeSheet(writer, "流水列表", listBills, BusinessBillsPageDto.class);
     * ExcelUtil.finishWrite(writer);
     *
     * @param response
     * @param fileName 导出的文件名，不需要加.xlsx
     */
    public static <T> ExcelWriter createExport(HttpServletRequest request, HttpServletResponse response, String fileName) {
        request.getSession();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        } catch (UnsupportedEncodingException e) {
            throw new MyException("导出XLS时发送错误");
        }
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            throw new MyException("导出XLS发送错误");
        }
        ExcelWriter excelWriter = EasyExcel.write(outputStream).build();
        return excelWriter;
    }

    /**
     * 写入一个sheet
     *
     * @param sheetName   工作表名
     * @param list<T>
     * @param entityClass
     */
    public static <T> void writeSheet(ExcelWriter excelWriter, String sheetName, List<T> list, Class entityClass) {
        WriteSheet sheet = EasyExcel.writerSheet(sheetName).head(entityClass).build();
        excelWriter.write(list, sheet);
    }

    /**
     * 结束写入sheet并导出
     */
    public static <T> void finishWrite(ExcelWriter excelWriter) {
        excelWriter.finish();
    }

}
