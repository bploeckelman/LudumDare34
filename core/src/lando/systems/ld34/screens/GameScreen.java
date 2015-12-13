package lando.systems.ld34.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectMap;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.uielements.AreaButton;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.world.*;

/**
 * Brian Ploeckelman created on 12/9/2015.
 */
public class GameScreen extends AbstractScreen {

    final SpriteBatch      batch;
    final NavigationLayout layout;

    ObjectMap<Area.Type, Area> areaMap;
    Area currentArea;
    ResourceManager resourceManager;
    Background background;

    public GameScreen(LudumDare34 game) {
        super(game);
        batch = Assets.batch;

        layout = new NavigationLayout(this);
        SetupNavigation(layout);

        resourceManager = new ResourceManager();

        background = new Background();
        areaMap = new ObjectMap<Area.Type, Area>();
        areaMap.put(Area.Type.MGMT, new AreaMgmt(this));
        areaMap.put(Area.Type.PYRAMID, new AreaPyramid(this));
        areaMap.put(Area.Type.QUARRY, new AreaQuarry(this));
        areaMap.put(Area.Type.FIELD, new AreaField(this));
        areaMap.put(Area.Type.WOODS, new AreaWoods(this));

        TransitionToArea(AreaButton.SelectedButton.AreaLocation);
    }

    public void TransitionToArea(Area.Type area) {
        System.out.println(area.toString());

        currentArea = areaMap.get(area);

        // fancy transition code- tween, twerk or wtf ever

    }

    private void SetupNavigation(NavigationLayout navLayout) {
        AreaButton managementButton = new AreaButton("Management", Area.Type.MGMT);
        AreaButton.SelectedButton = managementButton;

        navLayout.add(managementButton);
        navLayout.add(new AreaButton("Quarry", Area.Type.QUARRY));
        navLayout.add(new AreaButton("Field", Area.Type.FIELD));
        navLayout.add(new AreaButton("Woods", Area.Type.WOODS));

        float height = uiCamera.viewportHeight;
        float third = height / 3;
        navLayout.layout(new Rectangle(0, third, 75, height - third));

        //navLayout.add(new AreaButton("Pyramid", Area.Type.PYRAMID));

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        layout.update();
        resourceManager.update(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        background.render(batch);
        currentArea.render(batch);
        batch.end();

        layout.render(batch, uiCamera);
    }

}
