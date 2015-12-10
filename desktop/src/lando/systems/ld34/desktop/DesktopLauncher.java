package lando.systems.ld34.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import lando.systems.ld34.Config;
import lando.systems.ld34.LudumDare34;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Config.width;
		config.height = Config.height;
		new LwjglApplication(new LudumDare34(), config);
	}
}
