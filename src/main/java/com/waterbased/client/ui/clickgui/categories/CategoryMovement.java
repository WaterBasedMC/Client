package com.waterbased.client.ui.clickgui.categories;

import com.waterbased.client.modules.movement.BouncySlime;
import com.waterbased.client.modules.movement.Flight;
import com.waterbased.client.modules.movement.NoSlowDown;
import com.waterbased.client.modules.movement.SpectatorCam;
import com.waterbased.client.ui.clickgui.CategoryDrawable;

public class CategoryMovement extends CategoryDrawable {

    public CategoryMovement(int x, int y) {
        super("Movement", x, y);

        this.addEntry(new CategoryModuleEntry(BouncySlime.class));
        this.addEntry(new CategoryModuleEntry(Flight.class));
        this.addEntry(new CategoryModuleEntry(NoSlowDown.class));
        this.addEntry(new CategoryModuleEntry(SpectatorCam.class));

    }



}
