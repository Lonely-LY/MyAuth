package cn.daenx.myauth.base.vo;

import lombok.Data;

@Data
public class PayConfig {
    //{"fieldName":"url","fieldContent":"api地址","fieldText":"https://www.baidu.com"}
    private String fieldName;
    private String fieldContent;
    private String fieldText;
}