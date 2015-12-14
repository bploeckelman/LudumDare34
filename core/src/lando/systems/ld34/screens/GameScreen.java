package lando.systems.ld34.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectMap;
import lando.systems.ld34.Config;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.uielements.AreaButton;
import lando.systems.ld34.uielements.ManagementButton;
import lando.systems.ld34.uielements.PyramidButton;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.utils.camera.Shake;
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

    public HUDManager hudManager;

    public Pyramid Pyramid;
    private final FrameBuffer currentFBO;
    private MutableFloat sceneAlpha;
    private final static float SCENEFADE = .3f;
    private final static float BACKGROUNDTRANSITION = .5f;
    public final static float gameLength = 600f;
    public float gameTimer;
    public Shake shaker;

    public GameScreen(LudumDare34 game) {
        super(game);
        gameTimer = 0;
        Gdx.gl.glClearColor(0, 0, 0, 0);
        sceneAlpha = new MutableFloat(1);
        currentFBO = new FrameBuffer(Pixmap.Format.RGBA8888, Config.width, Config.height, false);

        hudManager = new HUDManager();
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
        sceneAlpha.setValue(1);
        TransitionToArea(AreaButton.SelectedButton.AreaLocation);

        shaker = new Shake(120, 2.0f);
    }

    public void TransitionToArea(Area.Type area) {
        NavigationLayout.CurrentArea = area;
        final Area nextArea = areaMap.get(area);
        if (currentArea == nextArea) return;
        Timeline.createSequence()
                .push(Tween.to(sceneAlpha, 1, SCENEFADE)
                      .target(0))
                .push(Tween.to(background.xOffset, 1, BACKGROUNDTRANSITION)
                        .target(nextArea.worldX * (Assets.background.getWidth()/8f)))
                .push(Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        currentArea = nextArea;
                    }
                }))
                .push(Tween.to(sceneAlpha, 1, SCENEFADE)
                        .target(1))
                .start(Assets.tween);

    }

    public void ShowManagementScreen(Manage.Type skillScreen) {
        if (currentArea.type != Area.Type.MGMT) {
            return;
        }
        ((AreaMgmt) currentArea).setCurrentManageType(skillScreen);
    }

    private void SetupNavigation(NavigationLayout navLayout) {
        AreaButton managementAreaButton = new AreaButton("Management", Assets.managementIcon, Area.Type.MGMT, "Slave Mangement");
        AreaButton.SelectedButton = managementAreaButton;

        navLayout.add(managementAreaButton);
        navLayout.add(new AreaButton("Quarry", Assets.quarryIcon, Area.Type.QUARRY, "Quarry"));
        navLayout.add(new AreaButton("Field", Assets.fieldIcon, Area.Type.FIELD, "Farmlands"));
        navLayout.add(new AreaButton("Woods", Assets.woodsIcon, Area.Type.WOODS, "Woods"));

        navLayout.AreaButtons.get(Area.Type.MGMT).efficiencyLevel = 40;
        navLayout.AreaButtons.get(Area.Type.QUARRY).efficiencyLevel = 26;
        navLayout.AreaButtons.get(Area.Type.FIELD).efficiencyLevel = 12;
        navLayout.AreaButtons.get(Area.Type.WOODS).efficiencyLevel = 0;

        // layout added buttons first before adding pyramid button - hacky but fuck it
        float height = uiCamera.viewportHeight;

        float yOffset = background.SandHeight;
        float boundsHeight = height - background.SandHeight - 40;

        navLayout.layoutAreaButtons(new Rectangle(0, yOffset, 50, boundsHeight));

        Rectangle pyramidBounds = new Rectangle(uiCamera.viewportWidth - 50, yOffset, 50, boundsHeight);
        navLayout.add(new PyramidButton(pyramidBounds));

        ManagementButton skillsManagementButton = new ManagementButton("Workers", Manage.Type.WORKERS, "Manage Skilled Workers");
        ManagementButton slaveManagementButton = new ManagementButton("Slaves", Manage.Type.SLAVES, "Manage Slave Labor");
        ManagementButton.SelectedButton = slaveManagementButton;
        ShowManagementScreen(slaveManagementButton.Screen);

        navLayout.add(slaveManagementButton);
        navLayout.add(new ManagementButton("Upgrades", Manage.Type.UPGRADES, "Upgrade your Buildings"));
        navLayout.add(new ManagementButton("Trade", Manage.Type.RESOURCES, "Trade Resources for Gold"));
        navLayout.add(skillsManagementButton);
        navLayout.add(new ManagementButton("Pharoah", Manage.Type.PHAROAH, "Keep the Pharoah Happy"));


        navLayout.layoutManagement(new Rectangle(0, (uiCamera.viewportHeight - 40), uiCamera.viewportWidth, 40));
    }

    public void addNotification(String msg){
        hudManager.addNotification(msg);
    }

    public boolean gameOver(){
        return gameTimer > gameLength;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!gameOver()) {
            gameTimer += delta;
            if (gameOver()){
                // TODO display game over and stats
                TransitionToArea(Area.Type.PYRAMID);
            }
        }
        if (!gameOver()) {
            ResourceManager.update(delta);
            layout.update(delta);
        }
        currentArea.update(delta);
        Pyramid.update(delta);
        hudManager.update(delta);
        shaker.update(delta, camera, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
    }

    @Override
    public void render(float delta) {
        update(delta);


        currentFBO.begin();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        currentArea.render(batch);
        batch.end();
        currentFBO.end();

        Texture currentTexture = currentFBO.getColorBufferTexture();
        currentTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        batch.setColor(1, 1, 1, sceneAlpha.floatValue());
        batch.draw(currentTexture, 0, currentFBO.getHeight(), currentFBO.getWidth(), -currentFBO.getHeight());
        batch.setColor(1, 1, 1, 1);
        layout.render(batch, uiCamera);

        hudManager.render(batch);
        batch.end();

    }

}
