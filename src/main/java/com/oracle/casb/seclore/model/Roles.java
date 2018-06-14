package com.oracle.casb.seclore.model;

import java.util.EnumSet;

public enum Roles {
    EXTERNAL(EnumSet.noneOf(FSPermissions.class)),
    OWNER(EnumSet.allOf(FSPermissions.class)),
    VIEWER(EnumSet.of(FSPermissions.VIEW, FSPermissions.LITE_VIEWER)),
    PUBLISHER(EnumSet.of(FSPermissions.VIEW, FSPermissions.LITE_VIEWER, FSPermissions.COPY_DATA,  FSPermissions.PRINT)),
    CONTRIBUTOR(EnumSet.of(FSPermissions.VIEW, FSPermissions.LITE_VIEWER,
            FSPermissions.COPY_DATA, FSPermissions.EDIT, FSPermissions.PRINT, FSPermissions.SCREEN_CAPTURE));

    private EnumSet<FSPermissions> permissions;

    public EnumSet<FSPermissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(EnumSet<FSPermissions> permissions) {
        this.permissions = permissions;
    }

    Roles(EnumSet<FSPermissions> fsPermissions) {
        this.permissions = fsPermissions;
    }
}
