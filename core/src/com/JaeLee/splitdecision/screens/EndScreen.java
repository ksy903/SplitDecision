package com.JaeLee.splitdecision.screens;

import com.JaeLee.splitdecision.debug.InputController;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class EndScreen implements Screen {

    private Label title;
    private BitmapFont white;
    private Table table;
    private Stage stage;

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
        table = new Table();
        table.setFillParent(true);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        white = new BitmapFont(Gdx.files.internal("font/mainFont.fnt"));
        System.out.println(GameMode.bestCount);
        title = new Label("Best Score: "+GameMode.bestCount, new Label.LabelStyle(white, Color.WHITE));
        table.add(title);
        stage.addActor(table);

        Gdx.input.setInputProcessor(new InputController() {
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
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
        white.dispose();
    }
}
