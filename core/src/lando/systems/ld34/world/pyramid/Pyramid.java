package lando.systems.ld34.world.pyramid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.utils.Assets;

import java.util.ArrayList;

/**
 * Created by Brian on 12/12/2015.
 */
public class Pyramid {

    public float BuildSpeed = 40;
    public int BlockSize = 64;

    private boolean _addLeft = true;
    private int _groupIndex = 0;
    private int _groupMax = 1;

    private ArrayList<PyramidBlock> _blocks = new ArrayList<PyramidBlock>();
    private int _pyramidHeight = 0;

    private Rectangle _bounds;
    private ArrayList<LeftRight> _leftRightList = new ArrayList<LeftRight>();

    private float _scale = 1;

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

   // float _time = 0;

    public void update(float delta) {
/*
        _time += delta;
        if (_time > 2) {
            addBlock();
            _time = 0;
        }
*/

        // check for new blocks
        if ((int) LudumDare34.GameScreen.resourceManager.getAmount(ResourceManager.Resources.BUILD) > _blocks.size()) {
            addBlock();
        }

        float dx = BuildSpeed * delta * _scale;

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
                block.bounds.y = maxY;
                block.currentRow++;
                block.movingUp = false;
            }
        } else if (block.fromLeft) {
            LeftRight lr = _leftRightList.get(block.currentRow);
            float max = lr.left - BlockSize;
            if (block.bounds.x >= max) {
                block.bounds.x = max;
                if (block.currentRow == block.finalRow) {
                    placeBlock(block);
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
                    placeBlock(block);
                    lr.right += BlockSize;
                    lr.left = Math.min(lr.left, lr.right - BlockSize);
                } else {
                    block.movingUp = true;
                }
            }
        }
    }

    private void placeBlock(PyramidBlock block) {
        block.isPlaced = true;
        _pyramidHeight = Math.max(_pyramidHeight, (block.finalRow + 1));

        if (((BlockSize / _scale) * _pyramidHeight) > _bounds.height) {
            _scale *= 2;
        }
    }

    public void render(SpriteBatch batch) {
        //batch.setColor(Color.RED);
        //Assets.boxNinePatch.draw(batch, _bounds.x, _bounds.y, _bounds.width, _bounds.height);

        float scaleOffset = ((_scale - 1) / (_scale * 2));
        float xOffset = _bounds.width * scaleOffset;
        float yOffset = _bounds.y * ((_scale -1) / _scale);
        batch.setColor(Color.WHITE);
        for (PyramidBlock block : _blocks) {
             batch.draw(block.image, xOffset + block.bounds.x / _scale, yOffset + block.bounds.y / _scale, block.bounds.width /_scale, block.bounds.height /_scale);
        }
    }

    public int getHeight() {
        return _pyramidHeight;
    }

    private void addBlock() {

        float scaleOffset = ((_scale - 1) / (_scale * 2));
        float xOffset = _bounds.width * _scale * scaleOffset;

        PyramidBlock block = createBlock();
        int x = (int) ((_addLeft) ? _bounds.x - (BlockSize /_scale) - xOffset
                : (_bounds.x + _bounds.width) + xOffset);

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

    private Texture getBlockTexture() {
        return Assets.blockTexture;
    }
}