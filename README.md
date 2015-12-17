# README #

根据module name自动生成插件入口类以及plugin.xml,config.xml
根据config.xml自动生成插件方法

### 使用步骤 ###

1.复制AppcanPluginDemo3.0工程，重命名module为插件名称，以uex开头;  
2.右键module -> AppCanMenu -> initModule (初始化工程)
会初始化入口类，plugin.xml,config.xml  
3.在config.xml中配置method name type params(多个参数以“|”分隔)  
4.右键 -> Generate -> generate生成插件方法  

### TODO ###

* 生成VO
