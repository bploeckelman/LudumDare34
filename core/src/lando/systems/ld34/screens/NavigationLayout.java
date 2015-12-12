package lando.systems.ld34.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Brian on 12/12/2015.
 */
public class NavigationLayout {

    final AbstractScreen _screen;

    public NavigationLayout(AbstractScreen screen) {
        _screen = screen;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();


        batch.end();
    }

    public void update() {
        Vector3 mousePos = _screen.getMouseScreenPos();
    }
}
