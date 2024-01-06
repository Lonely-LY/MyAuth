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
public class CardExportVo extends Model {

    @ExcelProperty(value = "id")
    @ColumnWidth(value = 8)
    private Integer id;

    @ExcelProperty(value = "卡密")
    @ColumnWidth(value = 50)
    private String ckey;

    /**
     * 点数
     */
    @ExcelProperty(value = "点数")
    @ColumnWidth(value = 10)
    private Integer point;

    /**
     * 秒数
     */
    @ExcelProperty(value = "秒数")
    @ColumnWidth(value = 10)
    private Integer seconds;

    /**
     * 生成时间
     */
    private Integer addTime;

    /**
     * 使用时间
     */
    private Integer letTime;

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
     * 所属软件id
     */
    private Integer fromSoftId;

    /**
     * 所属软件名称
     */
    @ExcelProperty(value = "所属软件名称")
    @ColumnWidth(value = 18)
    private String fromSoftName;

    /**
     * 生成人ID
     */
    @ExcelProperty(value = "生成人ID")
    @ColumnWidth(value = 12)
    private Integer fromAdminId;
}
