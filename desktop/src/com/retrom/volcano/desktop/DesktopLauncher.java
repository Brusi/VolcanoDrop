package com.retrom.volcano.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.retrom.volcano.Volcano;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = Math.round(config.width * 16f / 9);
		config.width = 400;
		config.height = 700;
		config.samples = 2;
		System.out.println(""+config.width + ":" + config.height);
		new LwjglApplication(new Volcano(), config);
	}
}
