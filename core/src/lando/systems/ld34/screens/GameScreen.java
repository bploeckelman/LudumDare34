package lando.systems.ld34.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectMap;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.world.Area;
import lando.systems.ld34.world.AreaMgmt;

/**
 * Brian Ploeckelman created on 12/9/2015.
 */
public class GameScreen extends AbstractScreen {

    final SpriteBatch      batch;
    final NavigationLayout layout;

    ObjectMap<Area.Type, Area> areaMap;
    Area currentArea;

    public GameScreen(LudumDare34 game) {
        super(game);
        batch = Assets.batch;
        layout = new NavigationLayout(this);
        areaMap = new ObjectMap<Area.Type, Area>();
        areaMap.put(Area.Type.MGMT, new AreaMgmt(this));
        // TODO: add other areas
        currentArea = areaMap.get(Area.Type.MGMT);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        layout.update();
    }

    @Override
    public void render(float delta) {
        update(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        currentArea.render(batch);
        batch.end();

        layout.render(batch, uiCamera);
    }

}
