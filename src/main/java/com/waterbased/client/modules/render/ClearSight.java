package com.waterbased.client.modules.render;

import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;
import net.minecraft.client.util.InputUtil;

public class ClearSight extends Module {

    public ClearSight() {
        super("ClearSight", "Have a clear view, even under water/lava/powder_snow!", InputUtil.GLFW_KEY_O);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onKey() {
        Client.INSTANCE.MODULE_MANAGER.getModule(this.getClass()).toggleState();
    }

}
