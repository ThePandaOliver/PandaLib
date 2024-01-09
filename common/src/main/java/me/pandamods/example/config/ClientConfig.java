package me.pandamods.example.config;

import me.pandamods.example.PandaLibExample;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.config.Config;

@Config(modId = PandaLib.MOD_ID, name = "client", parentDirectory = PandaLibExample.MOD_ID)
public class ClientConfig {
	public String exampleString = "";
	@Config.Gui.Category("category1")
	public String exampleString2 = "";
	@Config.Gui.Category("category1")
	public String exampleString3 = "";
	@Config.Gui.Category("category1")
	public String exampleString4 = "";
	public String exampleString5 = "";
	public String exampleString6 = "";
	@Config.Gui.Category("category2")
	public String exampleString7 = "";
	@Config.Gui.Category("category2")
	public String exampleString8 = "";
	public String exampleString9 = "";
	public String exampleString10 = "";
	public float exampleFloat = 0;
	public int exampleInt = 0;
	public boolean exampleBoolean = false;
	@Config.Gui.Category(value = "example_class", isObject = true)
	public ExampleClass exampleClass = new ExampleClass();

	public static class ExampleClass {
		public String exampleString = "";
		public float exampleFloat = 0;
		@Config.Gui.Category("category3")
		public int exampleInt = 0;
		@Config.Gui.Category("category3")
		public boolean exampleBoolean = false;
	}
}
