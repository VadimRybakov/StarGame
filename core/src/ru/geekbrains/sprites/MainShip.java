package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.exception.GameException;
import ru.geekbrains.math.Rect;

public class MainShip extends Sprite {
    private Rect worldBounds;
    private static final float HEIGHT = 0.12f;
    private static final float V_LEN = 0.01f;
    private Vector2 v;
    private Vector2 dst;
    private Vector2 tmp;
    public MainShip(TextureAtlas atlas) throws GameException {
        super(new TextureRegion(atlas.findRegion("main_ship"), 0, 0,
                atlas.findRegion("main_ship").getRegionWidth()/2,
                atlas.findRegion("main_ship").getRegionHeight()));
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(HEIGHT);
        v = new Vector2();
        dst = new Vector2();
        tmp = new Vector2();
        setBottom(worldBounds.getBottom() + 0.05f);
    }

    @Override
    public void update(float delta) {
        tmp.set(dst);
        float remainingDistance = (tmp.sub(pos)).len();
        if (remainingDistance > V_LEN) {
            pos.add(v);
        } else {
            v.setZero();
            pos.set(dst);
        }
        if (getRight() >= worldBounds.getRight()) {
            setRight(worldBounds.getRight());
        }
        if (getLeft() <= worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        dst.set(touch);
        dst.y = pos.y;
        v.set(dst.cpy().sub(pos)).setLength(V_LEN);
        return false;
    }
}
