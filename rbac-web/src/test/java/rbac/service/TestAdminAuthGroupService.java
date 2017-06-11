package rbac.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rbac.ApplicationTest;
import rbac.dao.repository.AdminAuthGroup;

/**
 * Created by pandaking on 2017/6/4.
 */
public class TestAdminAuthGroupService extends ApplicationTest {

    @Autowired
    private AdminAuthGroupService adminAuthGroupService;




    @Test
    public void testDelete() {
        AdminAuthGroup group = new AdminAuthGroup();
        group.setId(4l);
        adminAuthGroupService.delete(group);
    }
}
