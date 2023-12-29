package me.pandamods.example.config;

import me.pandamods.example.PandaLibExample;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.config.Config;

@Config(modId = PandaLib.MOD_ID, name = "client", parentDirectory = PandaLibExample.MOD_ID)
public class ClientConfig {
	public String exampleString = "";
	public String exampleString2 = "";
	public String exampleString3 = "";
	public String exampleString4 = "";
	public String exampleString5 = "";
	public String exampleString6 = "";
	public String exampleString7 = "";
	public String exampleString8 = "";
	public String exampleString9 = "";
	public String exampleString10 = "";
	public float exampleFloat = 0;
	public int exampleInt = 0;
	public boolean exampleBoolean = false;
	public ExampleClass exampleClass = new ExampleClass();

	public static class ExampleClass {
		public String exampleString = "";
	}
}
