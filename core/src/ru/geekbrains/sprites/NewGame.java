package ru.geekbrains.sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.exception.GameException;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.GameScreen;

public class NewGame extends ScaledButton {
    private final Game game;

    public NewGame(TextureAtlas atlas, Game game) throws GameException {
        super(atlas.findRegion("button_new_game"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        setTop(0.2f);
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen(game));
    }
}
