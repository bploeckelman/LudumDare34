package lando.systems.ld34.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld34.Config;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.motivation.MotivationGame;
import lando.systems.ld34.resources.ResourceInfo;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.uielements.TutorialInfo;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.world.Area;
import lando.systems.ld34.world.AreaMgmt;
import lando.systems.ld34.world.Manage;

import javax.swing.plaf.LabelUI;

/**
 * Created by dsgraham on 12/14/15.
 */
public class TutorialManager {

    Array<TutorialInfo> screens;
    MutableFloat sceneAlpha;
    boolean acceptInput;

    public TutorialManager(){
        acceptInput = true;
        sceneAlpha = new MutableFloat(1);
        screens = new Array<TutorialInfo>();

        screens.add(new TutorialInfo("Welcome to [YELLOW] Pyramid Scheme[].\nThe Pharaoh has tasked you, his favorite overseer, to help grow his burial tomb.", Area.Type.MGMT, new Rectangle(0, 0, 0, 0)));

        screens.add(new TutorialInfo("We have 30 years until the eclipse that is foretelling his passing.", Area.Type.WOODS,
                new Rectangle(40, 280, 100, 100)));

        screens.add(new TutorialInfo("When it reaches the sun here the game is over.", Area.Type.PYRAMID,
                new Rectangle(495, 320, 85, 85)));

        //Management Tutorial Section
        TutorialInfo info = new TutorialInfo("Here is the Management Screen\nYou get to it by selecting this button", Area.Type.MGMT,
                expandRectangle(NavigationLayout.AreaButtons.get(Area.Type.MGMT).Bounds));
        info.mgmtScreen = Manage.Type.SLAVES;
        screens.add(info);

        info = new TutorialInfo("This is the Slave Management Screen", Area.Type.MGMT,
                expandRectangle(NavigationLayout.ResourceButtons.get(Manage.Type.SLAVES).Bounds));
        screens.add(info);

        info = new TutorialInfo("Here you can assign slaves to tasks", Area.Type.MGMT,
                expandRectangle(AreaMgmt.bounds));
        info.pos = new Vector2(150, 150);
        screens.add(info);

        info = new TutorialInfo("Here is the Upgrades Screen", Area.Type.MGMT,
                expandRectangle(NavigationLayout.ResourceButtons.get(Manage.Type.UPGRADES).Bounds));
        info.mgmtScreen = Manage.Type.UPGRADES;
        screens.add(info);

        info = new TutorialInfo("Here is the Trade Screen", Area.Type.MGMT,
                expandRectangle(NavigationLayout.ResourceButtons.get(Manage.Type.RESOURCES).Bounds));
        info.mgmtScreen = Manage.Type.RESOURCES;
        screens.add(info);

        info = new TutorialInfo("Here is the Pharaoh Screen", Area.Type.MGMT,
                expandRectangle(NavigationLayout.ResourceButtons.get(Manage.Type.PHAROAH).Bounds));
        info.mgmtScreen = Manage.Type.PHAROAH;
        screens.add(info);

        //Woods Tutorial Section
        screens.add(new TutorialInfo("Here is your Woods\nYou get here by selecting this button", Area.Type.WOODS,
                expandRectangle(NavigationLayout.AreaButtons.get(Area.Type.WOODS).Bounds)));

        info = new TutorialInfo("You can see how many slaves you have, how effecetive they are and how many resources you have stored up.", Area.Type.WOODS,
                expandRectangle(LudumDare34.GameScreen.resourceManager.getResourceInfo(ResourceManager.Resources.WOOD).bgPB.bounds));
        info.pos = new Vector2(Config.width/2, 150);
        screens.add(info);

        info = new TutorialInfo("Wood is used to upgrade each working area.", Area.Type.WOODS,
                expandRectangle(LudumDare34.GameScreen.resourceManager.getResourceInfo(ResourceManager.Resources.WOOD).bgPB.bounds));
        info.pos = new Vector2(Config.width/2, 150);
        screens.add(info);

        info = new TutorialInfo("When your slaves get lazy, you will have to 'motivate' them", Area.Type.WOODS,
                expandRectangle(MotivationGame.gameArea));
        info.pos = new Vector2(Config.width/2, 150);
        screens.add(info);

        //Food Tutorial Section
        screens.add(new TutorialInfo("Here is your Farms\nYou get here by selecting this button", Area.Type.FIELD,
                expandRectangle(NavigationLayout.AreaButtons.get(Area.Type.FIELD).Bounds)));

        info = new TutorialInfo("Food is used to create more slaves.  When you have enough stocked up a new slave will be born.", Area.Type.FIELD,
                expandRectangle(LudumDare34.GameScreen.resourceManager.getResourceInfo(ResourceManager.Resources.FOOD).bgPB.bounds));
        info.pos = new Vector2(Config.width/2, 150);
        screens.add(info);

        //Quarry Tutorial Section
        screens.add(new TutorialInfo("Here is your Quarry\nYou get here by selecting this button.", Area.Type.QUARRY,
                expandRectangle(NavigationLayout.AreaButtons.get(Area.Type.QUARRY).Bounds)));

        info = new TutorialInfo("Stones are converted to building blocks for the pyramid.", Area.Type.QUARRY,
                expandRectangle(LudumDare34.GameScreen.resourceManager.getResourceInfo(ResourceManager.Resources.STONE).bgPB.bounds));
        info.pos = new Vector2(Config.width/2, 150);
        screens.add(info);

        info = new TutorialInfo("Speaking of the Pyramid", Area.Type.QUARRY, new Rectangle());
        screens.add(info);

        //Pyramid Tutorial Section
        screens.add(new TutorialInfo("The Monumental Monument.\nYou get here by selecting this button.", Area.Type.PYRAMID,
                expandRectangle(new Rectangle(Config.width - 50, Config.height/2 - 190/2, 50, 190))));


        info = new TutorialInfo("Good Luck out there. Grow the biggest pyramid", Area.Type.MGMT,
                expandRectangle(NavigationLayout.AreaButtons.get(Area.Type.MGMT).Bounds));
        info.mgmtScreen = Manage.Type.SLAVES;
        screens.add(info);
    }

    public boolean isDisplayed(){
        return screens.size > 0;
    }

    public void update(float dt){
        TutorialInfo currentTutorial = screens.get(0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            screens.clear();
            return;
        }
        if (acceptInput && Gdx.input.justTouched())
        {
            acceptInput = false;
            if (screens.size > 1) {
                final TutorialInfo info = screens.get(1);
                if (info.area != currentTutorial.area) {
                    LudumDare34.GameScreen.TransitionToArea(info.area);
                    Timeline.createSequence()
                            .push(Tween.to(sceneAlpha, 1, GameScreen.SCENEFADE)
                                    .target(0))
                            .push(Tween.call(new TweenCallback() {
                                @Override
                                public void onEvent(int i, BaseTween<?> baseTween) {
                                    screens.removeIndex(0);
                                }
                            }))
                            .push(Tween.to(sceneAlpha, 1, GameScreen.BACKGROUNDTRANSITION)
                                    .target(0))
                            .push(Tween.to(sceneAlpha, 1, GameScreen.SCENEFADE)
                                    .delay(.3f)
                                    .target(1))
                            .push(Tween.call(new TweenCallback() {
                                @Override
                                public void onEvent(int i, BaseTween<?> baseTween) {
                                    acceptInput = true;
                                    if (info.mgmtScreen != null) {
                                        LudumDare34.GameScreen.ShowManagementScreen(info.mgmtScreen);
                                    }
                                }
                            }))
                            .start(Assets.tween);
                } else {
                    screens.removeIndex(0);
                    acceptInput = true;
                    if (info.mgmtScreen != null) {
                        LudumDare34.GameScreen.ShowManagementScreen(info.mgmtScreen);
                    }
                }
            } else {
                screens.removeIndex(0);
            }
        }

    }

    public Rectangle expandRectangle(Rectangle rect){
        return new Rectangle(rect.x - 10, rect.y - 10, rect.width +20, rect.height + 20);
    }

    public void render(SpriteBatch batch){
        if (screens.size <= 0) return;
        TutorialInfo info = screens.get(0);
        drawHightlight(batch, info);

        String coloredReplace = info.text;
        Color c = new Color(1,1,1,sceneAlpha.floatValue());
        int intAlpha = (int)(sceneAlpha.floatValue() * 255);
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(intAlpha));
        if (sb.length() < 2) sb.insert(0, '0'); // pad with leading zero if needed
        String hex = sb.toString();

        Assets.glyphLayout.setText(Assets.font, coloredReplace.replace("xALPHAx", hex), c, info.wrapWidth, Align.center, true);
        float txtH = Assets.glyphLayout.height;
        float boxWidth = (info.wrapWidth + 20);
        Rectangle bounds = new Rectangle(info.pos.x - boxWidth /2 - 10, info.pos.y - txtH /2 - 10, boxWidth, txtH + 20);

        batch.setColor(62f / 255, 42f / 255, 0, sceneAlpha.floatValue());
        batch.draw(Assets.whiteTexture, bounds.x, bounds.y, bounds.width, bounds.height);

        batch.setColor(new Color(1, 1, 1, sceneAlpha.floatValue()));
        Assets.niceNinePatch.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);

        Assets.font.draw(batch, Assets.glyphLayout, bounds.x + 10, bounds.y + bounds.height - 10);



    }



    private void drawHightlight(SpriteBatch batch, TutorialInfo info){
        Rectangle light = info.hightlightBounds;
        batch.setColor(0,0,0,.75f * sceneAlpha.floatValue());
        batch.draw(Assets.whiteTexture, 0, 0, light.x, light.y);
        batch.draw(Assets.whiteTexture, light.x, 0, light.width, light.y);
        batch.draw(Assets.whiteTexture, light.x + light.width, 0, Config.width, light.y);

        batch.draw(Assets.whiteTexture, 0, light.y, light.x, light.height);
        batch.draw(Assets.whiteTexture, light.x + light.width, light.y, Config.width, light.height);

        batch.draw(Assets.whiteTexture, 0, light.y + light.height, light.x, Config.height);
        batch.draw(Assets.whiteTexture, light.x, light.y + light.height,light.width, Config.height);
        batch.draw(Assets.whiteTexture, light.x + light.width, light.y + light.height, Config.width, Config.height);
        batch.setColor(Color.WHITE);
    }
}
