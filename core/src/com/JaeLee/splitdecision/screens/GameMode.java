package com.JaeLee.splitdecision.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class GameMode implements Screen {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private final float metersToPixels = 32;
    private Body wallLeft, wallCenter, wallRight;
    private float gravity;
    
    /*
    TODO Add Sprites.
    private SpriteBatch batch;
    private Sprite wallLeftSprite, wallCenterSprite, wallRightSprite;
    */

    @Override
    public void render(float delta) {
        camera.position.set(0, 0, 0);
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width/metersToPixels;
        camera.viewportHeight = height/metersToPixels;
        camera.update();
    }

    @Override
    public void show() {
        System.out.println(Gdx.graphics.getWidth()+ " " + Gdx.graphics.getHeight());
        gravity = -9.81f;
        world = new World(new Vector2(0, gravity), true);
        debugRenderer = new Box2DDebugRenderer();

        camera = new OrthographicCamera();

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(-((float)Gdx.graphics.getWidth() / 2) / metersToPixels + .5f, 0);

        ChainShape wallL = new ChainShape();
        wallL.createChain(new Vector2[]{new Vector2(-0.5f, ((float)Gdx.graphics.getHeight() / 2) / metersToPixels),
                                        new Vector2(0.5f, ((float)Gdx.graphics.getHeight() / 2) / metersToPixels),
                                        new Vector2(0.5f, -((float)Gdx.graphics.getHeight() / 2) / metersToPixels),
                                        new Vector2(-0.5f, -((float)Gdx.graphics.getHeight() / 2) / metersToPixels),
                                        new Vector2(-0.5f, ((float)Gdx.graphics.getHeight() / 2) / metersToPixels)});
        fixtureDef.shape = wallL;
        fixtureDef.friction = 1;
        fixtureDef.restitution = 0;

        wallLeft = world.createBody(bodyDef);
        wallLeft.createFixture(fixtureDef);

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

    }
}
