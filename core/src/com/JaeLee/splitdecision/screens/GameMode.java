package com.JaeLee.splitdecision.screens;

import com.JaeLee.splitdecision.debug.InputController;
import com.JaeLee.splitdecision.debug.ObjectUserData;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
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

import java.util.Random;

public class GameMode implements Screen {
    private int colorCounter;
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
    private float speed = 20;
    private BodyDef bodyDef = new BodyDef();
    private FixtureDef fixtureDef = new FixtureDef();
    private Array<Body> tmpBodies = new Array<Body>();
    public static int bestCount = 0;
    private int currentCount = 0;
    private SpriteBatch batch;
    private Sprite backdrop;
    private int pointerLeft, pointerRight;
    private boolean left, right;

    //Keeping objects in right-ish size.
    private static final int VIRTUAL_WIDTH = 1920;
    private static final int VIRTUAL_HEIGHT = 1080;
    private static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH / (float)VIRTUAL_HEIGHT;
    private Rectangle viewport;
    private float left1, left2, right1, right2;
    float scale;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATION);

        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);

        world.getBodies(tmpBodies);

        batch.begin();
        backdrop.draw(batch);
        Sprite sprites = new Sprite(new Texture(Gdx.files.internal("White.png")));
        sprites.setRotation(shipLeft.getAngle() * MathUtils.radiansToDegrees);
        sprites.setSize(metersToPixels, 0.5f * metersToPixels);

        Sprite line = new Sprite(new Texture(Gdx.files.internal("Red.png")));
        line.setSize(Gdx.graphics.getWidth(), 1);
        line.setPosition(0, Gdx.graphics.getHeight()/4);
        line.draw(batch);


        sprites.setPosition((float)Gdx.graphics.getWidth()/2 + (shipLeft.getPosition().x * metersToPixels) - (sprites.getWidth()/2), (float)Gdx.graphics.getHeight()/2 + (shipLeft.getPosition().y * metersToPixels)- (sprites.getHeight()/2));
        sprites.draw(batch);
        sprites.setPosition((float)Gdx.graphics.getWidth()/2 + (shipRight.getPosition().x * metersToPixels) - (sprites.getWidth()/2), (float)Gdx.graphics.getHeight()/2 + (shipRight.getPosition().y * metersToPixels) - (sprites.getHeight()/2));
        sprites.draw(batch);

        for(Body body:tmpBodies){
            if(body != null && body.getUserData() instanceof ObjectUserData){
                Sprite sprite = new Sprite(((ObjectUserData) body.getUserData()).texture);
                sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);

                sprite.setSize(((ObjectUserData) body.getUserData()).size1 * metersToPixels, metersToPixels);
                sprite.setPosition((float)Gdx.graphics.getWidth()/2 + ((wallLeft.getPosition().x + (((ObjectUserData) body.getUserData()).size1/2) + .5f) * metersToPixels) - (sprite.getWidth() / 2),
                        (float)Gdx.graphics.getHeight()/2 + ((body.getPosition().y * metersToPixels)) - (sprite.getHeight() / 2));
                sprite.draw(batch);

                sprite.setSize(((ObjectUserData) body.getUserData()).size2 * metersToPixels, metersToPixels);
                sprite.setPosition((float)Gdx.graphics.getWidth()/2 + ((wallCenter.getPosition().x - (((ObjectUserData) body.getUserData()).size2/2) - 1.5f) * metersToPixels) - (sprite.getWidth() / 2),
                        (float)Gdx.graphics.getHeight()/2 + ((body.getPosition().y * metersToPixels)) - (sprite.getHeight() / 2));
                sprite.draw(batch);

                sprite.setSize(((ObjectUserData) body.getUserData()).size3 * metersToPixels, metersToPixels);
                sprite.setPosition((float)Gdx.graphics.getWidth()/2 + ((wallCenter.getPosition().x + (((ObjectUserData) body.getUserData()).size3/2) + 1.5f) * metersToPixels) - (sprite.getWidth() / 2),
                        (float)Gdx.graphics.getHeight()/2 + ((body.getPosition().y * metersToPixels)) - (sprite.getHeight() / 2));
                sprite.draw(batch);

                sprite.setSize(((ObjectUserData) body.getUserData()).size4 * metersToPixels, metersToPixels);
                sprite.setPosition((float)Gdx.graphics.getWidth()/2 + ((wallRight.getPosition().x - (((ObjectUserData) body.getUserData()).size4/2) - .5f) * metersToPixels) - (sprite.getWidth() / 2),
                        (float)Gdx.graphics.getHeight()/2 + ((body.getPosition().y * metersToPixels)) - (sprite.getHeight() / 2));
                sprite.draw(batch);

                if(!((ObjectUserData) body.getUserData()).reproduced && body.getPosition().y + 0.49f <= ((float)Gdx.graphics.getHeight()/2/metersToPixels)){
                    ((ObjectUserData) body.getUserData()).reproduced = true;
                    colorCounter++;
                    changeDirection();
                    makeRow();
                }
                if((body.getPosition().y +.5f < shipLeft.getPosition().y - .25f && !((ObjectUserData) body.getUserData()).counted) ||
                        (body.getPosition().y +.5f < shipRight.getPosition().y - .25f && !((ObjectUserData) body.getUserData()).counted)){
                    currentCount++;
                    ((ObjectUserData) body.getUserData()).counted = true;
                }
                if(body.getPosition().y + .5f <= -(float)Gdx.graphics.getHeight() / 2 / metersToPixels){
                    world.destroyBody(body);
                }

            }
        }
        batch.end();

        if(shipLeft.getPosition().y + 0.25f < -((float)Gdx.graphics.getHeight()/4/metersToPixels) || shipRight.getPosition().y + 0.25f < -((float)Gdx.graphics.getHeight()/4/metersToPixels)){
            if(currentCount/4 > bestCount)
                bestCount = currentCount/4;
            ((Game)Gdx.app.getApplicationListener()).setScreen(new EndScreen());
        }
        gravity -= 0.005;
        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        left1 = Gdx.graphics.getWidth() / 2 * 0.3f / metersToPixels;
        left2 = Gdx.graphics.getWidth() / 2 * 0.3f / metersToPixels;
        right1 = Gdx.graphics.getWidth() / 2 * 0.3f / metersToPixels;
        right2 = Gdx.graphics.getWidth() / 2 * 0.3f / metersToPixels;
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
        float w = (float)VIRTUAL_WIDTH*scale;
        float h = (float)VIRTUAL_HEIGHT*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
        camera.viewportWidth = width/metersToPixels + 0.1f;
        camera.viewportHeight = height/metersToPixels + 0.1f;
        camera.update();
        makeRow();
    }
    @Override
    public void show() {
        backdrop = new Sprite(new Texture(Gdx.files.internal("GameBackdrop.png")));
        backdrop.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backdrop.setPosition(0,0);
        colorCounter = 0;
        gravity = -9.8f;
        world = new World(new Vector2(0, gravity), true);
        debugRenderer = new Box2DDebugRenderer();
        batch = new SpriteBatch();
        camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        camera.position.set(0, 0, 0);

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

        bodyDef.position.set(((float)Gdx.graphics.getWidth() / 4) / metersToPixels, 0);
        shipRight = world.createBody(bodyDef);
        shipRight.createFixture(fixtureDef);
        bodyDef.position.set(-((float)Gdx.graphics.getWidth() / 4) / metersToPixels, 0);
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
        bodyDef.position.set(0,((float)Gdx.graphics.getHeight() / 2 / metersToPixels) + 0.5f);
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        Body body = world.createBody(bodyDef);
        fixtureDef.density = 10;

        PolygonShape brickShape = new PolygonShape();

        brickShape.setAsBox(left1/2-.02f, .5f, new Vector2((wallLeft.getPosition().x + (left1/2) + .5f), 0), 0);
        fixtureDef.shape = brickShape;
        body.createFixture(fixtureDef);

        brickShape.setAsBox(left2/2-.02f, .5f, new Vector2((wallCenter.getPosition().x - (left2/2) - 1.5f), 0), 0);
        fixtureDef.shape = brickShape;
        body.createFixture(fixtureDef);

        brickShape.setAsBox(right1/2-.02f, .5f, new Vector2((wallCenter.getPosition().x + (right1/2) + 1.5f), 0), 0);
        fixtureDef.shape = brickShape;
        body.createFixture(fixtureDef);

        brickShape.setAsBox(right2/2-.02f, .5f, new Vector2((wallRight.getPosition().x - (right2/2) - .5f), 0), 0);
        fixtureDef.shape = brickShape;
        body.createFixture(fixtureDef);

        body.setLinearVelocity(0, gravity);
        body.setUserData(new ObjectUserData(left1, left2, right1, right2, setColor()));
    }

    private void changeDirection(){
        Random random = new Random();
        if(left){
            if(pointerLeft == 1){
                if(left1 <= 0.5f){
                    left = false;
                }
                else{
                    left1 -= 0.5f;
                    left2 += 0.5f;
                }
            }
            else if(pointerLeft == 2){
                if(left1 <= 3.5){
                    left = false;
                }
                else{
                    left1 -= 0.5f;
                    left2 += 0.5f;
                }
            }
            else if(pointerLeft == 3){
                if(left2 <= 3.5){
                    left = false;
                }
                else{
                    left2 -= 0.5f;
                    left1 += 0.5f;
                }
            }
            else{
                if(left2 <= 0.5f){
                    left = false;
                }
                else{
                    left2 -= 0.5f;
                    left1 += 0.5f;
                }
            }
        }
        else if(!left){
            pointerLeft = random.nextInt(4)+1;
            left = true;
        }

        if(right){
            if(pointerRight == 1){
                if(right1 <= 0.5f){
                    right = false;
                }
                else{
                    right1 -= 0.5f;
                    right2 += 0.5f;
                }
            }
            else if(pointerRight == 2){
                if(right1 <= 3.5){
                    right = false;
                }
                else{
                    right1 -= 0.5f;
                    right2 += 0.5f;
                }
            }
            else if(pointerRight == 3){
                if(right2 <= 3.5){
                    right = false;
                }
                else{
                    right2 -= 0.5f;
                    right1 += 0.5f;
                }
            }
            else{
                if(right2 <= 0.5f){
                    right = false;
                }
                else{
                    right2 -= 0.5f;
                    right1 += 0.5f;
                }
            }

        }
        else if(!right){
            pointerRight = random.nextInt(4)+1;
            right = true;
        }
    }
    private Texture setColor(){
        Texture t = new Texture(Gdx.files.internal("Red.png"));
        if(colorCounter % 7 == 0){
            t = new Texture(Gdx.files.internal("Red.png"));
        }
        else if(colorCounter % 7 == 1){
            t = new Texture(Gdx.files.internal("Orange.png"));
        }
        else if(colorCounter % 7 == 2){
            t = new Texture(Gdx.files.internal("Yellow.png"));
        }
        else if(colorCounter % 7 == 3){
            t = new Texture(Gdx.files.internal("Green.png"));
        }
        else if(colorCounter % 7 == 4){
            t = new Texture(Gdx.files.internal("Blue.png"));
        }
        else if(colorCounter % 7 == 5){
            t = new Texture(Gdx.files.internal("Purple.png"));
        }
        else if(colorCounter % 7 == 6){
            t = new Texture(Gdx.files.internal("Violet.png"));
        }
            return t;
    }
}
