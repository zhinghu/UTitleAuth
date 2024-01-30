package com.undeadlydev.UTitleAuth.controllers;

import com.undeadlydev.UTitleAuth.TitleAuth;
import com.undeadlydev.UTitleAuth.nms.NMSReflectionNew;
import com.undeadlydev.UTitleAuth.nms.NMSReflectionOld;
import com.undeadlydev.UTitleAuth.superclass.NMSReflection;
import com.undeadlydev.UTitleAuth.enums.Versions;

public class VersionController {

    private final TitleAuth plugin;
    private final NMSReflection reflection;

    public VersionController(TitleAuth plugin) {
        this.plugin = plugin;
        if (Versions.getVersion().esMayorIgual(Versions.v1_16)) {
            this.reflection = new NMSReflectionNew();
        } else {
            this.reflection = new NMSReflectionOld();
        }
    }

    public NMSReflection getReflection() {
        return reflection;
    }
}