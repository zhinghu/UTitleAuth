package com.undeadlydev.UTitleAuth.controllers;

import com.undeadlydev.UTitleAuth.TitleAuth;
import com.undeadlydev.UTitleAuth.nms.NMSReflectionNew;
import com.undeadlydev.UTitleAuth.nms.NMSReflectionOld;
import com.undeadlydev.UTitleAuth.superclass.NMSReflection;
import com.undeadlydev.UTitleAuth.utils.VersionUtils;

public class VersionController {

    private final TitleAuth plugin;
    private final NMSReflection reflection;

    public VersionController(TitleAuth plugin) {
        this.plugin = plugin;
        if (VersionUtils.getVersion().esMayorIgual(VersionUtils.v1_17_1)) {
            this.reflection = new NMSReflectionNew();
        } else {
            this.reflection = new NMSReflectionOld();
        }
    }

    public NMSReflection getReflection() {
        return reflection;
    }
}