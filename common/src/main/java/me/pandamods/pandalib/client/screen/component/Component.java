package me.pandamods.pandalib.client.screen.component;

public interface Component {
	<T extends Component> T addComponent(T component);
	<T extends Component> T removeComponent(T component);

}
