package lando.systems.ld34.motivation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld34.Config;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.resources.ResourceInfo;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.screens.GameScreen;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.utils.ParticleManager;
import lando.systems.ld34.utils.SoundManager;

public class MotivationGame {

    // Positioning info for the game's UI
    // NOTE: All other X/Y positions within this class are relative to these positions.
    private final static float GAME_HEIGHT = 46f;
    private final static float GAME_WIDTH = 340f;
    private final static float GAME_X = Config.width/2 - 170;
    private final static float GAME_Y = 300f;

    private final static float BUTTON_HEIGHT = 34f;
    private final static float BUTTON_WIDTH = 96f;
    private final static float BUTTON_X = 238f;
    private final static float BUTTON_Y = 6f;
    private final static String BUTTON_TEXT = "Motivate!";

    private final static float BAR_HEIGHT = 34f; // pixels
    private final static float BAR_WIDTH = 226f; // pixels
    private final static float BAR_X = 6f; // pixels
    private final static float BAR_Y = 6f; // pixels

    private final static float INDICATOR_SPEED = 1.3f; // % of the bar per second (out of 1)

    private final static float COOLDOWN_TIMER = 0.5f;

    private final static String DISABLED_NO_SLAVES_MESSAGE = "No slaves to motivate.";
    private final static String DISABLED_EFFICIENT = "No motivation required at this time.";

    private ResourceManager resourceManager;
    private ResourceManager.Resources resourceType;

    private float targetFalloffRangeU;  // The falloff area on either side of the target, out of 1
    private float targetRangeU; // The size of the target, out of 1
    private float targetU; // Where's the target?

    private Rectangle bg;
    private Rectangle bar;
    private Rectangle button;
    private Rectangle whipIcon;
    private Rectangle whipBorder;

    private boolean buttonIsHovered = false;

    private float indicatorPosition; // Where along the bar to place the indicator, from 0 to 1
    private float indicatorDirection;

    private boolean isOnCooldown = false;
    private float cooldownTimer = 0;
    private float currentScore = 0;

    private boolean isDisabledNoSlaves = true;
    private boolean isDisabledHighEfficiency = false;

    private ParticleManager particleManager;


    // Construct -------------------------------------------------------------------------------------------------------

    /**
     * todo: take in the game manager so we can report motivations
     * @param resourceType String
     */
    public MotivationGame(ResourceManager resourceManager, ResourceManager.Resources resourceType) {
        particleManager = new ParticleManager();
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

        ResourceInfo info = this.resourceManager.getResourceInfo(resourceType);
        whipBorder = new Rectangle(info.bgPB.bounds.x + info.bgPB.bounds.width + 5, info.bgPB.bounds.y, bg.width - info.bgPB.bounds.width - 5, info.bgPB.bounds.height);
        float whipSize = Math.min(whipBorder.width - 10, whipBorder.height - 10);
        whipIcon = new Rectangle(whipBorder.x + whipBorder.width /2 - whipSize /2, whipBorder.y + whipBorder.height /2 - whipSize /2 ,whipSize, whipSize);

        this.targetRangeU = this.resourceManager.getWhipTargetRange(resourceType);
        this.targetFalloffRangeU = this.resourceManager.getWhipFalloffRange(resourceType);

        this.reset();

    }


    // Getter/Setters --------------------------------------------------------------------------------------------------


    // -----------------------------------------------------------------------------------------------------------------

    private float getCurrentMotivationScore() {
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

        return score;
    }

    private void initiateCooldown() {
        isOnCooldown = true;
        cooldownTimer = COOLDOWN_TIMER;
    }

    private void reset() {
        this.indicatorPosition = 0; // All the way to the left
        this.indicatorDirection = 1; // Moving right
        // Position the target
        targetU = MathUtils.random(0, 1 - targetRangeU);    // NOTE: Falloff might be cut off; that's OK
    }

    private void reportMotivationScore(float score) {
        float shakeDuration = 0.5f;
        resourceManager.addEfficiency(resourceType, score);
        if (MathUtils.random() > score){
            if (resourceManager.removeSlaves(resourceType, 1) > 0) {
                particleManager.addBlood(new Vector2(bar.x + bar.width * indicatorPosition, bg.y + bg.height/2), 100);
                LudumDare34.GameScreen.hudManager.addNotification("You Killed a Slave");
                SoundManager.playScream();
                GameScreen.stats.slavesKilledMotivating++;
                shakeDuration = 1f;
            }
        }
        LudumDare34.GameScreen.shaker.shake(shakeDuration);
    }

    /**
     *
     * @return boolean Whether or not the update function should continue.
     */
    private boolean onButtonPress() {
        GameScreen.stats.motivations++;
        particleManager.addBlood(new Vector2(whipIcon.x + whipIcon.width/2, whipIcon.y + whipIcon.height/2), 100);
        SoundManager.playWhip();
        currentScore = getCurrentMotivationScore();
        reportMotivationScore(currentScore);
        initiateCooldown();
        return false;
    }


    // Per Frame Functions ---------------------------------------------------------------------------------------------

    public void render(SpriteBatch batch){

        Color c = batch.getColor();



        // Game BG
        Assets.nice2NinePatch.draw(batch, bg.x, bg.y, bg.width, bg.height);



        // Shader bar
        if (isDisabledNoSlaves) {
            Assets.glyphLayout.setText(Assets.font, DISABLED_NO_SLAVES_MESSAGE);
            Assets.font.setColor(Color.WHITE);
            Assets.font.draw(
                    batch,
                    DISABLED_NO_SLAVES_MESSAGE,
                    bar.x + (bar.width / 2) - (Assets.glyphLayout.width / 2) + 2,
                    bar.y + (bar.height / 2) + (Assets.glyphLayout.height / 2)
                            );
        } else if (isDisabledHighEfficiency) {
            Assets.glyphLayout.setText(Assets.font, DISABLED_EFFICIENT);
            Assets.font.setColor(Color.WHITE);
            Assets.font.draw(
                    batch,
                    DISABLED_EFFICIENT,
                    bar.x + (bar.width / 2) - (Assets.glyphLayout.width / 2) + 2,
                    bar.y + (bar.height / 2) + (Assets.glyphLayout.height / 2)
            );
        } else {
            batch.setShader(Assets.motivationBarShader);
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Assets.motivationBarShader.setUniformf("u_target_x", targetU);
            Assets.motivationBarShader.setUniformf("u_target_width", targetRangeU);
            Assets.motivationBarShader.setUniformf("u_target_falloff_width", targetFalloffRangeU);
            Assets.motivationBarShader.setUniformf("u_indicator_x", indicatorPosition);
            Assets.motivationBarShader.setUniformf("u_indicator_dir", indicatorDirection);
            Assets.motivationBarShader.setUniformi("u_is_on_cooldown", isOnCooldown ? 1 : 0);
            Assets.motivationBarShader.setUniformf("u_cooldown_percent_remaining", cooldownTimer / COOLDOWN_TIMER);
            Assets.motivationBarShader.setUniformf("hit_score", currentScore);
            batch.draw(Assets.testTexture, bar.x, bar.y, bar.width, bar.height);
            batch.setShader(null);
        }

        // Button
        if (buttonIsHovered && !isDisabledHighEfficiency && !isDisabledNoSlaves) {
            Assets.nice2NinePatch.draw(batch, button.x, button.y, button.width, button.height);
        } else {
            if (isDisabledNoSlaves || isDisabledHighEfficiency) {
                batch.setColor(0.5f, 0.5f, 0.5f, 1f);
            } else {
                batch.setColor(1f, 1f, 1f, 1f);
            }
            Assets.niceNinePatch.draw(batch, button.x, button.y, button.width, button.height);
        }
        batch.setColor(1f, 1f, 1f, 1f);
        Assets.glyphLayout.setText(Assets.font, BUTTON_TEXT);
        Color buttonFontColor = (isDisabledNoSlaves || isDisabledHighEfficiency) ? Color.GRAY : Color.WHITE;
        Assets.font.setColor(buttonFontColor);
        Assets.font.draw(
                batch,
                BUTTON_TEXT,
                button.x + (button.width / 2) - (Assets.glyphLayout.width / 2) + 2,
                button.y + (button.height / 2) + (Assets.glyphLayout.height / 2)
        );

        // If we're on cooldown, let's indicate how they did
        if (isOnCooldown) {
            String scoreMessage = currentScore == 1f ? "Perfect!" : MathUtils.round(currentScore*100)+"%";
            Assets.glyphLayout.setText(Assets.font, scoreMessage);
            Assets.font.setColor(Color.WHITE);
            Assets.font.draw(
                    batch,
                    scoreMessage,
                    bar.x + (bar.width * indicatorPosition) - (Assets.glyphLayout.width / 2),
                    bar.y + bar.height + Assets.glyphLayout.height +
                            (bar.height * (1 - (cooldownTimer / COOLDOWN_TIMER)))
            );
        }

        particleManager.render(batch);

        Assets.nice2NinePatch.draw(batch, whipBorder.x, whipBorder.y, whipBorder.width, whipBorder.height);

        batch.draw(Assets.whippingTexture, whipIcon.x, whipIcon.y, whipIcon.width, whipIcon.height);

        Assets.font.setColor(Color.WHITE);
        batch.setColor(c);

    }

    public void update(float delta){
        particleManager.update(delta);

        // Cooldown
        if (isOnCooldown) {
            cooldownTimer -= delta;
            if (cooldownTimer <= 0f) {
                isOnCooldown = false;
                reset();
            } else {
                return;
            }
        }

        // Should we disable the interface due to lack of slaves?
        isDisabledNoSlaves = (resourceManager.getSlaveAmount(resourceType) == 0);
        isDisabledHighEfficiency = resourceManager.getEfficiency(resourceType) > resourceManager.getResourceInfo(resourceType).maxEfficiency * .9f;

        if (!isDisabledNoSlaves && !isDisabledHighEfficiency) {

            // Is the button hovered?
            float mousePosX = Gdx.input.getX();
            float mousePosY = Config.height - Gdx.input.getY();
            buttonIsHovered = button.contains(mousePosX, mousePosY);

            // Update the target range and falloff
            float whipTargetRange = resourceManager.getWhipTargetRange(resourceType);
            if (targetRangeU != whipTargetRange) {
                targetRangeU = whipTargetRange;
                reset();
            }
            targetFalloffRangeU = resourceManager.getWhipFalloffRange(resourceType);

            // Check for button press
            if (Gdx.input.justTouched()) {
                int touchX = Gdx.input.getX();
                int touchY = (Config.height - Gdx.input.getY());
                if (button.contains(touchX, touchY)) {
                    if (!onButtonPress()) {
                        return;
                    }
                }
            }

            // Update the indicator
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

}