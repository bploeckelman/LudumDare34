package lando.systems.ld34.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.utils.Assets;

/**
 * Brian Ploeckelman created on 12/9/2015.
 */
public class GameScreen extends AbstractScreen {

    final SpriteBatch batch;
    final NavigationLayout layout;

    public GameScreen(LudumDare34 game) {
        super(game);
        batch = Assets.batch;
        layout = new NavigationLayout(this);
    }

    @Override
    public void update(float delta) {

        super.update(delta);
        layout.update();
    }

    @Override
    public void render(float delta) {
        update(delta);

        batch.setProjectionMatrix(camera.combined);

        // draw

        layout.render(batch, uiCamera);
    }

}
