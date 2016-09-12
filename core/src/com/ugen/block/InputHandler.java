package com.ugen.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by eugen_000 on 9/6/2016.
 */
public class InputHandler implements InputProcessor {
    GameScreen screen;

    public InputHandler(GameScreen screen){
        this.screen = screen;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(!screen.getRenderer().getCurrent().getMoving())
            screen.getRenderer().getCurrent().rotate();
        screen.getRenderer().getCurrent().setMoving(false);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float trueX = screen.getCam().viewportWidth * screenX / Gdx.graphics.getWidth();
        float trueY = screen.getCam().viewportHeight - screen.getCam().viewportHeight * screenY / Gdx.graphics.getHeight();

        screen.getRenderer().getCurrent().setMoving(true);
        if(trueX > screen.getRenderer().getCurrent().getPosition().x)
            if(Math.abs(trueX - screen.getRenderer().getCurrent().getPosition().x) >= screen.getRenderer().getCurrent().getBlockWidth()){
                screen.getRenderer().getCurrent().moveRight();
            }
        if (trueX < screen.getRenderer().getCurrent().getPosition().x){
            if(Math.abs(trueX - screen.getRenderer().getCurrent().getPosition().x) >= screen.getRenderer().getCurrent().getBlockWidth()){
                screen.getRenderer().getCurrent().moveLeft();
            }
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
