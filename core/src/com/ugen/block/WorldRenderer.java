package com.ugen.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by eugen_000 on 9/3/2016.
 */
public class WorldRenderer {

    SpriteBatch batch;
    ShapeRenderer shapeBatch;

    Random rand;

    private GameWorld world;

    private OrthographicCamera cam;
    private Viewport viewport;
    private ArrayList<Polyomino> Polyominoes;
    private ArrayList<Color> colors;
    private ArrayList<Vector2> positions;

    private long timer;
    private long initTime = System.currentTimeMillis();

    float width, height;

    Polyomino first;

    public WorldRenderer(GameWorld world){
        this.world = world;

        rand = new Random();

        timer = initTime;

        Polyominoes = world.getCurrentGeneration();
        colors = world.getColors();
        positions = world.getPositions();

        batch = new SpriteBatch();
        shapeBatch = new ShapeRenderer();
        shapeBatch.setAutoShapeType(true);

        cam = new OrthographicCamera(1.0f, (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth());
        viewport = new ExtendViewport(108, 192, cam);
        viewport.apply();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        width = cam.viewportWidth;
        height = cam.viewportHeight;

        first = Polyominoes.get(rand.nextInt(Polyominoes.size()));
        first.setPosition(new Vector2(width / 2, height));

        for(int i = 0; i < Polyominoes.size(); i++){
            Polyominoes.get(i).setBlockWidth((int)width / 10);
        }
    }

    public void render(float delta){
        timer = System.currentTimeMillis() - initTime;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeBatch.begin(ShapeRenderer.ShapeType.Line);
        shapeBatch.setProjectionMatrix(cam.combined);

        first.draw(shapeBatch, colors.get(Polyominoes.indexOf(first)), (int)first.getPosition().x, (int)first.getPosition().y);
        if(timer > 500){
            first.moveDown();
            timer = 0;
            initTime = System.currentTimeMillis();
        }

        if(first.getPosition().y < 0){
            first = Polyominoes.get(rand.nextInt(Polyominoes.size()));
            first.setPosition(new Vector2(width / 2, height));
        }

        shapeBatch.end();
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }
}
