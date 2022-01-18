package cn.myauthx.api.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Data
@Accessors(chain = true)
@TableName("ma_msg")
public class Msg extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String keyword;

    private String msg;

    /**
     * 0=禁用，1=正常
     */
    private Integer status;

    /**
     * 所属软件id
     */
    private Integer fromSoftId;

    /**
     * 所属软件版本，留空则全版本可用
     */
    private Integer fromVerId;


}