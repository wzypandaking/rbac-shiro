# 说明

`第三方依赖包`在构建时需要满足一下2个特点:
>   * 一个账号
>   * 该账号属于[`权限管理组`](../AuthGroup.md)
>   * 构建时需要创建相应的`version`和`license`


# 生成步骤
    1、修改第三方依赖版本
        <dependency>
            <groupId>rbac</groupId>
            <artifactId>spring-boot-rbac-shiro</artifactId>
            <version>1.0.0</version>
        </dependency>
        
    2、cd ./build/dependency
       ./BuildThird.php 
       
        它会将创建的权限值生成到项目模块(spring-boot-rbac-shiro)中。并开始构建三
    方依赖包。
    
# 生成规则
    1、./spring-boot-rbac-shiro/src/main/java/org/springframework/rbac/Permissions.java
        package rbac;
                    
        /**
         * Created by pandaking on 2017/5/31.
         */
        public enum RbacPermissions {
            RBAC("rbac", "权限系统"),
            ...
        
            ;
            private String name;
            private String title;
        
            RbacPermissions(String name, String title) {
                this.name = name;
                this.title = title;
            }
        
            public String getName() {
                return name;
            }
        
            public String getTitle() {
                return title;
            }
        }
    2、./spring-boot-rbac-shiro/src/main/resources/permission-rbac-shiro.xsd
            <?xml version="1.0" encoding="UTF-8" standalone="no"?>
            <xsd:schema xmlns="http://rbac.rbacshiro.com"
                        xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                        targetNamespace="http://rbac.rbacshiro.com">
                <xsd:simpleType name="Permissions">
                    <xsd:restriction xml:base="xsd:token">
                        <xsd:enumeration value="RBAC"/>
                    ...
                    </xsd:restriction>
                </xsd:simpleType>
                <xsd:element name="hasPermission">
                    <xsd:complexType>
                        <xsd:attribute name="values" type="Permissions" use="required"/>
                        <xsd:attribute name="logical" use="required">
                            <xsd:simpleType>
                                <xsd:restriction>
                                    <xsd:enumeration value="AND" />
                                    <xsd:enumeration value="OR" />
                                </xsd:restriction>
                            </xsd:simpleType>
                        </xsd:attribute>
                    </xsd:complexType>
                </xsd:element>
            </xsd:schema>
     3、./spring-boot-rbac-shiro/src/main/resources/rbac.lic  数字签名
     
# 使用方法

>   ## 1、引入依赖包

    <dependency>
        <groupId>rbac</groupId>
        <artifactId>spring-boot-rbac-shiro</artifactId>
        <version>1.0.0</version>
    </dependency>

>   ## 2、继承`org.springframework.rbac.web.ShiroConfigurer`

    /**
     * Created by pandaking on 2017/5/25.
     */
    @Configuration
    public class RbacShiroConfigurer extends ShiroConfigurer {
    
        @Override
        public LinkedHashMap<String, String> filterChainDefinitionMap() {
            //配置访问权限
            LinkedHashMap<String, String> filterChainDefinitionMap=new LinkedHashMap<>();
            //  静态资源
            {
                filterChainDefinitionMap.put("/css/**", "anon"); //表示可以匿名访问
                filterChainDefinitionMap.put("/fonts/**", "anon"); //表示可以匿名访问
                filterChainDefinitionMap.put("/images/**", "anon"); //表示可以匿名访问
                filterChainDefinitionMap.put("/js/**", "anon"); //表示可以匿名访问
                filterChainDefinitionMap.put("/layui/**", "anon"); //表示可以匿名访问
                filterChainDefinitionMap.put("/**.js", "anon"); //表示可以匿名访问
            }
            filterChainDefinitionMap.put("/*", "authc");//表示需要认证才可以访问
            filterChainDefinitionMap.put("/**", "authc");//表示需要认证才可以访问
            filterChainDefinitionMap.put("/*.*", "authc");
            return filterChainDefinitionMap;
        }
    
        @Override
        public String getLoginUrl() {
            return "/signin.html";
        }
    
        @Override
        public String getSuccessUrl() {
            return "/index.html";
        }
    }
        
>   ## 3、加入权限控制

`org.springframework.rbac.Permissions.java` 配合 `org.springframework.rbac.web.annotations.RequiresPermissions`
一起使用。
方法如下：

    @RestController
    @RequestMapping("/")
    public class AuthGroupController {
    
        @RequiresPermissions(value = {
                org.springframework.rbac.Permissions.RBAC,
                ...
        })
        @RequestMapping("list")
        public String list(SearchParam param, PageParam pageParam) {
            return "list";
        }
    }

>   ## 4、模板中加入权限
* a、在 `<html>` 中增加
>>
    xmlns:shiro="http://permissions.rbacshiro.com"`。
* b、在模板中可使用入下标签：
>>
    <shiro:hasPermission values="RBAC_AUTHORITY_GROUP_EDIT" logical="AND">
       <div>我有权限哦</div>
    </shiro:hasPermission>