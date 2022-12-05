package com.waterbased.client.ui.clickgui.categories;

import com.waterbased.client.modules.render.ClearSight;
import com.waterbased.client.modules.render.EntityESP;
import com.waterbased.client.modules.render.NightVision;
import com.waterbased.client.ui.clickgui.CategoryDrawable;

public class CategoryRender extends CategoryDrawable {

    public CategoryRender(int x, int y) {
        super("Render", x, y);

        this.addEntry(new CategoryEntry(ClearSight.class));
        this.addEntry(new CategoryEntry(EntityESP.class));
        this.addEntry(new CategoryEntry(NightVision.class));

    }



}
