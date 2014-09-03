package ch.gaspardrosay.spaceattack.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import ch.gaspardrosay.spaceattack.MainSpaceAttack;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(1024, 1280);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new MainSpaceAttack();
        }
}