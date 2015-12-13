package lando.systems.ld34.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld34.uielements.NavigationButton;
import lando.systems.ld34.utils.Assets;

import java.util.ArrayList;

/**
 * Created by Brian on 12/12/2015.
 */
public class NavigationLayout {

    private final GameScreen _screen;
    public final ArrayList<NavigationButton> NavButtons = new ArrayList<NavigationButton>();

    public NavigationLayout(GameScreen screen) {
        _screen = screen;
    }

    public void add(NavigationButton button) {
        NavButtons.add(button);
    }

    private Rectangle _navBounds;

    public void layout(Rectangle bounds) {
        _navBounds = bounds;
        float height = 0f;

        for (NavigationButton button : NavButtons) {
            height += button.Bounds.height;
        }

        int space = (int)(bounds.height - height) / (NavButtons.size() + 1);

        float y = bounds.y + bounds.height;
        for (NavigationButton button : NavButtons) {
            y -= (button.Bounds.height + space);

            button.Bounds.y = y;
            button.Bounds.x = (int)(bounds.x + (bounds.width - button.Bounds.width)/2);
        }
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        //batch.setColor(Color.BLUE);
        //batch.draw(Assets.whiteTexture, _navBounds.x, _navBounds.y, _navBounds.width, _navBounds.height);

        for (NavigationButton button : NavButtons) {
            button.render(batch);
        }

        batch.end();
    }

    public void update() {
        Vector3 mousePos = _screen.getMouseScreenPos();
        // mouse y is top down, render is down up - reverse the y y? because. fucker.
        mousePos.y = _screen.uiCamera.viewportHeight - mousePos.y;

        boolean clicked = Gdx.input.justTouched();

        for (NavigationButton button : NavButtons) {
            button.update(mousePos, clicked);
        }
    }
}
