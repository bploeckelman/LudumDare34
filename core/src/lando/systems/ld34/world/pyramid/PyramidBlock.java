package lando.systems.ld34.world.pyramid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Brian on 12/12/2015.
 */
public class PyramidBlock {
    public Texture image;
    public Rectangle bounds;
    public boolean isPlaced;
    public int currentRow;
    public int finalRow;
    //public Rectangle finalPosition;
    public boolean fromLeft;
    public boolean movingUp;
}
