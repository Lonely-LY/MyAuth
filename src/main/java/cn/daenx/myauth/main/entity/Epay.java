package cn.daenx.myauth.main.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("ma_pay_config")
public class Epay extends Model {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 通道名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 通道标识
     */
    private String driver;

    /**
     * 通道配置
     */
    private String config;

    /**
     * 通道说明
     */
    private String content;

    /**
     * 修改时间戳
     */
    private Integer updateTime;

    /**
     * 通道开关
     */
    private Integer enabled;
}
