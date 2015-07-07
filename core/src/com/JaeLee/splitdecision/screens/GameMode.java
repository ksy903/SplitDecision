package com.JaeLee.splitdecision.screens;

import com.JaeLee.splitdecision.debug.InputController;
import com.JaeLee.splitdecision.debug.ObjectUserData;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GameMode implements Screen {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private final float metersToPixels = 32;
    private Body wallLeft, wallCenter, wallRight, shipLeft, shipRight;
    private float gravity;
    private final float TIMESTEP = 1 / 60f;
    private final int VELOCITYITERATIONS = 8, POSITIONITERATION = 3;
    private Vector2[] borderSize = new Vector2[]{
            new Vector2(0.5f, ((float)Gdx.graphics.getHeight() / 2) / metersToPixels),
            new Vector2(-0.5f, ((float)Gdx.graphics.getHeight() / 2) / metersToPixels),
            new Vector2(-0.5f, -((float)Gdx.graphics.getHeight() / 2) / metersToPixels),
            new Vector2(0.5f, -((float)Gdx.graphics.getHeight() / 2) / metersToPixels),
            new Vector2(0.5f, ((float)Gdx.graphics.getHeight()/2) / metersToPixels)};
    private float speed = 15;
    private BodyDef bodyDef = new BodyDef();
    private FixtureDef fixtureDef = new FixtureDef();
    private Array<Body> tmpBodies = new Array<Body>();

    //Keeping objects in right size.
    private static final int VIRTUAL_WIDTH = 1920;
    private static final int VIRTUAL_HEIGHT = 1080;
    private static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH / (float)VIRTUAL_HEIGHT;
    private Rectangle viewport;
    private float leftS2 = .5f, leftE2 = 11.5f, rightS2 = 16.5f, rightE2 = 26.5f, myP1 = .5f;
    private float leftS1 = .5f, leftE1 = 11.5f, rightS1 = 16.5f, rightE1 = 26.5f, myP2 = .5f;
    float scale;

    /*
    TODO Add Sprites.
    private SpriteBatch batch;
    private Sprite wallLeftSprite, wallCenterSprite, wallRightSprite;
    */

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATION);

        camera.position.set(0, 0, 0);
        camera.update();
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);

        world.getBodies(tmpBodies);

        for(Body body:tmpBodies){
            if(body != null && body.getUserData() instanceof ObjectUserData){
                if(body.getPosition().y <= ((float)Gdx.graphics.getHeight()/2/metersToPixels)-.5f &&!((ObjectUserData) body.getUserData()).reproduced){
                    makeRow();
                    ((ObjectUserData) body.getUserData()).reproduced = true;
                }
                if(body.getPosition().y < -((float)Gdx.graphics.getHeight()/2/metersToPixels) - .5f){
                    world.destroyBody(body);
                }
            }
        }
        gravity += 0.01;
        debugRenderer.render(world, camera.combined);

    }

    @Override
    public void resize(int width, int height) {
        float aspectRatio = (float)width / (float)height;
        Vector2 crop = new Vector2(0f, 0f);
        if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)height/(float)VIRTUAL_HEIGHT;
            crop.x = (width - VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
            crop.y = (height - VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
        }
        System.out.println(scale);
        float w = (float)VIRTUAL_WIDTH*scale;
        float h = (float)VIRTUAL_HEIGHT*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
        camera.viewportWidth = width/metersToPixels+5;
        camera.viewportHeight = height/metersToPixels+5;
        camera.update();
    }

    @Override
    public void show() {

        gravity = -9.81f;
        world = new World(new Vector2(0, gravity), true);
        debugRenderer = new Box2DDebugRenderer();

        camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        //----------------------------------------------------------------------------------------
        bodyDef.type = BodyDef.BodyType.StaticBody;
        ChainShape wall = new ChainShape();
        wall.createChain(borderSize);
        fixtureDef.shape = wall;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0;
        bodyDef.gravityScale = 0;

        bodyDef.position.set(-((float)Gdx.graphics.getWidth() / 2) / metersToPixels + .5f - .01f, 0);
        wallLeft = world.createBody(bodyDef);
        wallLeft.createFixture(fixtureDef);
        bodyDef.position.set(0,0);
        wallCenter = world.createBody(bodyDef);
        ChainShape cent = new ChainShape();
        cent.createChain(new Vector2[]{
                new Vector2(-1.5f, ((float)Gdx.graphics.getHeight() / 2) / metersToPixels),
                new Vector2(1.5f, ((float)Gdx.graphics.getHeight() / 2) / metersToPixels),
                new Vector2(1.5f, -((float)Gdx.graphics.getHeight() / 2) / metersToPixels),
                new Vector2(-1.5f, -((float)Gdx.graphics.getHeight() / 2) / metersToPixels),
                new Vector2(-1.5f, ((float)Gdx.graphics.getHeight() / 2) / metersToPixels)});
        fixtureDef.shape = cent;
        wallCenter.createFixture(fixtureDef);
        fixtureDef.shape = wall;
        bodyDef.position.set(((float)Gdx.graphics.getWidth() / 2) / metersToPixels - .5f + .01f, 0);
        wallRight = world.createBody(bodyDef);
        wallRight.createFixture(fixtureDef);
        //----------------------------------------------------------------------------------------
        PolygonShape shipShape = new PolygonShape();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        shipShape.setAsBox(.5f, .25f);
        fixtureDef.shape = shipShape;
        bodyDef.gravityScale = 0;

        bodyDef.position.set(((float)Gdx.graphics.getWidth() / 4) / metersToPixels,
                            -((float)Gdx.graphics.getHeight() / 2) / metersToPixels + 1);
        shipRight = world.createBody(bodyDef);
        shipRight.createFixture(fixtureDef);
        bodyDef.position.set(-((float)Gdx.graphics.getWidth() / 4) / metersToPixels,
                -((float)Gdx.graphics.getHeight() / 2) / metersToPixels + 1);
        shipLeft = world.createBody(bodyDef);
        shipLeft.createFixture(fixtureDef);
        //---------------------------------------------------------------------------------------

        Gdx.input.setInputProcessor(new InputController() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if(screenX < ((float)Gdx.graphics.getWidth() / 2) + (shipLeft.getPosition().x * metersToPixels)){
                    shipLeft.setLinearVelocity(-speed,0);
                }
                if (screenX > ((float)Gdx.graphics.getWidth() / 2) + (shipLeft.getPosition().x * metersToPixels) && screenX < (float)Gdx.graphics.getWidth() / 2){
                    shipLeft.setLinearVelocity(speed,0);
                }
                if(screenX < (shipRight.getPosition().x * metersToPixels) + ((float)Gdx.graphics.getWidth() / 2) && screenX > (float)Gdx.graphics.getWidth() / 2){
                    shipRight.setLinearVelocity(-speed,0);
                }

                if(screenX > (shipRight.getPosition().x * metersToPixels) + ((float)Gdx.graphics.getWidth() / 2)){
                    shipRight.setLinearVelocity(speed,0);
                }
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if(screenX < ((float)Gdx.graphics.getWidth() / 2) + (shipLeft.getPosition().x * metersToPixels)){
                    shipLeft.setLinearVelocity(-speed,0);
                }
                if (screenX > ((float)Gdx.graphics.getWidth() / 2) + (shipLeft.getPosition().x * metersToPixels) && screenX < (float)Gdx.graphics.getWidth() / 2){
                    shipLeft.setLinearVelocity(speed,0);
                }
                if(screenX < (shipRight.getPosition().x * metersToPixels) + ((float)Gdx.graphics.getWidth() / 2) && screenX > (float)Gdx.graphics.getWidth() / 2){
                    shipRight.setLinearVelocity(-speed,0);
                }

                if(screenX > (shipRight.getPosition().x * metersToPixels) + ((float)Gdx.graphics.getWidth() / 2)){
                    shipRight.setLinearVelocity(speed,0);
                }
                if(screenX >= ((float)Gdx.graphics.getWidth() / 2) + (shipLeft.getPosition().x * metersToPixels) - (.5 * metersToPixels) &&
                        screenX <= ((float)Gdx.graphics.getWidth() / 2) + (shipLeft.getPosition().x * metersToPixels) + (.5 * metersToPixels))
                    shipLeft.setLinearVelocity(0,0);
                if(screenX >= (shipRight.getPosition().x * metersToPixels) + ((float)Gdx.graphics.getWidth() / 2) - (.5 * metersToPixels) &&
                        screenX <= (shipRight.getPosition().x * metersToPixels) + ((float)Gdx.graphics.getWidth() / 2) + (.5 * metersToPixels))
                    shipRight.setLinearVelocity(0,0);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                shipLeft.setLinearVelocity(0,0);
                shipRight.setLinearVelocity(0,0);
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.ESCAPE)
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
                return true;
            }
        });
        makeRow();
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
    private void makeRow(){
        PolygonShape brickShape = new PolygonShape();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.gravityScale = 1;
        bodyDef.position.set(-((float)Gdx.graphics.getWidth()/2/metersToPixels)+1+((leftE1-leftS1)/2),
                ((float)Gdx.graphics.getHeight()/2/metersToPixels)+1);
        brickShape.setAsBox((leftE1-leftS1)*scale/2-.02f,.5f);
        fixtureDef.shape = brickShape;
        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(new ObjectUserData());
    }
}
