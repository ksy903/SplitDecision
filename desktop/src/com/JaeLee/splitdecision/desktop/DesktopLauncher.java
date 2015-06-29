package com.JaeLee.splitdecision.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.JaeLee.splitdecision.SplitDecision;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 1000;
		config.width = 800;
		config.vSyncEnabled = true;
		new LwjglApplication(new SplitDecision(), config);
	}
}
