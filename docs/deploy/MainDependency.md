# 生成步骤
        cd ./build/dependency
        ./BuildMain.php 
# 生成规则

        它会将超级管理管理(ID:1)所创建的权限值生成到项目模块(rbac-web)中。
    具体路径：
        1、./rbac-web/src/main/java/rbac/RbacPermissions.java
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
        2、./rbac-web/src/main/resources/rbac-shiro.xsd
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
                
# 使用方法
        1、RbacPermissions.java需要配合 rbac.web.shiro.RequiresPermissions 注解一
    起使用。方法如下：
        
        @RestController
        @RequestMapping("/")
        public class AuthGroupController {
        
            @RequiresPermissions(value = {
                    RbacPermissions.RBAC,
                    ...
            })
            @RequestMapping("list")
            public String list(SearchParam param, PageParam pageParam) {
                return "list";
            }
        }
    
    2、rbac-shiro.xsd是在模板文件中使用，具体用法如下：
        a、在 <html> 中增加 xmlns:shiro="http://rbac.rbacshiro.com"。
        b、在模板中可使用入下标签：
            <shiro:hasPermission values="RBAC_AUTHORITY_GROUP_EDIT" logical="AND">
                <div>我有权限哦</div>
            </shiro:hasPermission>