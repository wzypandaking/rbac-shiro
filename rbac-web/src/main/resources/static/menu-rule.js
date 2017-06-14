/**
 * Created by pandaking on 2017/6/6.
 */
/**
 * 加载菜单权限
 * @param ruleName
 */
function loadMenuRules(ruleName) {
    commonAjax({
        url:'/rbac/rules/list',
        dataType:'json',
        success:function (result) {
            if (!result.success) {
                layer.msg(result.message);
                return;
            }
            var optList = ['<option value="">请选择菜单权限</option>'];
            for(var k in result.data) {
                var item = result.data[k];
                if (ruleName === item.name) {
                    optList.push('<option value="' + item.uuid + '" selected="selected">' + item.title + '&nbsp;&nbsp;(' + item.name + ')' + '</option>');
                } else {
                    optList.push('<option value="' + item.uuid + '">' + item.title + '&nbsp;&nbsp;(' + item.name + ')' + '</option>');
                }
            }
            $('#select2-option').html(optList.join('')).select2();
        }
    });
}