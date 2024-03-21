package me.pandamods.pandalib.client.screen.api;

import java.util.List;
import java.util.Optional;

public interface WidgetHolder {
	List<Widget> widgets();

	default void onMouseMove(double mouseX, double mouseY) {
		widgets().stream().filter(Widget::isHovered).forEach(widget -> widget.mouseMoved(mouseX, mouseY));
	}

	default boolean onMouseClick(double mouseX, double mouseY, int button) {
		Optional<Widget> widgetOptional = widgets().stream().filter(Widget::isHovered).findFirst();
		return widgetOptional.map(widget -> widget.mouseClicked(mouseX, mouseY, button)).orElse(false);
	}

	default boolean onMouseRelease(double mouseX, double mouseY, int button) {
		Optional<Widget> widgetOptional = widgets().stream().filter(Widget::isHovered).findFirst();
		return widgetOptional.map(widget -> widget.mouseReleased(mouseX, mouseY, button)).orElse(false);
	}

	default boolean onMouseDrag(double mouseX, double mouseY, int button, double dragX, double dragY) {
		Optional<Widget> widgetOptional = widgets().stream().filter(Widget::isHovered).findFirst();
		return widgetOptional.map(widget -> widget.mouseDragged(mouseX, mouseY, button, dragX, dragY)).orElse(false);
	}

	default boolean onMouseScroll(double mouseX, double mouseY, double delta) {
		Optional<Widget> widgetOptional = widgets().stream().filter(Widget::isHovered).findFirst();
		return widgetOptional.map(widget -> widget.mouseScrolled(mouseX, mouseY, delta)).orElse(false);
	}

	default boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
		Optional<Widget> widgetOptional = widgets().stream().filter(Widget::isHovered).findFirst();
		return widgetOptional.map(widget -> widget.keyPressed(keyCode, scanCode, modifiers)).orElse(false);
	}

	default boolean onKeyRelease(int keyCode, int scanCode, int modifiers) {
		Optional<Widget> widgetOptional = widgets().stream().filter(Widget::isHovered).findFirst();
		return widgetOptional.map(widget -> widget.keyReleased(keyCode, scanCode, modifiers)).orElse(false);
	}

	default boolean onCharType(char codePoint, int modifiers) {
		Optional<Widget> widgetOptional = widgets().stream().filter(Widget::isHovered).findFirst();
		return widgetOptional.map(widget -> widget.charTyped(codePoint, modifiers)).orElse(false);
	}
}
