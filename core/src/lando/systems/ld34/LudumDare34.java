package lando.systems.ld34;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import lando.systems.ld34.screens.GameScreen;
import lando.systems.ld34.utils.Assets;

public class LudumDare34 extends Game {

	public static GameScreen GameScreen;

	@Override
	public void create () {
		Assets.load();
		GameScreen = new GameScreen(this);
		setScreen(GameScreen);
	}

	@Override
	public void render () {
		float delta = Math.min(1 / 30f, Gdx.graphics.getDeltaTime());
		Assets.tween.update(delta);
		if (screen != null) {
			screen.render(delta);
		}
	}

	@Override
	public void dispose() {
		Assets.dispose();
	}
}
