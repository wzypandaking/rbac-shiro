/**
 * Created by pandaking on 2017/5/24.
 */
+(function(){

    var profileTemplate = loadArtTemplate("profile");
    commonAjax({
        url: '/rbac/admin/user',
        dataType: 'json',
        success: function (result) {
            var render = template.compile(profileTemplate);
            var html = render({
                adminUser:result.data
            });
            $('a#profile').html(html);
        }
    });
    $('a.logout').click(function () {
        commonAjax({
            url: '/rbac/admin/logout',
            success: function (result) {
                window.location.href = "/rbac";
            }
        });
    });

    var targetConfig = {
        "/rbac/version-license.html" : '/rbac/license.html',
        "/rbac/user_detail.html" : '/rbac/users.html',
        "/rbac/change_password.html" : '/rbac/users.html',
        "/rbac/group-rules.html" : '/rbac/groups.html',
    };
    var showPage = function(pid, resultMap){
        var trList = [];
        var list = resultMap[pid];

        trList.push('<ul class="nav lt">');
        for(var k in list) {
            var item = list[k];
            trList.push("<li>")
            trList.push('<a href="' + item.mca + '">');
            trList.push('<i class="fa fa-angle-right"></i> ');
            trList.push('<span class="fa ' + item.ico + ' icon"> &nbsp;&nbsp;' + item.name + '</span>');
            trList.push('</a>');
            if(resultMap[item.id] !== undefined) {
                trList.push(showPage(item.id, resultMap));
            }
            trList.push('</li>');
        }
        trList.push("</ul>");
        return trList.join('');
    };

    commonAjax({
        url : "/rbac/menu/show",
        data:{},
        dataType:'json',
        async:false,
        success:function(result) {
            var resultMap = {};
            for(var k in result.data) {
                var item = result.data[k];
                if(resultMap[item.pid] === undefined) {
                    resultMap[item.pid] = [];
                }
                resultMap[item.pid].push(item);
            }
            var liList = [];
            liList.push('<ul class="nav">');
            for(var key in resultMap[0]) {
                var item = resultMap[0][key];
                liList.push('<li>');
                liList.push('<a href="' + item.mca + '">');
                liList.push('<i class="fa ' + item.ico + ' icon"> <b class="bg-warning"></b></i>');
                liList.push('<span class="pull-right">');
                liList.push('<i class="fa fa-angle-down text"></i>');
                liList.push('<i class="fa fa-angle-up text-active"></i>');
                liList.push('</span>');
                liList.push('<span>' + item.name + '</span>');
                liList.push('</a>');
                liList.push(showPage(item.id, resultMap));
                liList.push('</li>');
            }
            liList.push('</ul>');
            $('#nav-container').html(liList.join(""));
        }
    });

    var url = window.location.href;
    var start = url.indexOf("/rbac");
    var end = url.indexOf("?");
    var target = url.substr(start, end > 0 ? (end - start) : url.length);
    $('ul.nav').find('a[href="' + (typeof targetConfig[target] === "undefined" ? target : targetConfig[target]) + '"]')
        .parent().addClass('active')
        .parent().prev().addClass('active')
        .parent().addClass('active');
})();