package lando.systems.ld34.efficiencygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.utils.Assets;

public class MotivationBar {

    private final static float BAR_WIDTH = 300f; // pixels
    private final static float BAR_HEIGHT = 60; // pixels
    private final static float BAR_X = 20f; // pixels
    private final static float BAR_Y = 20f; // pixels
    private final static float INDICATOR_SPEED = 1.2f; // % of the bar per second (out of 1)
    private final static float INDICATOR_WIDTH = 2f; // pixels

    private String resourceType; // TODO switch to Doug's enum for resource type
    private float targetRange; // The size of the target, out of 1
    private float targetFalloff;  // The falloff area on either side of the target, out of 1

    private Rectangle barArea;

    private Rectangle targetArea;
    private Rectangle targetFalloffArea;
    private float targetMinX;
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
     * @param targetRange float
     * @param targetFalloff float
     */
    public MotivationBar(String resourceType, float targetRange, float targetFalloff) {

        this.resourceType = resourceType;

//        this.targetFalloff = targetFalloff;
        if (this.targetRange + (this.targetFalloff * 2) > 1) {
            Gdx.app.log(this.getClass().toString(), "target range + the target falloffs exceeds 1");
        }

        this.barArea = new Rectangle(MotivationBar.BAR_X, MotivationBar.BAR_Y, MotivationBar.BAR_WIDTH, MotivationBar.BAR_HEIGHT);
        this.indicator = new Rectangle(0, MotivationBar.BAR_Y, MotivationBar.INDICATOR_WIDTH, MotivationBar.BAR_HEIGHT);
        this.indicatorMinX = MotivationBar.BAR_X;
        this.indicatorRangeWidth = MotivationBar.BAR_WIDTH - MotivationBar.INDICATOR_WIDTH;

        this.targetArea = new Rectangle(0, MotivationBar.BAR_Y, 0, MotivationBar.BAR_HEIGHT);
        this.targetFalloffArea = new Rectangle(0, MotivationBar.BAR_Y, 0, MotivationBar.BAR_HEIGHT);
        this.targetMinX = MotivationBar.BAR_X;
        this.setTargetRange(targetRange);
        this.setTargetFalloff(targetFalloff);

        this.reset();
        this.start();

    }


    // Getter/Setters --------------------------------------------------------------------------------------------------

    public void setTargetRange(float targetRange) {
        this.targetRange = targetRange;
        this.targetArea.width = MotivationBar.BAR_WIDTH * this.targetRange;
        this.updateTargetRangeWidth();
        this.reset();
    }

    public void setTargetFalloff(float targetFalloff) {
        this.targetFalloff = targetFalloff;
        this.targetFalloffArea.width = MotivationBar.BAR_WIDTH * ((this.targetFalloff * 2) + this.targetRange);
        this.updateTargetRangeWidth();
        this.reset();
    }


    // -----------------------------------------------------------------------------------------------------------------

    public void start() {
        this.isActive = true;
    }

    public void reset() {
        this.indicatorPosition = 0; // All the way to the left
        this.indicatorDirection = 1; // Moving right
        // todo: select and place target
        // Position the target
        this.targetFalloffArea.x = MathUtils.random() * this.targetRangeWidth;
        this.targetArea.x = this.targetFalloffArea.x + (this.targetFalloff * MotivationBar.BAR_WIDTH);
    }

    public void stop() {
        this.isActive = false;
    }

    public void updateTargetRangeWidth() {
        this.targetRangeWidth = MotivationBar.BAR_WIDTH - this.targetFalloffArea.width;
    }


    public void render(SpriteBatch batch){

        Color c = batch.getColor();

        batch.setColor(Color.BLUE);
        batch.draw(Assets.onePixelWhite, barArea.x, barArea.y, barArea.width, barArea.height);

        // Target
        batch.setColor(Color.ORANGE);
        batch.draw(
                Assets.onePixelWhite,
                targetFalloffArea.x,
                targetFalloffArea.y,
                targetFalloffArea.width,
                targetFalloffArea.height
        );
        batch.setColor(Color.RED);
        batch.draw(
                Assets.onePixelWhite,
                targetArea.x,
                targetArea.y,
                targetArea.width,
                targetArea.height
        );

        // Indicator
        batch.setColor(Color.WHITE);
        batch.draw(
                Assets.onePixelWhite,
                indicator.x,
                indicator.y,
                indicator.width,
                indicator.height
        );

        batch.setColor(c);

    }

    public void reportCurrentMotivationScore() {
        float score;
        // Measure from the middle of the indicator
        float currentPos = indicator.x + (indicator.width / 2);

//        Gdx.app.log("currentPos:", ""+currentPos);
//        Gdx.app.log("targetFalloffArea.x:", ""+targetFalloffArea.x);
//        Gdx.app.log("targetFalloffArea.x + targetFalloffArea.width:", ""+(targetFalloffArea.x + targetFalloffArea.width));
//        Gdx.app.log("targetArea.x:", ""+targetArea.x);
//        Gdx.app.log("targetArea.x + targetArea.width:", ""+(targetArea.x + targetArea.width));

        // Are we inside the falloff bounds?
        if (currentPos >= targetFalloffArea.x
                && currentPos <= targetFalloffArea.x + targetFalloffArea.width) {
            // Did they hit the target?
            if (currentPos >= targetArea.x
                    && currentPos <= targetArea.x + targetArea.width) {
                // Dead on!
                score = 1;
            } else {
                // Determine where in the falloff they hit and score accordingly.
                float missDist;
                if (currentPos < targetArea.x) {
                    // This is before the target area
                    missDist = targetArea.x - currentPos;
                } else {
                    // They hit after the target area
                    missDist = currentPos - (targetArea.x + targetArea.width);
                }
                score = 1 - (missDist / (this.targetFalloff * MotivationBar.BAR_WIDTH));
//                Gdx.app.log("missDist:", ""+missDist);
            }
        } else {
            // didn't even hit the falloff area
            score = 0;
        }

        Gdx.app.log("MotivationScore:", ""+score);
    }

    public void update(float delta){

        // Click?
        if (Gdx.input.justTouched()) {
            // TODO: test if they clicked the button
            this.reportCurrentMotivationScore();
            this.reset();
        }

        // Indicator
        float indicatorDY = (MotivationBar.INDICATOR_SPEED * delta) * indicatorDirection;
        this.indicatorPosition += indicatorDY;
        // check bounds
        if (this.indicatorPosition >= 1) {
            this.indicatorDirection *= -1;
            this.indicatorPosition -= (this.indicatorPosition - 1);
        } else if (this.indicatorPosition <= 0) {
            this.indicatorDirection *= -1;
            this.indicatorPosition += (this.indicatorPosition * -1);
            this.reset();
        }
        indicator.x = indicatorMinX + (indicatorRangeWidth * indicatorPosition);
    }



}