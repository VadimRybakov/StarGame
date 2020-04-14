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
import ru.geekbrains.utils.EnemyEmitter;

public class MainShip extends Ship {

    private static final int HP = 100;
    private static final float SHIP_HEIGHT = 0.15f;
    private static final float BOTTOM_MARGIN = 0.05f;
    private static final int INVALID_POINTER = -1;
    private float v_len = 0.01f;

    private boolean pressedLeft;
    private boolean pressedRight;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;
    private Vector2 tmp;
    private Vector2 dst;
    private EnemyEmitter enemyEmitter;

    private Vector2 bulletsPosCenter;
    private Vector2 bulletsPosLeft;
    private Vector2 bulletsPosRight;


    private boolean speedFlag = false;
    private boolean bulletSpeedFlag = false;
    private boolean bulletCenterFlag = false;
    private boolean bulletsLeftRightFlag = false;
    private boolean enemyAppearFlag = false;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool, Sound shootSound, EnemyEmitter enemyEmitter) throws GameException {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.shootSound = shootSound;
        bulletRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, 0.5f);
        v0 = new Vector2(0.5f, 0);
        v = new Vector2();
        reloadInterval = 0.2f;
        reloadTimer = reloadInterval;
        bulletHeight = 0.01f;
        damage = 1;
        hp = HP;
        dst = new Vector2();
        tmp = new Vector2();
        this.enemyEmitter = enemyEmitter;
        bulletsPosCenter = new Vector2();
        bulletsPosLeft = new Vector2();
        bulletsPosRight = new Vector2();
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
        if(enemyEmitter.getLevel()%3 == 0 && enemyEmitter.getLevel() < 15 && !speedFlag) {
            v_len *= 1.5f;
            speedFlag = true;
            bulletSpeedFlag = false;
        }
        if(enemyEmitter.getLevel()%5 == 0 && enemyEmitter.getLevel() < 15 && !bulletSpeedFlag){
            bulletV.y *= 1.2f;
            bulletSpeedFlag = true;
            speedFlag = false;
        }
        if(enemyEmitter.getLevel()%5 == 0 && !enemyAppearFlag){
            enemyAppearFlag = true;
            enemyEmitter.setGenerateInterval(enemyEmitter.getGenerateInterval()*0.8f);
        } else if(enemyEmitter.getLevel()%5 == 0) enemyAppearFlag = true;
        else enemyAppearFlag = false;
        if(enemyEmitter.getLevel() < 7 ||
                enemyEmitter.getLevel() >= 20 ){
            bulletsPosCenter.set(pos.x, pos.y + getHalfHeight());
            bulletCenterFlag = true;
        } else bulletCenterFlag = false;
        if(enemyEmitter.getLevel() >= 7){
            bulletsPosLeft.set(pos.x - getHalfWidth()/1.5f, pos.y );
            bulletsPosRight.set(pos.x + getHalfWidth()/1.5f, pos.y );
            bulletsLeftRightFlag = true;
        } else bulletsLeftRightFlag = false;
        autoShoot(delta);
        tmp.set(dst);
        float remainingDistance = (tmp.sub(pos)).len();
        if (remainingDistance > v_len) {
            pos.add(v);
        } else {
            v.setZero();
            pos.set(dst);
        }
    }
    @Override
    protected void shoot() {
        if(bulletCenterFlag){
            Bullet bulletCenter = bulletPool.obtain();
            bulletCenter.set(this, bulletRegion, bulletsPosCenter, bulletV, bulletHeight, worldBounds, damage);
        }
        if(bulletsLeftRightFlag){
            Bullet bulletLeft = bulletPool.obtain();
            Bullet bulletRight = bulletPool.obtain();
            bulletLeft.set(this, bulletRegion, bulletsPosLeft, bulletV, bulletHeight, worldBounds, damage);
            bulletRight.set(this, bulletRegion, bulletsPosRight, bulletV, bulletHeight, worldBounds, damage);
        }



        shootSound.play();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        dst.set(touch);
        dst.y = pos.y;
        v.set(dst.cpy().sub(pos)).setLength(v_len);
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

}
