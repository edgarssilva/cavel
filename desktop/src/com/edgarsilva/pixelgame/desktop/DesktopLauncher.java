package com.edgarsilva.pixelgame.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.edgarsilva.pixelgame.PixelGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Cavel");
		config.setInitialVisible(true);
		config.setWindowedMode((int) PixelGame.WIDTH, (int) PixelGame.HEIGHT);
		config.useVsync(false);
		config.setIdleFPS(60);
		new Lwjgl3Application(new PixelGame(), config);

		//LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.title = "Cavel";
		//config.width  = (int) PixelGame.WIDTH;
		//config.height = (int) PixelGame.HEIGHT;
		//config.vSyncEnabled = false; // Setting to false disables vertical sync
		//config.foregroundFPS = 0; // Setting to 0 disables foreground fps throttling
		//config.backgroundFPS = 0; // Setting to 0 disables background fps throttling
		//new LwjglApplication(new PixelGame(), config);
	}
}
