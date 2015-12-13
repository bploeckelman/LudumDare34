package lando.systems.ld34.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.uielements.AreaButton;
import lando.systems.ld34.uielements.ManagementButton;
import lando.systems.ld34.uielements.NavigationButton;
import lando.systems.ld34.world.Area;

import java.util.ArrayList;

/**
 * Created by Brian on 12/12/2015.
 */
public class NavigationLayout {

    private final GameScreen _screen;
    private ArrayList<AreaButton> _areaButtons = new ArrayList<AreaButton>();
    private ArrayList<ManagementButton> _skillsButtons = new ArrayList<ManagementButton>();

    public NavigationLayout(GameScreen screen) {
        _screen = screen;
    }

    public void add(AreaButton button) {
        _areaButtons.add(button);
    }

    public void add(ManagementButton button) {
        _skillsButtons.add(button);
    }

    private Rectangle _navBounds;

    public void layoutAreaButtons(Rectangle bounds) {
        _navBounds = bounds;
        float height = 0f;

        for (NavigationButton button : _areaButtons) {
            height += button.Bounds.height;
        }

        int space = (int)(bounds.height - height) / (_areaButtons.size() + 1);

        float y = bounds.y + bounds.height;
        for (NavigationButton button : _areaButtons) {
            y -= (button.Bounds.height + space);

            button.Bounds.y = y;
            button.Bounds.x = (int)(bounds.x + (bounds.width - button.Bounds.width)/2);
        }
    }

    public void layoutManagement(Rectangle bounds) {
        float width = 0f;

        for (NavigationButton button : _skillsButtons) {
            width += button.Bounds.width;
        }

        int space = (int)(bounds.width - width) / (_skillsButtons.size() + 1);

        float x = bounds.x + space;
        for (NavigationButton button : _skillsButtons) {
            button.Bounds.y = (int)(bounds.y + (bounds.height - button.Bounds.height)/2);
            button.Bounds.x = (int)x;
            x += button.Bounds.width + space;
        }
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        //batch.setColor(Color.BLUE);
        //batch.draw(Assets.whiteTexture, _navBounds.x, _navBounds.y, _navBounds.width, _navBounds.height);

        for (NavigationButton button : _areaButtons) {
            button.render(batch);
        }

        if (isManagementScreen()) {
            for (NavigationButton button : _skillsButtons) {
                button.render(batch);
            }
        }

        batch.end();
    }

    private boolean isManagementScreen() {
        return LudumDare34.GameScreen.currentArea.type == Area.Type.MGMT;
    }

    public void update() {
        Vector3 mousePos = _screen.getMouseScreenPos();
        // mouse y is top down, render is down up - reverse the y y? because. fucker.
        mousePos.y = _screen.uiCamera.viewportHeight - mousePos.y;

        boolean clicked = Gdx.input.justTouched();

        for (NavigationButton button : _areaButtons) {
            button.update(mousePos, clicked);
        }

        if (isManagementScreen()) {
            for (NavigationButton button : _skillsButtons) {
                button.update(mousePos, clicked);
            }
        }
    }
}
