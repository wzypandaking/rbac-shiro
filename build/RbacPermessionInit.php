#!/usr/bin/php
<?php
include_once './Template.php';

// $argv 可以接收参数
$mysql = mysql_connect("127.0.0.1:3306","root", "");
mysql_select_db("auth");
mysql_query("set names utf8");

$permissionName = $argv[1];
if(! $permissionName || ! preg_match("/^[a-z|A-Z]{1,}$/", $permissionName)) {
    exit("请输入权限名称");
}

$result = mysql_query("select name, title from admin_auth_rule where `name` like 'rbac%'");


$permissionJava = array();
$permissionXsd = array();
while($rule = mysql_fetch_assoc($result)) {
    $permission = str_replace("/", "_", strtoupper($rule['name']));
    $description = addslashes($rule['title']);
    $permissionJava[] = $permission . '("'. $rule['name'] .'", "'. $description .'"),';
    $permissionXsd[] = '<xsd:enumeration value="' . $permission . '"/>';
}

file_put_contents("../rbac-web/src/main/java/rbac/RbacPermissions.java", sprintf($rbacPermissionJavaTemplate, implode($permissionJava, "\r\n\t")));
file_put_contents("../rbac-web/src/main/resources/rbac-shior.xsd", sprintf($rbacPermissionXsdTemplate, implode($permissionXsd, "\r\n\t\t\t")));