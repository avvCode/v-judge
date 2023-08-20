package com.vv.sandbox.security;

import java.security.Permission;

/**
 * @author vv
 */
public class DenySecurityManager extends SecurityManager{
    @Override
    public void checkPermission(Permission perm) {
        super.checkPermission(perm);
    }

    @Override
    public void checkRead(String file) {
        super.checkRead(file);
    }

}
