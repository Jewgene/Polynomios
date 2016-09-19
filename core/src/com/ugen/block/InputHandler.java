package com.ugen.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by eugen_000 on 9/6/2016.
 */
public class InputHandler implements InputProcessor {
    GameScreen screen;
    float initX, initY;

    private long time;
    private boolean thing = false;

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

        if(!thing)
            thing = true;

        if(System.currentTimeMillis() - time < 200 && thing) {
            thing = false;
            screen.getRenderer().getCurrent().rotate();

            for(int i = 0; i < screen.getRenderer().getCurrent().getBlocks().size(); i++){
                if(screen.getRenderer().getCurrent().getBlocks().get(i).getX() > screen.getRenderer().getMaxX())
                    screen.getRenderer().getCurrent().reverseRotate();
            }
        }

        initX = screen.getCam().viewportWidth * screenX / Gdx.graphics.getWidth();
       // initY = screen.getCam().viewportHeight - screen.getCam().viewportHeight * screenY / Gdx.graphics.getHeight();
        time = System.currentTimeMillis();

        if(screen.getRenderer().checkCollisions())
            screen.getRenderer().getCurrent().reverseRotate();

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float trueX = screen.getCam().viewportWidth * screenX / Gdx.graphics.getWidth();
       // float trueY = screen.getCam().viewportHeight - screen.getCam().viewportHeight * screenY / Gdx.graphics.getHeight();

        if(trueX > initX) {
            if (Math.abs(trueX - initX) >= screen.getRenderer().getCurrent().getBlockWidth()) {
                screen.getRenderer().getCurrent().moveRight();
                for(int i = 0; i < screen.getRenderer().getCurrent().getBlocks().size(); i++) {
                    if (screen.getRenderer().getCurrent().getBlocks().get(i).getX() > screen.getRenderer().getMaxX() || screen.getRenderer().checkCollisions())
                        screen.getRenderer().getCurrent().moveLeft();
                }
                initX = trueX;
            }
        }

        if(trueX < initX){
            if(Math.abs(trueX - initX) >= screen.getRenderer().getCurrent().getBlockWidth()){
                if(!(screen.getRenderer().getCurrent().getPosition().x - screen.getRenderer().getMinX() < .5f))
                    screen.getRenderer().getCurrent().moveLeft();

                else
                    for(int i = 0; i < screen.getRenderer().getCurrent().getBlocks().size(); i++) {
                    if (screen.getRenderer().getCurrent().getBlocks().get(i).getX() < screen.getRenderer().getMinX() - screen.getRenderer().getCurrent().getBlockWidth() || screen.getRenderer().checkCollisions())
                        screen.getRenderer().getCurrent().moveRight();
                }
                initX = trueX;
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
