package com.JaeLee.splitdecision.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.scenes.scene2d.ui.TextButton.*;

public class MainMenu implements Screen{

    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private BitmapFont white;
    private Label title;
    private Table table;
    private TextButton game, exit, instructions;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(new StretchViewport( Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stage.getViewport().update( Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        table.invalidateHierarchy();
    }

    @Override
    public void show() {
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas(Gdx.files.internal("MainButton.pack"));
        skin = new Skin(atlas);

        table = new Table(skin);
        table.setFillParent(true);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        white = new BitmapFont(Gdx.files.internal("font/mainFont.fnt"));

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("MainButton.up");
        textButtonStyle.down = skin.getDrawable("MainButton.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = white;

        exit = new TextButton("EXIT", textButtonStyle);
        exit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        exit.pad(15);

        game = new TextButton("GAME", textButtonStyle);
        game.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen());
            }
        });
        game.pad(15);

        instructions = new TextButton("INSTRUCTIONS", textButtonStyle);
        instructions.pad(15);
        //TODO give function to instructions button

        title = new Label("Split Decision", new Label.LabelStyle(white, Color.WHITE));

        Gdx.input.setInputProcessor(stage);
        table.add(title);
        table.getCell(title).spaceBottom(100);
        table.row();
        table.add(game);
        table.getCell(game).spaceBottom(100);
        table.row();
        table.add(instructions);
        table.getCell(instructions).spaceBottom(100);
        table.row();
        table.add(exit);
        stage.addActor(table);
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
        stage.dispose();
        white.dispose();
        skin.dispose();
        atlas.dispose();
    }
}
