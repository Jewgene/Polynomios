package com.ugen.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * Created by WilsonCS30 on 10/17/2016.
 */

public class MainMenu extends ScreenAdapter {

    private Stage stage;
    private Table table;
    Skin skin;

    PolyominoesGame game;

    public MainMenu(PolyominoesGame game){
        this.game = game;
        create();
    }

    public void create(){
        stage = new Stage(new ExtendViewport(216, 384)){
            @Override
            public boolean keyDown(int keyCode){
                if(keyCode == Input.Keys.BACK)
                    Gdx.app.exit();

                return true;
            }
        };

        skin = AssetManager.getButtonSkin();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton startButton = new TextButton("Play", skin);

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(1);
            }
        });

        table.add(startButton).minSize(30).space(2).expandX();
        table.row();

        TextButton optionsButton = new TextButton("Options", skin);

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(2);
            }
        });

        table.add(optionsButton).minSize(30).space(2).expandX();
        table.row();

        TextButton exitButton = new TextButton("Quit", skin);

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(1);
            }
        });

        table.add(exitButton).minSize(30).space(2).expandX();
        table.row();
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose(){
        stage.dispose();
    }
}
