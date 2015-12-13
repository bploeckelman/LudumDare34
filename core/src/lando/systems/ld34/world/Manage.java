package lando.systems.ld34.world;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Manage {

    public enum Type { WORKERS, SLAVES, PHAROAH, UPGRADES, RESOURCES }

    protected final Type type;
    public Rectangle bounds;

    public Manage(Type type, Rectangle bounds) {
        this.type = type;
        this.bounds = bounds;
    }

    public abstract void update(float delta);
    public abstract void render(SpriteBatch batch);

}
