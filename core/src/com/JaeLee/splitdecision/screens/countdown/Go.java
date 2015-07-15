package com.JaeLee.splitdecision.screens.countdown;

import com.JaeLee.splitdecision.screens.GameMode;
import com.JaeLee.splitdecision.tween.SpriteAccessor;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class Go implements Screen{

    private Sprite logoScreen;
    private SpriteBatch batch;
    private TweenManager tweenManager;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //delta is time from last frame to current frame
        tweenManager.update(delta);
        batch.begin();
        logoScreen.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        Texture logoTexture = new Texture(Gdx.files.internal("GO.png"));
        logoScreen = new Sprite(logoTexture);
        logoScreen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.set(logoScreen, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(logoScreen, SpriteAccessor.ALPHA, .3f).target(1).repeatYoyo(1, 0).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameMode());
            }
        }).start(tweenManager);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        logoScreen.getTexture().dispose();
    }
}
