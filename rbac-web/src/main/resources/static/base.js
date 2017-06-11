/**
 * Created by pandaking on 2017/5/15.
 */
var targetConfig = {
        "/rbac/version-license.html" : '/rbac/license.html'
};
var commonAjax=function(opts) {
    var callback = opts.success;
    opts.success = function(result){
        if(result.code == 'login004') {
            window.location.href = "/rbac/signin.html";
        } else {
            callback(result);
        }
    };
    $.ajax(opts);
};

var getQueryString=function (currentLocation, name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = currentLocation.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
};

var buildPage = function(ele, result, callback) {
    $('#pagination').paging(result.total, {
        format: '[< ncn >]', // define how the navigation should look like and in which order onFormat() get's called
        perpage: result.size, // show 10 elements per page
        lapping: 0, // don't overlap pages for the moment
        page: 1, // start at page, can also be "null" or negative
        onSelect: function (page) {
            // add code which gets executed when user selects a page, how about $.ajax() or $(...).slice()?
            callback(page);
        },
        onFormat: function (type) {
            switch (type) {
                case 'block': // n and c
                    if (this.value != this.page)
                        return '<li><a href="#">' + this.value + '</a></li>';
                    return '<li class="active"><a href="#">' + this.value + '</a></li>';
                case 'prev': // [
                    return '<ul class="pagination pagination-sm m-t-sm m-b-none">' +
                        '<li><a href="#"><i class="fa fa-chevron-left"></i></a></li>';
                case 'next': // ]
                    return '<li><a href="#"><i class="fa fa-chevron-right"></i></a></li>' +
                        '</ul>';
                default :
                    return '';
            }
        }
    });
};


commonAjax({
    url: '/rbac/admin/user',
    dataType: 'json',
    success: function (result) {
        var adminUser = result.data;
        $('a#profile')
            .append('<span class="thumb-sm avatar pull-left"><img src="' + adminUser.avatar + '"></span>')
            .append(adminUser.username)
            .append('<b class="caret"></b>');
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


