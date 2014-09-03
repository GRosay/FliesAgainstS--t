package ch.gaspardrosay.spaceattack.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ch.gaspardrosay.spaceattack.MainSpaceAttack;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Space Attack!";
        config.width = 1024;
        config.height = 1280;
		new LwjglApplication(new MainSpaceAttack(new DesktopGoogleServices()), config);
	}
}
