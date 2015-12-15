package lando.systems.ld34.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.resources.ResourceInfo;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.uielements.ProgressBar;
import lando.systems.ld34.utils.Assets;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */
public class ManageResources extends Manage {

    ResourceManager resources;
    ProgressBar     availableGoldBar;
    ProgressBar     availableStoneBar;
    ProgressBar     availableFoodBar;
    ProgressBar     availableWoodBar;
    Rectangle       stoneButton;
    Rectangle       foodButton;
    Rectangle       woodButton;
    Texture         stoneTexture = Assets.plusIconOn;
    Texture         foodTexture = Assets.plusIconOn;
    Texture         woodTexture = Assets.plusIconOn;

    int goldAmount;
    int stoneAmount;
    int foodAmount;
    int woodAmount;
    int stoneAmountMax;
    int foodAmountMax;
    int woodAmountMax;


    public ManageResources(Rectangle bounds) {
        super(Type.RESOURCES, bounds);
        resources = LudumDare34.GameScreen.resourceManager;
        availableGoldBar  = new ProgressBar(Assets.nice2NinePatch);
        availableStoneBar = new ProgressBar(Assets.nice2NinePatch);
        availableFoodBar  = new ProgressBar(Assets.nice2NinePatch);
        availableWoodBar  = new ProgressBar(Assets.nice2NinePatch);

        final Color boundsColor = new Color(160f / 255f, 82f / 255f, 45f / 255f, 1f);
        availableGoldBar .boundsColor = boundsColor;
        availableStoneBar.boundsColor = boundsColor;
        availableFoodBar .boundsColor = boundsColor;
        availableWoodBar .boundsColor = boundsColor;

        availableGoldBar .fillColor.set(0f, 0.5f, 0f, 1f);
        availableStoneBar.fillColor.set(0f, 0.5f, 0f, 1f);
        availableFoodBar .fillColor.set(0f, 0.5f, 0f, 1f);
        availableWoodBar .fillColor.set(0f, 0.5f, 0f, 1f);

        availableGoldBar .bounds.set(leftMargin, barTop - 0f * (barHeight + lineSpacing), barWidth  + 2.0f *(buttonSize + widgetPadding), barHeight);
        availableStoneBar.bounds.set(leftMargin, barTop - 1f * (barHeight + lineSpacing), barWidth  + 0.75f *(buttonSize + widgetPadding), barHeight);
        availableFoodBar .bounds.set(leftMargin, barTop - 2f * (barHeight + lineSpacing), barWidth  + 0.75f *(buttonSize + widgetPadding), barHeight);
        availableWoodBar .bounds.set(leftMargin, barTop - 3f * (barHeight + lineSpacing), barWidth  + 0.75f *(buttonSize + widgetPadding), barHeight);

        stoneButton = new Rectangle(leftMargin + barWidth + 4.0f * widgetPadding, availableStoneBar.bounds.y, buttonSize, buttonSize);
        foodButton  = new Rectangle(leftMargin + barWidth + 4.0f * widgetPadding, availableFoodBar.bounds.y,  buttonSize, buttonSize);
        woodButton  = new Rectangle(leftMargin + barWidth + 4.0f * widgetPadding, availableWoodBar.bounds.y,  buttonSize, buttonSize);
    }

    @Override
    public void update(float delta) {
        final ResourceInfo goldResource  = resources.getResourceInfo(ResourceManager.Resources.GOLD);
        final ResourceInfo woodResource  = resources.getResourceInfo(ResourceManager.Resources.WOOD);
        final ResourceInfo stoneResource = resources.getResourceInfo(ResourceManager.Resources.STONE);
        final ResourceInfo foodResource  = resources.getResourceInfo(ResourceManager.Resources.FOOD);

        goldAmount     = MathUtils.floor(goldResource.amount);
        stoneAmount    = MathUtils.floor(stoneResource.amount);
        foodAmount     = MathUtils.floor(foodResource.amount);
        woodAmount     = MathUtils.floor(woodResource.amount);
        stoneAmountMax = MathUtils.floor(stoneResource.maxAmount);
        foodAmountMax  = MathUtils.floor(foodResource.maxAmount);
        woodAmountMax  = MathUtils.floor(woodResource.maxAmount);

        availableStoneBar .fillPercent.setValue(stoneAmount / (float) stoneAmountMax);
        availableFoodBar  .fillPercent.setValue(foodAmount  / (float) foodAmountMax);
        availableWoodBar  .fillPercent.setValue(woodAmount  / (float) woodAmountMax);

        final boolean canTradeStone = resources.canTrade(ResourceManager.Resources.STONE);
        final boolean canTradeFood  = resources.canTrade(ResourceManager.Resources.FOOD);
        final boolean canTradeWood  = resources.canTrade(ResourceManager.Resources.WOOD);

        stoneTexture = canTradeStone ? Assets.plusIconOn : Assets.plusIconOff;
        foodTexture  = canTradeFood  ? Assets.plusIconOn : Assets.plusIconOff;
        woodTexture  = canTradeWood  ? Assets.plusIconOn : Assets.plusIconOff;

        final float x = Gdx.input.getX();
        final float y = LudumDare34.GameScreen.uiCamera.viewportHeight - Gdx.input.getY();
        if      (stoneButton.contains(x,y)) LudumDare34.GameScreen.hudManager.showTooltip("Trade " + resources.getResourceInfo(ResourceManager.Resources.STONE).costToTrade() + " stone for 2 gold");
        else if (foodButton.contains(x,y))  LudumDare34.GameScreen.hudManager.showTooltip("Trade " + resources.getResourceInfo(ResourceManager.Resources.FOOD).costToTrade() + " food for 2 gold");
        else if (woodButton.contains(x,y))  LudumDare34.GameScreen.hudManager.showTooltip("Trade " + resources.getResourceInfo(ResourceManager.Resources.WOOD).costToTrade() + " wood for 2 gold");

        if (!Gdx.input.justTouched()) {
            return;
        }

        if      (stoneButton.contains(x,y) && canTradeStone) resources.tradeResource(ResourceManager.Resources.STONE);
        else if (foodButton.contains(x,y)  && canTradeFood)  resources.tradeResource(ResourceManager.Resources.FOOD);
        else if (woodButton.contains(x,y)  && canTradeWood)  resources.tradeResource(ResourceManager.Resources.WOOD);
    }

    @Override
    public void render(SpriteBatch batch) {
        drawHeading(batch, "Trade Management");
        drawAvailableRow(batch, "Current Gold:", availableGoldBar,  goldAmount,  -1);
        drawUpgrade1ColRow(batch, "Stone:", availableStoneBar, stoneTexture, stoneButton, stoneAmount, stoneAmountMax);
        drawUpgrade1ColRow(batch, "Food:", availableFoodBar, foodTexture, foodButton, foodAmount, foodAmountMax);
        drawUpgrade1ColRow(batch, "Wood:", availableWoodBar, woodTexture, woodButton, woodAmount, woodAmountMax);
    }
}
