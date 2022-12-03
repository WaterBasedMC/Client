package com.waterbased.client.ui;

import com.waterbased.client.Client;
import com.waterbased.client.modules.Module;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public class SelectionGUI extends LightweightGuiDescription {
    public SelectionGUI() {
        WGridPanel gridRoot = new WGridPanel();
        WPlainPanel panel = new WPlainPanel();
        WScrollPanel scroll = new WScrollPanel(panel);
        scroll.setScrollingHorizontally(TriState.FALSE);
        scroll.setScrollingVertically(TriState.TRUE);
        gridRoot.add(scroll, 0, 0);
        setRootPanel(gridRoot);
        scroll.setSize(300, 400);
        panel.setSize(300, 400);
        gridRoot.setSize(300, 400);
        int offset = 0;
        for (Module module : Client.INSTANCE.MODULE_MANAGER.getModules()) {
            String hotkey = " (Hotkey: " + (module.getKey() == null ? "none" : InputUtil.fromKeyCode(module.getKey(), 1).getLocalizedText().getString()) + ")";
            WButton button = new WButton(MutableText.of(new LiteralTextContent(module.getName() + hotkey))
                    .styled(style -> style
                            .withColor(module.isEnabled() ? Formatting.GREEN : Formatting.RED)
                    )
            );
            button.setOnClick(() -> {
                module.toggleState();
                button.setLabel(MutableText.of(new LiteralTextContent(module.getName() + hotkey))
                        .styled(style -> style
                                .withColor(module.isEnabled() ? Formatting.GREEN : Formatting.RED)
                        )
                );
            });

            panel.add(button, 50, offset, 200, 150);
            offset+=20;
        }
        gridRoot.validate(this);
    }
}
