package com.oracle.casb.seclore.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Set;

public enum FSPermissions {
    VIEW("View", (short) 2),
    LITE_VIEWER("Lite Viewer", (short) 6),
    EDIT("Edit", (short) 34),
    COPY_DATA("Copy Data", (short) 258),
    PRINT("Print", (short) 10),
    SCREEN_CAPTURE("Screen Capturer", (short) 514),
    MACRO("Macro", (short) 1026),
    FULL_CONTROL("Full  Control", (short) 170);

    private String name;
    private Short filePermission;

    private static Map<String, FSPermissions> permissionsMap
            = ImmutableMap.<String, FSPermissions>builder()
            .put("View", VIEW)
            .put("Lite Viewer", LITE_VIEWER)
            .put("Edit", EDIT)
            .put("Copy Data", COPY_DATA)
            .put("Print", PRINT)
            .put("Screen Capturer", SCREEN_CAPTURE)
            .put("Macro", MACRO)
            .put("Full  Control", FULL_CONTROL)
            .build();

    public static Short calculateFilePermissions(Set<FSPermissions> permissionSet) {
        short result = VIEW.filePermission;
        for (FSPermissions permission : permissionSet) {
            result |= permission.filePermission;
        }
        return Short.valueOf(result);
    }

    public static FSPermissions getFilePermisison(String permissionName) {
        if (permissionsMap.containsKey(permissionName)) {
            return permissionsMap.get(permissionName);
        } else {
            throw new RuntimeException("Invalid permisison :" + permissionName);
        }
    }

    FSPermissions(String name, Short filePermission) {
        this.name = name;
        this.filePermission = filePermission;
    }

    public String getName() {
        return name;
    }

    public Short getFilePermission() {
        return filePermission;
    }
}
