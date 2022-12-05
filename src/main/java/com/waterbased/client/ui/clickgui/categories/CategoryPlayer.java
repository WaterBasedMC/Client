package com.waterbased.client.ui.clickgui.categories;

import com.waterbased.client.modules.player.NoFall;
import com.waterbased.client.ui.clickgui.CategoryDrawable;

public class CategoryPlayer extends CategoryDrawable {

    public CategoryPlayer(int x, int y) {
        super("Player", x, y);

        this.addEntry(new CategoryModuleEntry(NoFall.class));
    }



}
