## ELADMIN

[eladmin](https://github.com/elunez/eladmin) 的修改版本

目标修改内容：
* 项目管理工具由`maven`更新为`gradle.kts`，并支持`java`和`kotlin`混写，可在`kotlin`中调用使用`lombok`的`java`对象
* 依赖均更新为最新版本，`jdk`升级至`17`
* 权限框架更换为`sa-token`，在`dev`环境中不验证权限
* `json`序列化更换为`jackson`，删除`fastjson`
* 删除`mapstruct`，重写`hibernate`的`ByteBuddyInterceptor`和使用`jackson`的`hibernate`模块来解决`hibernate`报懒加载异常
* 提供通用的查询方法和删除和导出`excel`方法，一个接口可查询连接数据库中所有的表，支持连表、多数据源等，支持任意数据库字段的任意条件的动态条件查询，并可以配置好权限来限制查询和导出，代码生成仅生成实体类和对应的`Repository`和前端即可
