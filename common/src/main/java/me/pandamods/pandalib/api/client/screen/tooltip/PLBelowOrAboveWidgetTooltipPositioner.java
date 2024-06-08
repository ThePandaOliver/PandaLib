package me.pandamods.pandalib.api.client.screen.tooltip;

import me.pandamods.pandalib.api.client.screen.elements.UIElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import org.joml.Vector2i;
import org.joml.Vector2ic;

@Environment(EnvType.CLIENT)
public class PLBelowOrAboveWidgetTooltipPositioner implements ClientTooltipPositioner {
    private final UIElement element;

    public PLBelowOrAboveWidgetTooltipPositioner(UIElement element) {
        this.element = element;
    }

    public Vector2ic positionTooltip(int screenWidth, int screenHeight, int mouseX, int mouseY, int tooltipWidth, int tooltipHeight) {
        Vector2i vector2i = new Vector2i();
        vector2i.x = this.element.getX() + 3;
        vector2i.y = this.element.getY() + this.element.getHeight() + 3 + 1;
        if (vector2i.y + tooltipHeight + 3 > screenHeight) {
            vector2i.y = this.element.getY() - tooltipHeight - 3 - 1;
        }

        if (vector2i.x + tooltipWidth > screenWidth) {
            vector2i.x = Math.max(this.element.getX() + this.element.getWidth() - tooltipWidth - 3, 4);
        }

        return vector2i;
    }
}
