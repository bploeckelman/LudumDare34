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

        screens.add(new TutorialInfo("Welcome to Pyramid Scheme.\nThe Pharaoh has tasked you, his favorite overseer, to help grow his burial tomb.", Area.Type.MGMT, new Rectangle(0, 0, 0, 0)));

        screens.add(new TutorialInfo("We have 30 years until the eclipse that is foretelling his passing.", Area.Type.WOODS,
                new Rectangle(40, 280, 100, 100)));

        screens.add(new TutorialInfo("When it reaches the sun here the game is over.", Area.Type.PYRAMID,
                new Rectangle(495, 320, 85, 85)));

        //Management Tutorial Section
        TutorialInfo slaveSelectionTutorial = new TutorialInfo("Here is the Management Screen\nYou get to it by selecting this button", Area.Type.MGMT,
                expandRectangle(NavigationLayout.AreaButtons.get(Area.Type.MGMT).Bounds));
        slaveSelectionTutorial.mgmtScreen = Manage.Type.SLAVES;
        screens.add(slaveSelectionTutorial);

        TutorialInfo slaveTutorial = new TutorialInfo("This is the Slave Management Screen", Area.Type.MGMT,
                expandRectangle(NavigationLayout.ResourceButtons.get(Manage.Type.SLAVES).Bounds));
        screens.add(slaveTutorial);

        slaveTutorial = new TutorialInfo("Here you can assign slaves to tasks", Area.Type.MGMT,
                expandRectangle(AreaMgmt.bounds));
        slaveTutorial.pos = new Vector2(150, 150);
        screens.add(slaveTutorial);

        TutorialInfo pharoahTutorial = new TutorialInfo("Here is the Pharaoh Screen", Area.Type.MGMT,
                expandRectangle(NavigationLayout.ResourceButtons.get(Manage.Type.PHAROAH).Bounds));
        pharoahTutorial.mgmtScreen = Manage.Type.PHAROAH;
        screens.add(pharoahTutorial);

        //Quarry Tutorial Section
        screens.add(new TutorialInfo("Here is your Quarry\nYou get here by selecting this button", Area.Type.QUARRY,
                expandRectangle(NavigationLayout.AreaButtons.get(Area.Type.QUARRY).Bounds)));
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

        Assets.glyphLayout.setText(Assets.font, info.text, new Color(1,1,1,sceneAlpha.floatValue()), info.wrapWidth, Align.center, true);
        float txtH = Assets.glyphLayout.height;
        float boxWidth = (info.wrapWidth + 20);
        Rectangle bounds = new Rectangle(info.pos.x - boxWidth /2 - 10, info.pos.y - txtH /2 - 10, boxWidth, txtH + 20);

        batch.setColor(62f / 255, 42f / 255, 0, sceneAlpha.floatValue());
        batch.draw(Assets.whiteTexture, bounds.x, bounds.y, bounds.width, bounds.height);

        batch.setColor(new Color(1,1,1,sceneAlpha.floatValue()));
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
