# MyAuth

### 介绍
    基于原项目进行二开的，原项目地址已在以下贴出

    一个简单的授权管理系统，使用了springboot、mybatis-plus、redis、mysql等。
    此仓库为后端代码

![logo](https://images.gitee.com/uploads/images/2022/0218/215303_dbbda392_5370510.png)

* 我的QQ：[1935613](https://wpa.qq.com/msgrd?v=3&uin=1935613&site=qq&menu=yes)
* 官方Q群：[1016357430](https://jq.qq.com/?_wv=1027&k=eaectWIr)
### 原项目分支
- [@Daen](https://gitee.com/daenmax)
  项目地址 https://gitee.com/daenmax/myauth
### 前端
- [@TianYe](https://gitee.com/fieldtianye)
项目地址 https://gitee.com/fieldtianye/my-auth-web

### 预览

MyAuthWeb @TianYe版预览
https://www.cnblogs.com/daen/p/16019664.html


### 开发环境
    - Windows10 家庭版 21H1
    - Java JDK 17
    - Maven 3.6.3
    - IDEA 2021.1
    - Redis 3.0.504
    - MySQL 8.0.12

### 使用教程
#### 1.安装
    1.  克隆仓库到本地
    2.  修改配置：application-dev.yml和application-prod.yml
    3.  启动Redis、MySQL
    4.  导入doc/myauth.sql到数据库
    5.  运行MyAuthApplication.java启动类
    6.  访问测试地址，查看是否正常：http://localhost:8081/myauth/web/connect
    7.  运行后会检查admin表，如果是首次运行，即没有任何用户，那么会自动添加一个，账号admin，密码123456，该账号拥有最高权限，账号必须为admin不能修改
#### 2.打包
    1.  修改application.yml里的环境
![IDEA打包教程](https://images.gitee.com/uploads/images/2022/0311/191225_bdb8cfee_5370510.png)

### 部署教程
[MyAuth 后端 宝塔面板部署教程](https://www.kxzi.cn/lonely/120.html)
<br>
[MyAuth 前端 宝塔面板部署教程](https://www.kxzi.cn/lonely/145.html)

### 各语言DEMO
见doc/demo/目录

### API文档
项目地址：[https://www.apifox.cn/apidoc/shared-f5728933-86e8-4767-a255-926500e3e31a](https://www.apifox.cn/apidoc/shared-f5728933-86e8-4767-a255-926500e3e31a)

### 附带
#### 更新日志(新版本软件管理内自带更新日志页面)
    doc中有两个更新日志页面，效果如下
    其中一个是PHP版本，一个是JS版本（HTML纯静态）
    - PHP版本可以保证源服务器地址不被泄露
    - JS版本可以部署在各种托管上（只上传index.html即可），不依赖nginx等环境，但是会泄露源服务器地址
    各位权衡考虑使用哪个
	![示例](https://images.gitee.com/uploads/images/2022/0301/225502_1788301a_5370510.png)
###### 小贴士
> 更新内容每行前面加上【新增】【修复】【优化】【其他】【删除】等标签，更新日志页面会自动处理成tag

例如：
![示例](https://images.gitee.com/uploads/images/2022/0317/235803_7fe34f73_5370510.png)
效果：
![示例](https://images.gitee.com/uploads/images/2022/0317/235842_d3407750_5370510.png)

#### 感谢
    我的各位朋友们
