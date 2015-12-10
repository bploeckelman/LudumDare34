package lando.systems.ld34.utils.camera;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


/**
 * https://github.com/libgdx/libgdx/blob/master/tests/gdx-tests/src/com/badlogic/gdx/tests/utils/OrthoCamController.java
 */
public class OrthoCamController extends InputAdapter implements GestureDetector.GestureListener {
    final OrthographicCamera camera;
    final Vector3 curr                 = new Vector3();
    final Vector3 last                 = new Vector3(-1, -1, -1);
    final Vector3 delta                = new Vector3();
    final Vector3 initialPosition      = new Vector3();
    final float   zoom_scale           = 0.025f;
    final float   min_camera_zoom      = 0.1f;
    final float   initial_camera_zoom  = 1;
    final boolean pan_right_mouse_only = true;
    final float   max_zoom             = 1.5f;

    public MutableFloat camera_zoom = new MutableFloat(initial_camera_zoom);
    public boolean debugRender = false;
    public boolean isRightMouseDown = false;
    public boolean pinchEnabled = false;
    public static boolean isPinching = false; // static so it can be accessed by both this instance and GestureDetectors based of this object
    public float initialScale;

    public OrthoCamController(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.zoom = camera_zoom.floatValue();
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        if (pan_right_mouse_only && isRightMouseDown && !isPinching) {
            camera.unproject(curr.set(x, y, 0));
            if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
                camera.unproject(delta.set(last.x, last.y, 0));
                delta.sub(curr);
                camera.position.add(delta.x, delta.y, 0);
            }
            last.set(x, y, 0);
        }
        return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        if (button == Input.Buttons.RIGHT) {
            isRightMouseDown = false;
        }
        last.set(-1, -1, -1);
        isPinching = false;
        return false;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        initialScale = camera.zoom;
        initialPosition.set(camera.position.x, camera.position.y, 0f);
        if (button == Input.Buttons.RIGHT) {
            isRightMouseDown = true;
        }
        return false;
    }

    @Override
    public boolean scrolled (int amount) {
        float scale = zoom_scale;
        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
            scale *= 10;
        }
        float destZoom = camera_zoom.floatValue() + scale * amount;
        camera_zoom.setValue(MathUtils.lerp(camera_zoom.floatValue(), destZoom, 0.9f));
        if (destZoom < min_camera_zoom) {
            camera_zoom.setValue(min_camera_zoom);
        }
        camera.zoom = camera_zoom.floatValue();
        return false;
    }

    @Override
    public boolean keyDown (int keycode) {
        if (keycode == Keys.P) debugRender = !debugRender;
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        initialPosition.set(camera.position.x, camera.position.y, 0f);
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if (!pinchEnabled) return false;

        float ratio = initialDistance / distance;
        float zoom = initialScale * ratio;
        zoom = Math.max(.2f, Math.min(zoom, max_zoom));
        camera.zoom = zoom;
        camera.update();
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        if (!pinchEnabled) return false;

        double dx1 = initialPointer1.x - pointer1.x;
        double dx2 = initialPointer2.x - pointer2.x;
        double dy1 = initialPointer1.y - pointer1.y;
        double dy2 = initialPointer2.y - pointer2.y;
        double dx = (dx1 + dx2) / 2.0;
        double dy = (dy1 + dy2) / 2.0;

        camera.position.x = (float) (initialPosition.x + dx * camera.zoom);
        camera.position.y = (float) (initialPosition.y - dy * camera.zoom);

        isPinching = true;
        return false;
    }

}
