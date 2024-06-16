package me.pandamods.pandalib.api.client.screen.elements.widgets.inputs;

import me.pandamods.pandalib.api.client.screen.elements.UIElementHolder;
import me.pandamods.pandalib.api.client.screen.elements.widgets.PLEditBox;
import net.minecraft.network.chat.Component;

public class SliderInput extends UIElementHolder {
	private final Slider slider;
	private final PLEditBox editBox;

	public SliderInput(double min, double max, float steps) {
		this.slider = new Slider(min, max, steps);
		this.editBox = new PLEditBox(this.getMinecraft().font, Component.empty());

		this.slider.onChangeListener(() -> this.editBox.setValue(Double.toString(this.slider.getValue())));
		this.editBox.setResponder(s -> this.slider.setValue(Double.parseDouble(s)));
	}

	public Slider getSlider() {
		return slider;
	}

	public PLEditBox getEditBox() {
		return editBox;
	}

	@Override
	public void init() {
		this.editBox.setSize(50, getHeight());
		this.slider.setSize(getWidth() - this.editBox.getWidth() - 2, getHeight());

		this.editBox.setRelativeX(this.slider.getWidth() + 2);

		this.addElement(slider);
		this.addElement(editBox);
	}

	public void setValue(double value) {
		this.editBox.setValue(Double.toString(value));
	}

	public double getValue() {
		return Double.parseDouble(this.editBox.getValue());
	}
}
