package com.waterbased.client.ui.clickgui.categories;

import com.waterbased.client.modules.movement.*;
import com.waterbased.client.ui.clickgui.CategoryDrawable;

public class CategoryMovement extends CategoryDrawable {

    public CategoryMovement(int x, int y) {
        super("Movement", x, y);

        this.addEntry(new CategoryModuleEntry(BouncySlime.class));
        this.addEntry(new CategoryModuleEntry(Flight.class));
        this.addEntry(new CategoryModuleEntry(NoSlowDown.class));
        this.addEntry(new CategoryModuleEntry(SpectatorCam.class));
        this.addEntry(new CategoryModuleEntry(VehicleFlight.class));

    }



}
