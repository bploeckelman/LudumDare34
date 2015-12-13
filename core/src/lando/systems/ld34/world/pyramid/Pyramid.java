package lando.systems.ld34.world.pyramid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.resources.ResourceManager;

/**
 * Created by Brian on 12/12/2015.
 */
public class Pyramid {

    public float BuildSpeed = 10;
    
    private boolean _addLeft = true;
    private int _blockIndex = 0;
    private int _groupCount = 1;

    private int _blockCount = 0;

    private int _placedBlocks = 0;

    public void update(float delta) {
        // check for new blocks
        if (LudumDare34.GameScreen.ResourceManager.getAmount(ResourceManager.Resources.BUILD) > _blockCount) {
            addBlock();
        }

    }

    public void render(SpriteBatch batch) {

    }

    public int getBlocks() {
        return _blockCount;
    }

    public int getPlacedBlocks() {
        return _placedBlocks;
    }

    private void addBlock() {

    }
}
