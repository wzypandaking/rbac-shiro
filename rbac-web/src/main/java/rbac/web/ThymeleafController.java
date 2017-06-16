package rbac.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rbac.RbacPermissions;

/**
 * Created by pandaking on 2017/5/26.
 */
@Controller
public class ThymeleafController {

    @RequestMapping(value = "{template}.html", method = RequestMethod.GET)
    public String show(@PathVariable String template) {
        return String.format("%s", template);
    }

}
