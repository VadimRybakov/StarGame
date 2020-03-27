package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.exception.GameException;
import ru.geekbrains.math.Rect;

public class Logo extends Sprite {

    private static final float V_LEN = 0.1f;
    private Vector2 v;
    private Vector2 tmp;
    private Vector2 dst;

    public Logo(Texture texture) throws GameException {
        super(new TextureRegion(texture));
        v = new Vector2();
        tmp = new Vector2();
        dst = new Vector2();
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
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
        pos.set(worldBounds.pos);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        dst.set(touch);
        v.set(dst.cpy().sub(pos)).setLength(V_LEN);
        return false;
    }
}
