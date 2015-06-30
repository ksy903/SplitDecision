package com.JaeLee.splitdecision.screens;

import com.JaeLee.splitdecision.debug.InputController;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    //A camera that is like looking at a paper. 2d camera.
    private OrthographicCamera camera;

    private final float TIMESTEP = 1 / 60f;
    //8 and 3 is the example in box2d documentation, and it seems to work and is stable. Higher = better simulation, but more computation used.
    private final int VELOCITYITERATIONS = 8, POSITIONITERATION = 3;
    private Body box, floor;
    private float speed = 500;
    private Vector2 movement = new Vector2();
    private Sprite manSprite, brickSprite;
    private SpriteBatch batch;
    private Array<Body> tmpBodies = new Array<Body>();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //tell the world that time is passing
        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATION);
        box.applyForceToCenter(movement, true);

        camera.position.set(box.getPosition().x, box.getPosition().y, 0);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        world.getBodies(tmpBodies);
        for(Body body : tmpBodies)
        if(body.getUserData() != null && body.getUserData() instanceof Sprite){
            Sprite sprite = (Sprite)body.getUserData();
            sprite.setPosition(body.getPosition().x - (sprite.getWidth() / 2f), body.getPosition().y - (sprite.getHeight() / 2f));
            sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
            sprite.draw(batch);
        }
        batch.end();

        //combine uses what you need. all you need to know for now...
        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / 25;
        camera.viewportHeight = height / 25;
        camera.update();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        world = new World(new Vector2(0, -9.81f), true);
        debugRenderer = new Box2DDebugRenderer();

        camera = new OrthographicCamera();

        Gdx.input.setInputProcessor(new InputController(){
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.ESCAPE:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
                        break;
                    case Input.Keys.W:
                        movement.y = speed;
                        break;
                    case Input.Keys.A:
                        movement.x = -speed;
                        break;
                    case Input.Keys.S:
                        movement.y = -speed;
                        break;
                    case Input.Keys.D:
                        movement.x = speed;
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Input.Keys.W:
                        movement.y = 0;
                        break;
                    case Input.Keys.A:
                        movement.x = 0;
                        break;
                    case Input.Keys.S:
                        movement.y = 0;
                        break;
                    case Input.Keys.D:
                        movement.x = 0;
                        break;
                }
                return true;
            }

            @Override
            public boolean scrolled(int amount) {
                camera.zoom += amount / 25f;
                return true;
            }
        });

        //body types. static - dose not move. dynamic - moves like normal, Collisions, drops. Kinematic - can move, but is not affected by kinematic or dynamic bodies. lose to static.
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        //A movable box -----------------------------------------------------------------------------------------------------------------------------------------------------------------
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(2.25f, 10);

        //box shape
        PolygonShape boxShape = new PolygonShape();
        //the values are halves.
        boxShape.setAsBox(.5f, 1);

        //fixture definition
        fixtureDef.shape = boxShape;
        fixtureDef.friction = .75f;
        fixtureDef.restitution = .1f;
        fixtureDef.density = 5;

        box = world.createBody(bodyDef);
        box.createFixture(fixtureDef);


        manSprite = new Sprite(new Texture(Gdx.files.internal("man.png")));
        manSprite.setSize(1, 2);
        manSprite.setOrigin(manSprite.getWidth()/2f, manSprite.getHeight()/2f);
        box.setUserData(manSprite);

        boxShape.dispose();
        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //a ball yay~ step 1) create a body definition (def only) -----------------------------------------------------------------------------------------------------------------------

        //ball shape is needed.
        CircleShape shape = new CircleShape();
        //use shape.setPosition(Vector2) to put the def in a different place.
        //in meters
        shape.setRadius(.5f);
        shape.setPosition(new Vector2(0, 1.5f));

        // create fixture definition - this is the physical properties of the body.
        fixtureDef.shape = shape;
        fixtureDef.density = 2;
        //from 0 - 1. 1 is max slide so it does not slide.
        fixtureDef.friction = .2f;
        //its "bounciness" again, 0 - 1. 1 is a 100% reflected force.
        fixtureDef.restitution = .8f;

        box.createFixture(fixtureDef);

        shape.dispose();
        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //a ground ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);

        //ground shape - a chain shape goes from vertices to vertices.
        ChainShape groundShape = new ChainShape();
        groundShape.createChain(new Vector2[]{new Vector2(-10, 1), new Vector2(10, 1), new Vector2(10, -1), new Vector2(-10, -1), new Vector2(-10, 1)});

        //fixture definition
        fixtureDef.shape = groundShape;
        fixtureDef.friction = .5f;
        fixtureDef.restitution = 0;

        floor = world.createBody(bodyDef);
        floor.createFixture(fixtureDef);

        brickSprite = new Sprite(new Texture(Gdx.files.internal("Brick.png")));
        brickSprite.setSize(20, 2);
        brickSprite.setOrigin(brickSprite.getWidth()/2f, brickSprite.getHeight()/2f);
        floor.setUserData(brickSprite);

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
        manSprite.getTexture().dispose();
    }
}
