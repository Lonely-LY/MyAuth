package cn.daenx.myauth.base.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

@Data
//导出时忽略没有@ExcelProperty的字段
@ExcelIgnoreUnannotated
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
public class AcardExportVo extends Model {

    @ExcelProperty(value = "id")
    @ColumnWidth(value = 8)
    private Integer id;

    @ExcelProperty(value = "卡密")
    @ColumnWidth(value = 50)
    private String ckey;

    /**
     * 余额
     */
    @ExcelProperty(value = "余额")
    @ColumnWidth(value = 10)
    private String money;

    /**
     * 生成时间
     */
    private Integer addTime;

    /**
     * 使用时间
     */
    private Integer letTime;

    /**
     * 使用人账号
     */
    @ExcelProperty(value = "使用人账号")
    @ColumnWidth(value = 20)
    private String letUser;

    /**
     * 卡密状态，0=未使用，1=已使用，2=被禁用
     */
    private Integer status;

    /**
     * 卡密状态，0=未使用，1=已使用，2=被禁用
     */
    @ExcelProperty(value = "卡密状态")
    @ColumnWidth(value = 12)
    private String statusName;

    /**
     * 生成时间
     */

    @ExcelProperty(value = "生成时间")
    @ColumnWidth(value = 20)
    private Date addTimeName;

    /**
     * 使用时间
     */
    @ExcelProperty(value = "使用时间")
    @ColumnWidth(value = 20)
    private Date letTimeName;
}
