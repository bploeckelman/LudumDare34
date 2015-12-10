package lando.systems.ld34.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import lando.systems.ld34.Config;
import lando.systems.ld34.LudumDare34;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(Config.width, Config.height);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new LudumDare34();
        }
}