package cn.myauthx.api.web.entity;

import cn.myauthx.api.base.po.baseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ma_soft")
public class Soft extends baseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String key;

    private String name;

    /**
     * -1=封禁，0=停用，1=正常，2=维护
     */
    private Integer status;

    /**
     * 0=收费，1=免费
     */
    private Integer type;

    private Integer addTime;

    /**
     * 数据加密秘钥
     */
    private String genKey;

    /**
     * 0=数据不加密，1=数据加密
     */
    private Integer genStatus;

    /**
     * 0=不允许多开，1=允许多开
     */
    private Integer batchSoft;

    /**
     * 0=不允许多地同时登录，1=允许多地同时登录
     */
    private Integer multipleLogin;

    /**
     * 心跳有效时间
     */
    private Integer heartTime;


}