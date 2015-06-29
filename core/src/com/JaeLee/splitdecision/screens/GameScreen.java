package com.JaeLee.splitdecision.screens;

import com.JaeLee.splitdecision.debug.InputController;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen implements Screen {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    //A camera that is like looking at a paper. 2d camera.
    private OrthographicCamera camera;

    private final float TIMESTEP = 1 / 60f;
    //8 and 3 is the example in box2d documentation, and it seems to work and is stable. Higher = better simulation, but more computation used.
    private final int VELOCITYITERATIONS = 8, POSITIONITERATION = 3;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //combine uses what you need. all you need to know for now...
        debugRenderer.render(world, camera.combined);

        //tell the world that time is passing
        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATION);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / 100;
        camera.viewportHeight = height / 100;
        camera.update();
    }

    @Override
    public void show() {
        world = new World(new Vector2(0, -9.81f), true);
        debugRenderer = new Box2DDebugRenderer();

        camera = new OrthographicCamera();

        Gdx.input.setInputProcessor(new InputController(){
            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.ESCAPE)
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
                return true;
            }
        });

        //body types. static - dose not move. dynamic - moves like normal, Collisions, drops. Kinematic - can move, but is not affected by kinematic or dynamic bodies. lose to static.

        //a ball yay~ step 1) create a body definition (def only) -----------------------------------------------------------------------------------------------------------------------
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        //uses a meter. so not 1 pixel, but 1 meter.
        bodyDef.position.set(0, 10);

        //ball shape is needed.
        CircleShape shape = new CircleShape();
        //use shape.setPosition(Vector2) to put the def in a different place.
        //in meters
        shape.setRadius(.5f);

        // create fixture definition - this is the physical properties of the body.
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2;
        //from 0 - 1. 1 is max slide so it does not slide.
        fixtureDef.friction = .2f;
        //its "bounciness" again, 0 - 1. 1 is a 100% reflected force.
        fixtureDef.restitution = .8f;

        world.createBody(bodyDef).createFixture(fixtureDef);

        shape.dispose();
        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //a ground ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);

        //ground shape - a chain shape goes from vertices to vertices.
        ChainShape groundShape = new ChainShape();
        groundShape.createChain(new Vector2[]{new Vector2(-1, 0), new Vector2(1, 0), new Vector2(1, -1), new Vector2(-1, -1), new Vector2(-1, 0)});

        //fixture definition
        fixtureDef.shape = groundShape;
        fixtureDef.friction = .5f;
        fixtureDef.restitution = 0;

        world.createBody(bodyDef).createFixture(fixtureDef);

        groundShape.dispose();
        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }
}
