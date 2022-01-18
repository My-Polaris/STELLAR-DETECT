# 星际探测-游戏官网

这是一个基于Spring Boot+Thymeleaf的游戏官网，“星际探测”即为假想游戏名。

由于游戏的设定并未完全确定,所以只实作用户相关与公告相关的一些功能。

在技术栈上，前端的页面由前端三剑客搭建，一些交互动作与动画效果则由jQuery补充完成，AJAX负责异步发送请求至后端，同时接收相应的数据显示到页面上；后端框架使用的是Spring Boot框架。

前端由Thymeleaf渲染H5页面，其中CSS与JS都与templates的html分开存放，后端控制以Controller->Service->JpaReporitory的框架搭建。

### 首页：

<img src="src\main\resources\static\images\result-index1.png" style="zoom:40%;" />

**开局加载**：进入网站时会加载进度条，页面加载完成后才正式进入。

**整屏滑动**：页面以整屏滑动，会有相应的提示滑动信息，也可以点击导航栏实现滑动。

> 此处用到了fullPage全屏滚动插件，fullPage.js 是一个基于 jQuery 的插件，它能够很方便、很轻松的制作出全屏网站。

### 公告页：

<img src="src\main\resources\static\images\result-index2.png" style="zoom:40%;" />

### 登录/注册页：

<img src="src\main\resources\static\images\result-login.png" style="zoom:40%;" />

### 数据库设计：

<img src="src\main\resources\static\images\db_design.png" alt="db_design" style="zoom:40%;" />

### 功能设计：

<img src="src\main\resources\static\images\function_design.png" alt="function_design" style="zoom:40%;" />

总结来说是一个Spring Boot的小项目，该项目的功能并不多，主要还是以介绍游戏，展示游戏特性、游戏公告为主。