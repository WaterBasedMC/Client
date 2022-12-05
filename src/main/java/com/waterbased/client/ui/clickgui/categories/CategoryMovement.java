package com.waterbased.client.ui.clickgui.categories;

import com.waterbased.client.modules.movement.Flight;
import com.waterbased.client.modules.movement.NoSlowDown;
import com.waterbased.client.modules.movement.SpectatorCam;
import com.waterbased.client.ui.clickgui.CategoryDrawable;

public class CategoryMovement extends CategoryDrawable {

    public CategoryMovement(int x, int y) {
        super("Movement", x, y);

        this.addEntry(new CategoryEntry(Flight.class));
        this.addEntry(new CategoryEntry(NoSlowDown.class));
        this.addEntry(new CategoryEntry(SpectatorCam.class));

    }



}
