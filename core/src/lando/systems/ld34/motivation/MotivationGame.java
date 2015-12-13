package lando.systems.ld34.motivation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
    private final static float GAME_HEIGHT = 140f;
    private final static float GAME_WIDTH = 340f;
    private final static float GAME_X = Config.width/2 - 170;
    private final static float GAME_Y = 200f;

    private final static float BUTTON_HEIGHT = 40f;
    private final static float BUTTON_WIDTH = 80f;
    private final static float BUTTON_X = 130f;
    private final static float BUTTON_Y = 20f;

    private final static float BAR_HEIGHT = 40f; // pixels
    private final static float BAR_WIDTH = 300f; // pixels
    private final static float BAR_X = 20f; // pixels
    private final static float BAR_Y = 80f; // pixels

    private final static float INDICATOR_SPEED = 1.2f; // % of the bar per second (out of 1)
    private final static float INDICATOR_WIDTH = 2f; // pixels

    private ResourceManager resourceManager;
    private ResourceManager.Resources resourceType;
    private float targetRange; // The size of the target, out of 1
    private float targetFalloffRange;  // The falloff area on either side of the target, out of 1

    private Rectangle bg;
    private Rectangle bar;
    private Rectangle button;

    private Rectangle target;
    private Rectangle targetFalloff; // Covers the falloff on either side of the target
    private float targetRangeWidth;

    private Rectangle indicator;
    private float indicatorPosition; // Where along the bar to place the indicator, from 0 to 1
    private float indicatorMinX;
    private float indicatorRangeWidth;
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
        this.indicator = new Rectangle(
                0,  // Set when we update
                this.bar.y,
                MotivationGame.INDICATOR_WIDTH,
                this.bar.height
        );
        this.target = new Rectangle(
                0,  // Set when the targetRange is setter is called
                this.bar.y,
                0,  // Set when the targetRange is setter is called
                this.bar.height
        );
        this.targetFalloff = new Rectangle(
                0,  // Set when the targetFalloffRange is setter is called
                this.bar.y,
                0,  // Set when the targetFalloffRange is setter is called
                this.bar.height
        );

        // Computed values
        this.indicatorMinX = this.bar.x;
        this.indicatorRangeWidth = this.bar.width - MotivationGame.INDICATOR_WIDTH;

        this.setTargetRange(resourceManager.getWhipTargetRange(resourceType));
        this.setTargetFalloffRange(resourceManager.getWhipFalloffRange(resourceType));

        this.reset();
        this.start();

    }


    // Getter/Setters --------------------------------------------------------------------------------------------------

    public void setTargetRange(float targetRange) {
        this.targetRange = targetRange;
        this.target.width = bar.width * this.targetRange;
        this.updateTargetRangeWidth();
        this.reset();
    }

    public void setTargetFalloffRange(float targetFalloffRange) {
        this.targetFalloffRange = targetFalloffRange;
        this.targetFalloff.width = bar.width * ((this.targetFalloffRange * 2) + this.targetRange);
        this.updateTargetRangeWidth();
        //this.reset();
    }

    private void updateTargetRangeWidth() {
        this.targetRangeWidth = bar.width - this.targetFalloff.width;
    }


    // -----------------------------------------------------------------------------------------------------------------


    public void reset() {
        this.indicatorPosition = 0; // All the way to the left
        this.indicatorDirection = 1; // Moving right
        // todo: select and place target
        // Position the target
        this.targetFalloff.x = bar.x + (MathUtils.random() * this.targetRangeWidth);
        this.target.x = this.targetFalloff.x + (this.targetFalloffRange * MotivationGame.BAR_WIDTH);
    }

    public void render(SpriteBatch batch){

        Color c = batch.getColor();


        // Game BG
        batch.setColor(Color.GRAY);
        batch.draw(Assets.whiteTexture, bg.x, bg.y, bg.width, bg.height);

        // Bar
        batch.setColor(Color.BLUE);
        batch.draw(Assets.whiteTexture, bar.x, bar.y, bar.width, bar.height);


        // Target
        batch.setColor(Color.ORANGE);
        batch.draw(
                Assets.whiteTexture,
                targetFalloff.x,
                targetFalloff.y,
                targetFalloff.width,
                targetFalloff.height
        );
        batch.setColor(Color.RED);
        batch.draw(
                Assets.whiteTexture,
                target.x,
                target.y,
                target.width,
                target.height
        );

        // Indicator
        batch.setColor(Color.WHITE);
        batch.draw(
                Assets.whiteTexture,
                indicator.x,
                indicator.y,
                indicator.width,
                indicator.height
        );

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

        float score;
        // Measure from the middle of the indicator
        float currentPos = indicator.x + (indicator.width / 2);

        // Are we inside the falloff bounds?
        if (currentPos >= targetFalloff.x
                && currentPos <= targetFalloff.x + targetFalloff.width) {
            // Did they hit the target?
            if (currentPos >= target.x
                    && currentPos <= target.x + target.width) {
                // Dead on!
                score = 1;
            } else {
                // Determine where in the falloff they hit and score accordingly.
                float missDist;
                if (currentPos < target.x) {
                    // This is before the target area
                    missDist = target.x - currentPos;
                } else {
                    // They hit after the target area
                    missDist = currentPos - (target.x + target.width);
                }
                score = 1 - (missDist / (this.targetFalloffRange * bar.width));
            }
        } else {
            // didn't even hit the falloff area
            score = 0;
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

        setTargetFalloffRange(resourceManager.getWhipFalloffRange(resourceType));

        // Click?
        if (Gdx.input.justTouched()) {
            // TODO: test if they clicked the button
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
        indicator.x = indicatorMinX + (indicatorRangeWidth * indicatorPosition);
    }

}