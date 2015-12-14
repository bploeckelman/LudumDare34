package lando.systems.ld34.world.pyramid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.utils.Assets;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Brian on 12/12/2015.
 */
public class Pyramid {

    public float BuildSpeed = 40;
    public int BlockSize = 40;

    private boolean _addLeft = true;
    private int _groupIndex = 0;
    private int _groupMax = 1;

    private ArrayList<PyramidBlock> _blocks = new ArrayList<PyramidBlock>();
    private int _pyramidHeight = 0;

    private Rectangle _bounds;
    private ArrayList<LeftRight> _leftRightList = new ArrayList<LeftRight>();

    public Pyramid(Rectangle bounds) {
        _bounds = bounds;
        addLeftRight();
    }

    private void addLeftRight() {
        boolean isCentralBlock = (_leftRightList.size() % 2) == 0;

        float middle = _bounds.x + (_bounds.width / 2);

        LeftRight leftRight = new LeftRight();
        if (isCentralBlock) {
            leftRight.left = middle + (BlockSize / 2);
            leftRight.right = middle - (BlockSize / 2);
        } else {
            leftRight.left = leftRight.right = middle;
        }

        _leftRightList.add(leftRight);
    }

    private float _delay = 2f;

    public void update(float delta) {
        _delay -= delta;
        if (_delay > 0) return;

        // check for new blocks
        if ((int) LudumDare34.GameScreen.ResourceManager.getAmount(ResourceManager.Resources.BUILD) > _blocks.size()) {
            addBlock();
        }

        float dx = BuildSpeed * delta;

        for (PyramidBlock block : _blocks) {
            if (block.isPlaced) continue;

            if (block.movingUp) {
                block.bounds.y += dx;
            } else if (block.fromLeft) {
                block.bounds.x += dx;
            } else {
                block.bounds.x -= dx;
            }

            checkPosition(block);
        }
    }

    private void checkPosition(PyramidBlock block) {
        if (block.movingUp) {
            float maxY = _bounds.y + ((block.currentRow + 1) * BlockSize);
            if (block.bounds.y >= maxY) {
                block.currentRow++;
                block.movingUp = false;
            }
        } else if (block.fromLeft) {
            LeftRight lr = _leftRightList.get(block.currentRow);
            float max = lr.left - BlockSize;
            if (block.bounds.x >= max) {
                block.bounds.x = max;
                if (block.currentRow == block.finalRow) {
                    block.isPlaced = true;
                    _pyramidHeight = Math.max(_pyramidHeight, (block.finalRow + 1));
                    lr.left -= BlockSize;
                    lr.right = Math.max(lr.right, lr.left + BlockSize);
                } else {
                    block.movingUp = true;
                }
            }
        } else {
            LeftRight lr = _leftRightList.get(block.currentRow);
            float min = lr.right;
            if (block.bounds.x <= min) {
                block.bounds.x = min;
                if (block.currentRow == block.finalRow) {
                    block.isPlaced = true;
                    lr.right += BlockSize;
                    lr.left = Math.min(lr.left, lr.right - BlockSize);
                } else {
                    block.movingUp = true;
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.setColor(Color.RED);
        Assets.boxNinePatch.draw(batch, _bounds.x, _bounds.y, _bounds.width, _bounds.height);

        batch.setColor(Color.WHITE);
        for (PyramidBlock block : _blocks) {
            batch.draw(block.image, block.bounds.x, block.bounds.y, block.bounds.width, block.bounds.height);
        }
    }

    public int getHeight() {
        return _pyramidHeight;
    }

    private void addBlock() {
        PyramidBlock block = createBlock();
        int x = (int) ((_addLeft) ? _bounds.x - BlockSize : (_bounds.x + _bounds.width));
        block.bounds = new Rectangle(x, _bounds.y, BlockSize, BlockSize);
        block.fromLeft = _addLeft;
        block.finalRow = _groupIndex;
        _blocks.add(block);

        _groupIndex++;
        if (_groupIndex == _groupMax) {
            _groupIndex = 0;
            _groupMax++;
            _addLeft = !_addLeft;
            addLeftRight();
        }
    }

    private PyramidBlock createBlock() {
        PyramidBlock block = new PyramidBlock();
        block.image = getBlockTexture();
        block.currentRow = 0;
        return block;
    }

    private Random _rand = new Random();

    private Texture getBlockTexture() {
        return Assets.blockTexture;
//        return Assets.pryamidBlocks.get(_rand.nextInt(Assets.pryamidBlocks.size()));
    }
}