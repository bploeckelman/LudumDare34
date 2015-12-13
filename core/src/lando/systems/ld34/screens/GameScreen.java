package lando.systems.ld34.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectMap;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.uielements.AreaButton;
import lando.systems.ld34.uielements.ManagementButton;
import lando.systems.ld34.uielements.PyramidButton;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.world.*;
import lando.systems.ld34.world.pyramid.Pyramid;

/**
 * Brian Ploeckelman created on 12/9/2015.
 */
public class GameScreen extends AbstractScreen {

    final SpriteBatch      batch;
    final NavigationLayout layout;

    // java# (tm)
    public ResourceManager ResourceManager;

    ObjectMap<Area.Type, Area> areaMap;
    public Area currentArea;
    Background background;

    public HUDNotificationManager hudNotificationManager;

    public Pyramid Pyramid;

    public GameScreen(LudumDare34 game) {
        super(game);
        hudNotificationManager = new HUDNotificationManager();
        LudumDare34.GameScreen = this;

        float w = uiCamera.viewportWidth / 2f;
        float h = uiCamera.viewportHeight - uiCamera.viewportHeight / 6f;
        float x = uiCamera.viewportWidth / 2f - w / 2f;
        float y = uiCamera.viewportHeight / 2f - h / 2f;
        Pyramid = new Pyramid(new Rectangle(x, y, w, h));

        batch = Assets.batch;

        ResourceManager = new ResourceManager();

        background = new Background();
        areaMap = new ObjectMap<Area.Type, Area>();
        areaMap.put(Area.Type.MGMT, new AreaMgmt(this));
        areaMap.put(Area.Type.PYRAMID, new AreaPyramid(this));
        areaMap.put(Area.Type.QUARRY, new AreaQuarry(this));
        areaMap.put(Area.Type.FIELD, new AreaField(this));
        areaMap.put(Area.Type.WOODS, new AreaWoods(this));

        currentArea = areaMap.get(Area.Type.MGMT);

        layout = new NavigationLayout(this);
        SetupNavigation(layout);

        TransitionToArea(AreaButton.SelectedButton.AreaLocation);
    }

    public void TransitionToArea(Area.Type area) {
        final Area nextArea = areaMap.get(area);
        Tween.to(background.xOffset, 1, 1f)
                .target(nextArea.worldX * (512/5f))
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        currentArea = nextArea;
                    }
                })
                .start(Assets.tween);

    }

    public void ShowManagementScreen(Manage.Type skillScreen) {
        if (currentArea.type != Area.Type.MGMT) {
            return;
        }
        ((AreaMgmt) currentArea).setCurrentManageType(skillScreen);
    }

    private void SetupNavigation(NavigationLayout navLayout) {
        AreaButton managementAreaButton = new AreaButton("Management", Assets.managementIcon, Area.Type.MGMT);
        AreaButton.SelectedButton = managementAreaButton;

        navLayout.add(managementAreaButton);
        navLayout.add(new AreaButton("Quarry", Assets.quarryIcon, Area.Type.QUARRY));
        navLayout.add(new AreaButton("Field", Assets.fieldIcon, Area.Type.FIELD));
        navLayout.add(new AreaButton("Woods", Assets.woodsIcon, Area.Type.WOODS));

        // layout added buttons first before adding pyramid button - hacky but fuck it
        float height = uiCamera.viewportHeight;
        navLayout.layoutAreaButtons(new Rectangle(0, background.SandHeight, 75, height - background.SandHeight));

        Rectangle pyramidBounds = new Rectangle(uiCamera.viewportWidth - 75,
                background.SandHeight, 75, height - background.SandHeight);
        navLayout.add(new PyramidButton(pyramidBounds));

        ManagementButton skillsManagementButton = new ManagementButton("Workers", Assets.workersIcon, Manage.Type.WORKERS);
        ManagementButton.SelectedButton = skillsManagementButton;
        ShowManagementScreen(skillsManagementButton.Screen);

        navLayout.add(skillsManagementButton);
        navLayout.add(new ManagementButton("Slaves", Assets.slavesIcon, Manage.Type.SLAVES));
        navLayout.add(new ManagementButton("Pharoah", Assets.pharoahIcon, Manage.Type.PHAROAH));
        navLayout.add(new ManagementButton("Upgrades", Assets.upgradesIcon, Manage.Type.UPGRADES));
        navLayout.add(new ManagementButton("Resources", Assets.resourcesIcon, Manage.Type.RESOURCES));

        navLayout.layoutManagement(new Rectangle(0, 0, uiCamera.viewportWidth, background.SandHeight));
    }

    public void addNotification(String msg){
        hudNotificationManager.addNotification(msg);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        layout.update();
        ResourceManager.update(delta);
        Pyramid.update(delta);
        currentArea.update(delta);
        hudNotificationManager.update(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        background.render(batch);
        currentArea.render(batch);

        hudNotificationManager.render(batch);
        batch.end();

        layout.render(batch, uiCamera);
    }

}
