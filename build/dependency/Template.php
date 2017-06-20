<?php
ini_set('date.timezone','Asia/Shanghai');
error_reporting(0);

$permissionJavaTemplate = 'package org.springframework.rbac;

/**
 * Created by pandaking on 2017/5/31.
 */
public enum Permissions {
    %s

    ;
    private String name;
    private String title;

    Permissions(String name, String title) {
        this.name = name;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }
}';

$rbacPermissionJavaTemplate = 'package rbac;

/**
 * Created by pandaking on 2017/5/31.
 */
public enum RbacPermissions {
    %s

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
}';



$permissionXsdTemplate = '<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsd:schema xmlns="http://permissions.rbacshiro.com"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            targetNamespace="http://permissions.rbacshiro.com">
    <xsd:simpleType name="Permissions">
        <xsd:restriction xml:base="xsd:token">
            %s
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
</xsd:schema>';
$rbacPermissionXsdTemplate = '<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsd:schema xmlns="http://rbac.rbacshiro.com"
            xmlns:xsd="http://www.w3.org/2001/XMLSchema"
            targetNamespace="http://rbac.rbacshiro.com">
    <xsd:simpleType name="Permissions">
        <xsd:restriction xml:base="xsd:token">
            %s
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
</xsd:schema>';
