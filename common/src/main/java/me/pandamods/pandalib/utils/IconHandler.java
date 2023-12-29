package me.pandamods.pandalib.utils;

import com.mojang.blaze3d.platform.NativeImage;
import dev.architectury.platform.Mod;
import me.pandamods.pandalib.PandaLib;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.apache.commons.lang3.Validate;

import java.io.Closeable;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IconHandler implements Closeable {
	private final Map<Path, DynamicTexture> modIconCache = new HashMap<>();

	public DynamicTexture createIcon(Mod iconSource, String iconPath) {
		try {
			Path path = iconSource.findResource(iconPath).get();
			DynamicTexture cachedIcon = getCachedModIcon(path);
			if (cachedIcon != null) {
				return cachedIcon;
			}
			cachedIcon = getCachedModIcon(path);
			if (cachedIcon != null) {
				return cachedIcon;
			}
			try (InputStream inputStream = Files.newInputStream(path)) {
				NativeImage image = NativeImage.read(Objects.requireNonNull(inputStream));
				Validate.validState(image.getHeight() == image.getWidth(), "Must be square icon");
				DynamicTexture tex = new DynamicTexture(image);
				cacheModIcon(path, tex);
				return tex;
			}

		} catch (IllegalStateException e) {
			if (e.getMessage().equals("Must be square icon")) {
				PandaLib.LOGGER.error("Mod icon must be a square for icon source {}: {}", iconSource.getModId(), iconPath, e);
			}

			return null;
		} catch (Throwable t) {
			if (!iconPath.equals("assets/" + iconSource.getModId() + "/icon.png")) {
				PandaLib.LOGGER.error("Invalid mod icon for icon source {}: {}", iconSource.getModId(), iconPath, t);
			}
			return null;
		}
	}

	@Override
	public void close() {
		for (DynamicTexture tex : modIconCache.values()) {
			tex.close();
		}
	}

	DynamicTexture getCachedModIcon(Path path) {
		return modIconCache.get(path);
	}

	void cacheModIcon(Path path, DynamicTexture tex) {
		modIconCache.put(path, tex);
	}
}
