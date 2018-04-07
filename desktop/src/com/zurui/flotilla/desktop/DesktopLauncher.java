package com.zurui.flotilla.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zurui.flotilla.Game;
import com.zurui.flotilla.global.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) Constants.EXACT_X;
		config.height = (int) Constants.EXACT_Y;
		new LwjglApplication(new Game(), config);
	}
}
