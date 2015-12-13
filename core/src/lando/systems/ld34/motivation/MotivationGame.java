package lando.systems.ld34.motivation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.Config;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.utils.SoundManager;

public class MotivationGame {

    // Positioning info for the game's UI
    // NOTE: All other X/Y positions within this class are relative to these positions.
    private final static float GAME_HEIGHT = 46f;
    private final static float GAME_WIDTH = 340f;
    private final static float GAME_X = Config.width/2 - 170;
    private final static float GAME_Y = 200f;

    private final static float BUTTON_HEIGHT = 34f;
    private final static float BUTTON_WIDTH = 80f;
    private final static float BUTTON_X = 254f;
    private final static float BUTTON_Y = 6f;

    private final static float BAR_HEIGHT = 34f; // pixels
    private final static float BAR_WIDTH = 242f; // pixels
    private final static float BAR_X = 6f; // pixels
    private final static float BAR_Y = 6f; // pixels

    private final static float INDICATOR_SPEED = 1.2f; // % of the bar per second (out of 1)

    private ResourceManager resourceManager;
    private ResourceManager.Resources resourceType;

    private float targetFalloffRangeU;  // The falloff area on either side of the target, out of 1
    private float targetRangeU; // The size of the target, out of 1
    private float targetU; // Where's the target?

    private Rectangle bg;
    private Rectangle bar;
    private Rectangle button;

    private float indicatorPosition; // Where along the bar to place the indicator, from 0 to 1
    private float indicatorDirection;

    private boolean isActive = false;

    // Construct -------------------------------------------------------------------------------------------------------

    /**
     * todo: take in the game manager so we can report motivations
     * @param resourceType String
     */
    public MotivationGame(ResourceManager resourceManager, ResourceManager.Resources resourceType) {

        this.resourceManager = resourceManager;
        this.resourceType = resourceType;

        // Let's start building the rectangles
        this.bg = new Rectangle(
                MotivationGame.GAME_X, MotivationGame.GAME_Y, MotivationGame.GAME_WIDTH, MotivationGame.GAME_HEIGHT
        );
        this.bar = new Rectangle(
                MotivationGame.GAME_X + MotivationGame.BAR_X,
                MotivationGame.GAME_Y + MotivationGame.BAR_Y,
                MotivationGame.BAR_WIDTH,
                MotivationGame.BAR_HEIGHT);
        this.button = new Rectangle(
                MotivationGame.GAME_X + MotivationGame.BUTTON_X,
                MotivationGame.GAME_Y + MotivationGame.BUTTON_Y,
                MotivationGame.BUTTON_WIDTH,
                MotivationGame.BUTTON_HEIGHT
        );

        this.setTargetRangeU(resourceManager.getWhipTargetRange(resourceType));
        this.setTargetFalloffRangeU(resourceManager.getWhipFalloffRange(resourceType));

        this.reset();
        this.start();

    }


    // Getter/Setters --------------------------------------------------------------------------------------------------

    public void setTargetRangeU(float targetRangeU) {
        this.targetRangeU = targetRangeU;
    }

    public void setTargetFalloffRangeU(float targetFalloffRangeU) {
        this.targetFalloffRangeU = targetFalloffRangeU;
    }

    public void setTargetU(float targetU) {
        this.targetU = targetU;
    }

    // -----------------------------------------------------------------------------------------------------------------


    public void reset() {
        this.indicatorPosition = 0; // All the way to the left
        this.indicatorDirection = 1; // Moving right
        // Position the target
        targetU = MathUtils.random(0, 1 - targetRangeU);    // NOTE: Falloff might be cut off; that's OK
    }

    public void render(SpriteBatch batch){

        Color c = batch.getColor();

        // Game BG
        Assets.nice2NinePatch.draw(batch, bg.x, bg.y, bg.width, bg.height);

        // Shader bar
        batch.setShader(Assets.motivationBarShader);
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Assets.motivationBarShader.setUniformf("u_target_x", targetU);
        Assets.motivationBarShader.setUniformf("u_target_width", targetRangeU);
        Assets.motivationBarShader.setUniformf("u_target_falloff_width", targetFalloffRangeU);
        Assets.motivationBarShader.setUniformf("u_indicator_x", indicatorPosition);
        Assets.motivationBarShader.setUniformf("u_indicator_dir", indicatorDirection);
        batch.draw(Assets.testTexture, bar.x, bar.y, bar.width, bar.height);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.setShader(null);

        // Button
        batch.setColor(Color.GREEN);
        batch.draw(
                Assets.whiteTexture,
                button.x,
                button.y,
                button.width,
                button.height
        );

        batch.setColor(c);

    }

    private void reportCurrentMotivationScore() {

        float score = 0f;

        // All measurements are from 0 to 1

        // Are we inside the falloff bounds?
        if (indicatorPosition >= targetU - targetFalloffRangeU
                && indicatorPosition <= targetU + targetRangeU + targetFalloffRangeU) {
            // Did they hit the target?
            if (indicatorPosition >= targetU
                    && indicatorPosition <= targetU + targetRangeU) {
                // Dead on!
                score = 1f;
            } else {
                // Determine where in the falloff they hit and score accordingly.
                float missDist;
                if (indicatorPosition < targetU) {
                    // This is before the target area
                    missDist = targetU - indicatorPosition;
                } else {
                    // They hit after the target area
                    missDist = indicatorPosition - (targetU + targetRangeU);
                }
                score = 1 - (missDist / targetFalloffRangeU);
            }
        }

        resourceManager.addEfficiency(resourceType, score);
        if (MathUtils.random() > score){
            resourceManager.removeSlaves(resourceType, 1);
        }
    }

    public void start() {
        this.isActive = true;
    }

    public void stop() {
        this.isActive = false;
    }

    public void update(float delta){

        if (!isActive) {
            return;
        }

        setTargetFalloffRangeU(resourceManager.getWhipFalloffRange(resourceType));

        // Click?
        if (Gdx.input.justTouched()) {
            int touchX = Gdx.input.getX();
            int touchY = (Config.height - Gdx.input.getY());
            if (button.contains(touchX, touchY)) {
                SoundManager.playWhip();
                SoundManager.playScream();
                this.reportCurrentMotivationScore();
                this.reset();
            }
        }

        // Indicator
        float indicatorDY = (MotivationGame.INDICATOR_SPEED * delta) * indicatorDirection;
        this.indicatorPosition += indicatorDY;
        // check bounds
        if (this.indicatorPosition >= 1) {
            this.indicatorDirection *= -1;
            this.indicatorPosition -= (this.indicatorPosition - 1);
        } else if (this.indicatorPosition <= 0) {
            this.indicatorDirection *= -1;
            this.indicatorPosition += (this.indicatorPosition * -1);
        }

    }

}