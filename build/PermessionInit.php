#!/usr/bin/php
<?php
ini_set('date.timezone','Asia/Shanghai');
include_once './Template.php';

// $argv 可以接收参数
$mysql = mysql_connect("127.0.0.1:3306","root", "");
mysql_select_db("auth");
mysql_query("set names utf8");

$permissionName = $argv[1];
if(! $permissionName || ! preg_match("/^[a-z|A-Z]{1,}$/", $permissionName)) {
    exit("请输入权限名称");
}

$licenseKey = $argv[2];
if(! $licenseKey) {
    exit("请输入 License Key");
}

function buildLicenseKey() {
    global $licenseKey;
    $result = mysql_query("select license,public_key,expire_time  from admin_version_license where uuid='$licenseKey'");
    $license = mysql_fetch_assoc($result);
    if(!$license) {
        exit("非法的License Key");
    }
    if(strtotime($license['expire_time']) < time()) {
        exit("License Key 已过期");
    }
    $licenseContent = "{$license[license]}\r\n$license[public_key]";
    file_put_contents("../spring-boot-rbac-shiro/src/main/resources/rbac.lic", $licenseContent);
}

// todo 这个地方有问题，不能使用 rbac
function buildPermissionRules() {
    global $permissionJavaTemplate, $permissionXsdTemplate;
    $result = mysql_query("select name, title from admin_auth_rule where `name` like 'rbac%'");
    $permissionJava = array();
    $permissionXsd = array();
    while($rule = mysql_fetch_assoc($result)) {
        $permission = str_replace("/", "_", strtoupper($rule['name']));
        $description = addslashes($rule['title']);
        $permissionJava[] = $permission . '("'. $rule['name'] .'", "'. $description .'"),';
        $permissionXsd[] = '<xsd:enumeration value="' . $permission . '"/>';
    }

    file_put_contents("../spring-boot-rbac-shiro/src/main/java/org/springframework/rbac/Permissions.java", sprintf($permissionJavaTemplate, implode($permissionJava, "\r\n\t")));
    file_put_contents("../spring-boot-rbac-shiro/src/main/resources/permission-rbac-shior.xsd", sprintf($permissionXsdTemplate, implode($permissionXsd, "\r\n\t\t\t")));
}

buildLicenseKey();
buildPermissionRules();

chdir("../spring-boot-rbac-shiro");
system("mvn clean install -Dmaven.test.skip");