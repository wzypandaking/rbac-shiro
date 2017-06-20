package org.springframework.rbac;

/**
 * Created by pandaking on 2017/5/31.
 */
public enum Permissions {
    

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
}