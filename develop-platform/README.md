**sceo-dev-platform**
---------------------

**公共配置及监控**

**Eureka Server**

http://192.168.13.53:1111/eureka/

**Config Server**

http://192.168.13.53:9999

**Config Repository**

http://config-user@192.168.13.53:8999/config-eureka-Server/config-file.git

**监控**

Eureka Server：[http://192.168.13.53:1111](http://192.168.13.53:1111)

turbin stream: [http://192.168.13.51:3001/turbine.stream](http://192.168.13.51:3001/turbine.stream)

Hystrix Dashboard: [http://192.168.13.51:3000/hystrix](http://192.168.13.51:3000/hystrix)

zipkin: [http://192.168.13.51:9411/](http://192.168.13.51:9411/)

**API文档**

_example_：_http://localhost:2001/swagger-ui.html_


**客户端代码结构**

* `xxx-client`   

最终暴露服务的地方，包括FeignClient、熔断处理、Swagger、Hystrix stream、zipkin
* `xxx-common`   

存放 client 和 provider 模块公用代码。例如 Model、Result(统一返回信息封装类) 等
* `xxx-provider`   

实现服务代码的地方，包括DAO、Service、Mapper.xml等处理业务逻辑的代码, 为了跟踪服务的调用，provider也应集成zipkin

**相关教程**

* [后端数据校验 Hibernate Validator](http://192.168.13.53:8002/pages/viewpage.action?pageId=1572958)
* [Restful 接口设计](http://192.168.13.53:8002/pages/viewpage.action?pageId=1572945)
* [分页插件 Mybatis-PageHelper 使用方法](https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md)
* [spring-data-redis 集成](http://192.168.13.53:8002/pages/viewpage.action?pageId=1572974)

**重要提示**

`PageHelper.startPage`方法重要提示

只有紧跟在`PageHelper.startPage`方法后的第一个Mybatis的查询（Select）方法会被分页。

**更新记录**
* 17.4.13：添加Entity数据校验以及国际化配置和示例代码
