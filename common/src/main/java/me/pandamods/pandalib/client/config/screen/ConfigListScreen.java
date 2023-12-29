package me.pandamods.pandalib.client.config.screen;

import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.client.config.screen.widgets.ModInfoWidget;
import me.pandamods.pandalib.config.ConfigHolder;
import me.pandamods.pandalib.config.ModConfigData;
import me.pandamods.pandalib.utils.IconHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigListScreen extends Screen {
	public static final ResourceLocation UNKNOWN_ICON = new ResourceLocation("textures/misc/unknown_pack.png");

	private final Screen parentScreen;
	private final String modId;
	private final Collection<ConfigHolder<?>> configs;
	private final Optional<Mod> mod;
	private final Optional<ModConfigData> modData;
	private final IconHandler iconHandler = new IconHandler();

	protected ConfigListScreen(Screen parentScreen, String modId, Collection<ConfigHolder<?>> configs) {
		super(Component.empty());
		this.parentScreen = parentScreen;
		this.modId = modId;
		this.configs = configs;

		this.mod = Platform.getOptionalMod(modId);

		this.modData = this.mod.map(mod -> {
			ResourceLocation iconLocation = new ResourceLocation(PandaLib.MOD_ID, modId + "_icon");
			Optional<String> iconPathString = mod.getLogoFile(64 * Minecraft.getInstance().options.guiScale().get());
			DynamicTexture icon = iconHandler.createIcon(mod, iconPathString.orElse(""));
			if (icon != null) {
				Minecraft.getInstance().getTextureManager().register(iconLocation, icon);
			} else {
				iconLocation = UNKNOWN_ICON;
			}

			return Optional.of(new ModConfigData(
					iconLocation,
					Component.literal(mod.getName()),
					Component.literal(mod.getDescription()),
					mod
			));
		}).orElse(Optional.empty());
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(parentScreen);
	}

	@Override
	protected void init() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter();
		GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(1);

		for (ConfigHolder<?> configHolder : configs) {
			rowHelper.addChild(Button.builder(ConfigScreen.GetConfigTitle(configHolder),
					button -> this.minecraft.setScreen(new ConfigScreen<>(this, configHolder))).build());
		}

		gridLayout.arrangeElements();
		Vector2i margin = new Vector2i(20);
		FrameLayout.alignInRectangle(gridLayout, margin.x, margin.y,
				this.width - margin.x, this.height - margin.y, 0.5F, 0.0F);
		gridLayout.visitWidgets(this::addRenderableWidget);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int i, int j, float f) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, i, j, f);
	}
}
