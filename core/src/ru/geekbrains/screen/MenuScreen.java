package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture img;
    private Vector2 pos;
    private Vector2 v;
    private Vector2 dst;
//    private float rotate;

    @Override
    public void show() {
        super.show();
        img = new Texture("badlogic.jpg");
        pos = new Vector2();
        v = new Vector2();
//        rotate = 0;
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        dst = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
        v.set(0.01f * screenX, 0.01f * (Gdx.graphics.getHeight() - screenY));
        pos.setZero();
        return false;
    }

    private void update(float delta) {
        if(pos.epsilonEquals(dst, 0.001f)) v.setZero();
        pos.add(v);
//        rotate += 1;
    }

    private void draw() {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, pos.x, pos.y);
//        batch.draw(new TextureRegion(img), pos.x, pos.y, pos.x, pos.y, 250, 250, 1, 1, rotate);
        batch.end();
    }

}
