package com.ugen.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
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
    private ArrayList<Polyomino> dead;
    private ArrayList<Color> colors;
    private ArrayList<Vector2> positions;
    private ArrayList<Rectangle> tiles;

    private Rectangle gameWindow;

    private long timer;
    private long initTime = System.currentTimeMillis();

    float width, height, minX, maxX;

    Polyomino first;

    public WorldRenderer(GameWorld world){
        this.world = world;

        rand = new Random();

        timer = initTime;

        Polyominoes = world.getCurrentGeneration();
        colors = world.getColors();
        positions = world.getPositions();
        tiles = new ArrayList<Rectangle>();
        dead = new ArrayList<Polyomino>();
        batch = new SpriteBatch();
        shapeBatch = new ShapeRenderer();
        shapeBatch.setAutoShapeType(true);

        cam = new OrthographicCamera(1.0f, (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth());
        viewport = new ExtendViewport(108, 192, cam);
        viewport.apply();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        width = cam.viewportWidth;
        height = cam.viewportHeight;

        gameWindow = new Rectangle(width / 6, 0, 2 * width / 3, 4 * width / 3);


        first = (Polyominoes.get(rand.nextInt(Polyominoes.size())));
        first.setPosition(new Vector2(width / 2, 4 * width / 3));

        for(int i = 0; i < Polyominoes.size(); i++){
            Polyominoes.get(i).setBlockWidth(width / 15);
        }

        for(float i = 0; i < 4 * width / 3; i += first.getBlockWidth()){
            for(float j = 0; j < 2 * width / 3; j += first.getBlockWidth()){
                tiles.add(new Rectangle(width / 6 + j, i, first.getBlockWidth(), first.getBlockWidth()));
            }
        }

        minX = width / 6;
        maxX = width /6 + 2 * width / 3;
    }

    public void render(float delta){

        //BANISHED TO THE SHADOW REALM
        if(dead.size() == 0){
            first.setPosition(new Vector2(-420, -420));

            dead.add(first);

            first = new Polyomino(Polyominoes.get(rand.nextInt(Polyominoes.size())));
            first.setPosition(new Vector2(width / 2, 4 * width / 3));
            Gdx.app.log("DEBUG", "ahhhhhhhhhhhhhhh");
        }

        timer = System.currentTimeMillis() - initTime;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeBatch.begin(ShapeRenderer.ShapeType.Line);
        shapeBatch.setProjectionMatrix(cam.combined);


        if(timer > 300){
            first.moveDown();
            Gdx.app.log("DEBUG", first.getBlocks().size()+"");
            timer = 0;
            initTime = System.currentTimeMillis();
        }

        if(checkCollisions()){
            first.moveUp();
            if(first.getPosition().y >= 4 * width / 3){
               // Gdx.app.();
            }
            dead.add(first);
            first = new Polyomino(Polyominoes.get(rand.nextInt(Polyominoes.size())));
            first.setPosition(new Vector2(width / 2, 4 * width / 3));
        }

        else
            for(int i = 0; i < first.getBlocks().size(); i++){
            if(first.getBlocks().get(i).getY() < 0) {
                first.moveUp();
                dead.add(first);
                first = new Polyomino(Polyominoes.get(rand.nextInt(Polyominoes.size())));
                first.setPosition(new Vector2(width / 2, 4 * width / 3));
                break;
            }
        }

        shapeBatch.setColor(0.25f ,0.25f ,0.25f ,0.25f);
        for(Rectangle rect : tiles){
            shapeBatch.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        }

        for(int i = 0; i < dead.size(); i++)
            dead.get(i).draw(shapeBatch, colors.get(dead.get(i).getColorIndex()));

        first.draw(shapeBatch, colors.get(first.getColorIndex()));
        shapeBatch.end();
    }

    public void drop(){
        boolean b = false;

        while(!checkCollisions()){
            for(int i = 0; i < first.getBlocks().size(); i++) {
                if(!(first.getBlocks().get(i).getY() > 0))
                    b = true;
            }
            if(b)
                break;
            else
                first.moveDown();
        }

        first.moveUp();
        dead.add(first);
        first = new Polyomino(Polyominoes.get(rand.nextInt(Polyominoes.size())));
        first.setPosition(new Vector2(width / 2, 4 * width / 3));
    }

    public void project(Polyomino p){

    }

    public boolean checkCollisions() {
        boolean collided = false;

        for (int i = 0; i < dead.size(); i++) {
            for (int j = 0; j < first.getBlocks().size(); j++) {
                for (int ii = 0; ii < first.getBlocks().size(); ii++) {
                    if (first.getBlocks().get(ii).overlaps(dead.get(i).getBlocks().get(j))) {
                        collided = true;
                        break;
                    }
                }
            }
        }

        return collided;
    }


    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

    public Polyomino getCurrent(){
        return first;
    }

    public OrthographicCamera getCam(){
        return cam;
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }
}
