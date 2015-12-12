package lando.systems.ld34.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld34.screens.GameScreen;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */
public abstract class Area {

    public enum Type { PYRAMID, MGMT, QUARRY, FIELD, FOREST }

    protected final GameScreen gameScreen;
    protected final Type type;
    protected float worldX;

    public Area(GameScreen gameScreen, Type type) {
        this.gameScreen = gameScreen;
        this.type = type;
    }

    public abstract void update(float delta);
    public abstract void render(SpriteBatch batch);

}
