#!/usr/bin/php
<?php
include_once './Template.php';
include_once './Config.ini.php';

fwrite(STDOUT, "输入用户名:");
$username = trim(fgets(STDIN));
fwrite(STDOUT, "输入密码:");
$password = trim(fgets(STDIN));
fwrite(STDOUT, "请输入序列号:");
$licenseKey = trim(fgets(STDIN));

if(! $licenseKey) {
    exit("请输入 License Key");
}

$externalPaths = [
    "../../spring-boot-rbac-shiro/"
];


// $argv 可以接收参数
$mysql = mysql_connect("{$mysql_host}:{$mysql_port}",$mysql_user, $mysql_password);
mysql_select_db($mysql_db_name);
mysql_query("set names {$mysql_charset}");

function checkUserAndGetUserId($username, $password) {
    $user = mysql_fetch_assoc(mysql_query("select id, uuid, username, password, salt from admin_users where username='{$username}'"));
    if(empty($user)) {
        exit("没有找到用户");
    }
    $passwordStr = md5(md5($password) . $user['salt']);
    if(strcmp($passwordStr, $user['password']) != 0) {
        exit("密码不正确");
    }
    return $user;
}

function checkGroup($userId) {
    $groupAccess = mysql_fetch_assoc(mysql_query("select id from admin_auth_group_access where uid='{$userId}' and group_id=2"));
    if(empty($groupAccess)) {
        exit("你不在“权限控制组”无法进行构建");
    }
}

function checkLicenseKeyAndGetLicense($licenseKey) {
    $result = mysql_query("select license,public_key,expire_time  from admin_version_license where license='$licenseKey'");
    $license = mysql_fetch_assoc($result);
    if(!$license) {
        exit("非法的License Key");
    }
    $second = time() - strtotime($license['expire_time']);
    if($second > 0) {
        exit("License Key 已过期");
    }
    return $license;
}

$user = checkUserAndGetUserId($username, $password);
checkGroup($user['id']);
$license = checkLicenseKeyAndGetLicense($licenseKey);


function buildLicenseKey($user, $license, $externalPaths) {
    $licenseContent = "{$user[uuid]}\r\n{$license[license]}\r\n{$license[public_key]}";
    foreach($externalPaths as $path) {
        file_put_contents($path . "src/main/resources/rbac.lic", $licenseContent);
    }
}

function buildPermissionRules($creator, $externalPaths) {
    global $permissionJavaTemplate, $permissionXsdTemplate;
    $result = mysql_query("select name, title from admin_auth_rule where creator='$creator'");
    $permissionJava = array();
    $permissionXsd = array();
    while($rule = mysql_fetch_assoc($result)) {
        $permission = str_replace("/", "_", strtoupper($rule['name']));
        $description = addslashes($rule['title']);
        $permissionJava[] = $permission . '("'. $rule['name'] .'", "'. $description .'"),';
        $permissionXsd[] = '<xsd:enumeration value="' . $permission . '"/>';
    }
    foreach($externalPaths as $path) {
        file_put_contents($path . "src/main/java/org/springframework/rbac/Permissions.java", sprintf($permissionJavaTemplate, implode($permissionJava, "\r\n\t")));
        file_put_contents($path . "src/main/resources/permission-rbac-shior.xsd", sprintf($permissionXsdTemplate, implode($permissionXsd, "\r\n\t\t\t")));
    }
}

buildLicenseKey($user, $license, $externalPaths);
buildPermissionRules($user['id'], $externalPaths);

foreach($externalPaths as $path) {
    chdir($path);
    system("mvn clean install -Dmaven.test.skip");

    //  清空
    file_put_contents("src/main/java/org/springframework/rbac/Permissions.java", sprintf($permissionJavaTemplate, ""));
    file_put_contents("src/main/resources/rbac.lic", " ");
    file_put_contents("src/main/resources/permission-rbac-shior.xsd", sprintf($permissionXsdTemplate, ""));
}
