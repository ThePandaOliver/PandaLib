package me.pandamods.example.config;

import me.pandamods.example.PandaLibExample;
import me.pandamods.pandalib.config.Config;

@Config(modId = PandaLibExample.MOD_ID, name = "client", parentDirectory = PandaLibExample.MOD_ID)
public class ClientConfig {
	public String exampleString = "";
	public float exampleFloat = 0;
	public int exampleInt = 0;
	public boolean exampleBoolean = false;
	public ExampleClass exampleClass = new ExampleClass();

	public static class ExampleClass {
		public String exampleString = "";
	}
}
