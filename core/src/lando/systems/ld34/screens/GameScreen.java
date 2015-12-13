package lando.systems.ld34.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.motivation.MotivationGame;
import lando.systems.ld34.utils.Assets;

/**
 * Brian Ploeckelman created on 12/9/2015.
 */
public class GameScreen extends AbstractScreen {

    final SpriteBatch batch;
    private MotivationGame mb;

    public GameScreen(LudumDare34 game) {
        super(game);
        batch = Assets.batch;
        mb = new MotivationGame("apples", 0.1f, 0.1f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        mb.update(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);

//        Gdx.gl.glClearColor(0f, 191f / 255f, 255f / 255f, 1f);
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
//        batch.draw(Assets.testTexture, 0, 0);
        mb.render(batch);
        batch.end();
    }

}
