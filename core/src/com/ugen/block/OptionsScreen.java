package com.ugen.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;

/**
 * Created by WilsonCS30 on 10/17/2016.
 */

public class OptionsScreen extends ScreenAdapter{

    private Stage stage;
    private Table container, table;
    Skin skin;
    Skin toggleSkin;

    int degrees[] = {3, 4, 5, 6, 7, 8};
    ScrollPane scroll;

    private ArrayList<TextButton> buttons;

    PolyominoesGame game;

    public OptionsScreen(PolyominoesGame game){
        this.game = game;
        create();
    }

    public void create(){
        stage = new Stage(new ExtendViewport(216, 394)){
            @Override
            public boolean keyDown(int keyCode){
                if(keyCode == Input.Keys.BACK)
                    game.changeScreen(0);

                return true;
            }
        };

        skin = AssetManager.getButtonSkin();
        toggleSkin = AssetManager.getToggleButtonSkin();

        buttons = new ArrayList<TextButton>();

        container = new Table();
        container.setFillParent(true);
        stage.addActor(container);


        Table table = new Table();

        scroll = new ScrollPane(table);

        for(final int i : degrees){
            final TextButton button = new TextButton(i + "", toggleSkin);

            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //game.getGameScreen().getWorld().getPrefs().putInteger("degree", i);
                    game.getGameScreen().setGenerationCap(i - 1);
                }
            });

            buttons.add(button);
            table.add(button).minSize(30).space(2).expandX();
            table.row();
        }

        container.add(scroll);
        container.row();

        TextButton x = new TextButton("Back", skin);
        x.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(0);
            }
        });
        container.add(x).minSize(40).space(2).expandX().center();
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
