package ru.geekbrains.sprites;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Ship;
import ru.geekbrains.exception.GameException;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;

public class MainShip extends Ship {

    private static final int HP = 10;
    private static final float SHIP_HEIGHT = 0.15f;
    private static final float BOTTOM_MARGIN = 0.05f;
    private static final int INVALID_POINTER = -1;
    private static final float V_LEN = 0.01f;

    private boolean pressedLeft;
    private boolean pressedRight;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;
    private Vector2 tmp;
    private Vector2 dst;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool, Sound shootSound) throws GameException {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.shootSound = shootSound;
        bulletRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, 0.5f);
        bulletPos = new Vector2();
        v0 = new Vector2(0.5f, 0);
        v = new Vector2();
        reloadInterval = 0.2f;
        reloadTimer = reloadInterval;
        bulletHeight = 0.01f;
        damage = 1;
        hp = HP;
        dst = new Vector2();
        tmp = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(SHIP_HEIGHT);
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        bulletPos.set(pos.x, pos.y + getHalfHeight());
        autoShoot(delta);
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
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        dst.set(touch);
        dst.y = pos.y;
        v.set(dst.cpy().sub(pos)).setLength(V_LEN);
        return false;
    }


    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
        }
        return false;
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > pos.y
                || bullet.getTop() < getBottom());
    }

    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotate(180);
    }

    private void stop() {
        v.setZero();
    }

    public void startNewGame(Rect worldBounds) {
        flushDestroy();
        hp = HP;
        pressedLeft = false;
        pressedRight = false;
        leftPointer = INVALID_POINTER;
        rightPointer = INVALID_POINTER;
        stop();
        pos.x = worldBounds.pos.x;
    }
}
