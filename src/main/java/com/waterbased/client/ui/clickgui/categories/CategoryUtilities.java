package com.waterbased.client.ui.clickgui.categories;

import com.waterbased.client.modules.render.ClearSight;
import com.waterbased.client.modules.render.EntityESP;
import com.waterbased.client.modules.render.NightVision;
import com.waterbased.client.modules.utilities.AntiCactus;
import com.waterbased.client.modules.utilities.PacketLogger;
import com.waterbased.client.modules.utilities.PlayerAlert;
import com.waterbased.client.ui.clickgui.CategoryDrawable;

public class CategoryUtilities extends CategoryDrawable {

    public CategoryUtilities(int x, int y) {
        super("Utilities", x, y);

        this.addEntry(new CategoryModuleEntry(AntiCactus.class));
        this.addEntry(new CategoryModuleEntry(PlayerAlert.class));
        this.addEntry(new CategoryModuleEntry(PacketLogger.class));
    }
}
