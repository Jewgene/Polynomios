package com.ugen.block;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;

 /* Created by eugen_000 on 9/3/2016.
 */

public class GameScreen extends ScreenAdapter{

    InputHandler handler;

    private PolyominoesGame game;

    private GameWorld world;
    private WorldRenderer renderer;

    public GameScreen (PolyominoesGame game){
        this.game = game;

        world = new GameWorld(game);
        renderer = new WorldRenderer(world);
        world.setRenderer(renderer);
        handler = new InputHandler(this);

        Gdx.input.setInputProcessor(handler);
    }

    public void update(float delta){
        world.update(delta);
    }

    public void draw(float delta){
        renderer.render(delta);
    }

    @Override
    public void render(float delta){
        update(delta);
        draw(delta);
    }

    @Override
    public void pause(){
        super.pause();
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(handler);
    }

    @Override
    public void resume(){
        Gdx.input.setInputProcessor(handler);
    }

    @Override
    public void dispose(){
        super.dispose();
    }

    public GameWorld getWorld() {
        return world;
    }

    public PolyominoesGame getGame() {
        return game;
    }

    public WorldRenderer getRenderer() {
        return renderer;
    }

    public OrthographicCamera getCam(){
        return renderer.getCam();
    }


}
