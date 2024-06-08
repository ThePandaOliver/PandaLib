package me.pandamods.pandalib.api.client.screen.tooltip;

import me.pandamods.pandalib.api.client.screen.elements.UIElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.util.Mth;
import org.joml.Vector2i;
import org.joml.Vector2ic;

@Environment(EnvType.CLIENT)
public class PLMenuTooltipPositioner implements ClientTooltipPositioner {
    private static final int MARGIN = 5;
    private static final int MOUSE_OFFSET_X = 12;
    public static final int MAX_OVERLAP_WITH_WIDGET = 3;
    public static final int MAX_DISTANCE_TO_WIDGET = 5;
    private final UIElement element;

    public PLMenuTooltipPositioner(UIElement element) {
        this.element = element;
    }

    public Vector2ic positionTooltip(int screenWidth, int screenHeight, int mouseX, int mouseY, int tooltipWidth, int tooltipHeight) {
        Vector2i vector2i = new Vector2i(mouseX + 12, mouseY);
        if (vector2i.x + tooltipWidth > screenWidth - 5) {
            vector2i.x = Math.max(mouseX - 12 - tooltipWidth, 9);
        }

        vector2i.y += 3;
        int i = tooltipHeight + 3 + 3;
        int j = this.element.getY() + this.element.getHeight() + 3 + getOffset(0, 0, this.element.getHeight());
        int k = screenHeight - 5;
        if (j + i <= k) {
            vector2i.y += getOffset(vector2i.y, this.element.getY(), this.element.getHeight());
        } else {
            vector2i.y -= i + getOffset(vector2i.y, this.element.getY() + this.element.getHeight(), this.element.getHeight());
        }

        return vector2i;
    }

    private static int getOffset(int mouseY, int widgetY, int widgetHeight) {
        int i = Math.min(Math.abs(mouseY - widgetY), widgetHeight);
        return Math.round(Mth.lerp((float)i / (float)widgetHeight, (float)(widgetHeight - 3), 5.0F));
    }
}
