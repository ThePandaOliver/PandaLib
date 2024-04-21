package me.pandamods.pandalib.api.utils.screen;

import net.minecraft.resources.ResourceLocation;

public class WidgetImage {
	private final ResourceLocation defaultLocation;
	private final ResourceLocation hoveredLocation;
	private final ResourceLocation disabledLocation;

	public WidgetImage(ResourceLocation defaultLocation, ResourceLocation disabledLocation) {
		this(defaultLocation, disabledLocation, defaultLocation);
	}

	public WidgetImage(ResourceLocation defaultLocation, ResourceLocation hoveredLocation, ResourceLocation disabledLocation) {
		this.defaultLocation = defaultLocation;
		this.hoveredLocation = hoveredLocation;
		this.disabledLocation = disabledLocation;
	}

	public ResourceLocation get(boolean isActive) {
		return get(isActive, false);
	}

	public ResourceLocation get(boolean isActive, boolean isHovered) {
		return isActive ? isHovered ? hoveredLocation : defaultLocation : disabledLocation;
	}
}
