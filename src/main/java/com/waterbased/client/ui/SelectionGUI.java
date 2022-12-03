package com.waterbased.client.ui;

import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public class SelectionGUI extends LightweightGuiDescription {
    public SelectionGUI() {
        WPlainPanel root = new WPlainPanel();
        WGridPanel panel = new WGridPanel();
        WScrollPanel scroll = new WScrollPanel(panel);
        scroll.setScrollingHorizontally(TriState.FALSE);
        scroll.setScrollingVertically(TriState.TRUE);
        root.add(scroll, 0, 0);
        setRootPanel(root);
        scroll.setSize(300, 400);
        root.setSize(300, 400);
        int offset = 0;
        for (Module module : Client.INSTANCE.MODULE_MANAGER.getModules()) {
            WButton button = new WButton(MutableText.of(new LiteralTextContent(module.getName()))
                    .styled(style -> style
                            .withColor(module.isEnabled() ? Formatting.GREEN : Formatting.RED)
                    )
            );
            button.setOnClick(() -> {
                module.toggleState();
                button.setLabel(MutableText.of(new LiteralTextContent(module.getName()))
                        .styled(style -> style
                                .withColor(module.isEnabled() ? Formatting.GREEN : Formatting.RED)
                        )
                );
            });

            panel.add(button, 1, offset, 15, 20);
            offset++;
        }
        root.validate(this);
    }
}
