package lando.systems.ld34.uielements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.world.Area;

/**
 * Created by Brian on 12/12/2015.
 */
public class PyramidButton extends AreaButton {

    public Rectangle Boundary;

    public PyramidButton(Rectangle boundary) {
        super("Pyramid", Assets.pyramidIcon, Area.Type.PYRAMID);

        Boundary = boundary;
        Bounds = new Rectangle(0, 0, 50, 0);
        ImageColor = new Color(1, 1, 1, 1);
    }

    @Override
    public void update(Vector3 mousePos, boolean clicked) {
        super.update(mousePos, clicked);
        setBounds(Math.max(1, LudumDare34.GameScreen.Pyramid.getHeight()));
    }

    private void setBounds(int pyramidHeight) {
        Bounds.height = pyramidHeight * 20;
        Bounds.x = (int)(Boundary.x + (Boundary.width - Bounds.width) / 2);
        Bounds.y = (int)(Boundary.y + (Boundary.height - Bounds.height) / 2);
    }
}
