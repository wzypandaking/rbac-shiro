#!/usr/bin/php
<?php
include_once './Template.php';
include_once './Config.ini.php';

// $argv 可以接收参数
$mysql = mysql_connect("{$mysql_host}:{$mysql_port}",$mysql_user, $mysql_password);
mysql_select_db($mysql_db_name);
mysql_query("set names {$mysql_charset}");

$result = mysql_query("select name, title from admin_auth_rule where creator=1");

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