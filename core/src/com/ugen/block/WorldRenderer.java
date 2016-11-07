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
    Polyomino first;

    private GameWorld world;

    private OrthographicCamera cam;
    private Viewport viewport;
    private ArrayList<Polyomino> Polyominoes;
    private ArrayList<Polyomino> dead;
    private ArrayList<Color> colors;
    private ArrayList<Vector2> positions;
    private ArrayList<ArrayList<Rectangle>> tiles;
    private ArrayList<Rectangle> deadBlocks;
    private ArrayList<PolyominoContainer> containers;
    private Rectangle gameWindow;

    private long timer;
    private long initTime = System.currentTimeMillis();

    float width, height, minX, maxX;

    private int tileNum;


    public WorldRenderer(GameWorld world){
        this.world = world;

        rand = new Random();

        timer = initTime;

        Polyominoes = world.getCurrentGeneration();
        colors = world.getColors();
        //positions = world.getPositions();
        tiles = new ArrayList<ArrayList<Rectangle>>();
        dead = new ArrayList<Polyomino>();
        deadBlocks = new ArrayList<Rectangle>();
        batch = new SpriteBatch();
        shapeBatch = new ShapeRenderer();
        containers = new ArrayList<PolyominoContainer>();

        shapeBatch.setAutoShapeType(true);

        tileNum = Polyominoes.get(0).getCellNum();

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
            Polyominoes.get(i).setBlockWidth(width / (15 + 3 * (tileNum - 3)));
        }

        for(float i = 0; i < 4 * width / 3; i += first.getBlockWidth()){
            tiles.add(new ArrayList<Rectangle>());
            for(float j = 0; j < 2 * width / 3; j += first.getBlockWidth()){
                tiles.get((int)(i / first.getBlockWidth())).add(new Rectangle(width / 6 + j, i, first.getBlockWidth(), first.getBlockWidth()));
            }
        }

        minX = width / 6;
        maxX = width /6 + 2 * width / 3;

        for(int i = 0; i < 5; i++){
            containers.add(new PolyominoContainer());
        }

        for(PolyominoContainer container : containers){
            container.setHeight(width / 6);
            container.setWidth(width / 6);
            container.setPolyomino(new Polyomino(Polyominoes.get(rand.nextInt(Polyominoes.size()))));
        }
    }

    public void render(float delta){

        //BANISHED TO THE SHADOW REALM
        if(dead.size() == 0){
            first.setPosition(new Vector2(-420, -420));

            dead.add(first);

            first = new Polyomino(new Polyomino(Polyominoes.get(rand.nextInt(Polyominoes.size()))));


            first.setPosition(new Vector2(width / 2, 4 * width / 3));
        }

        timer = System.currentTimeMillis() - initTime;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeBatch.begin(ShapeRenderer.ShapeType.Filled);
        shapeBatch.setProjectionMatrix(cam.combined);


        if(timer > 300){
            first.moveDown();

            timer = 0;
            initTime = System.currentTimeMillis();
        }

        if(checkCollisions(first)){
            first.moveUp();
            if(first.getPosition().y >= 4 * width / 3){
               // Gdx.app.();
            }
           killCurrent();
        }

        else
            for(int i = 0; i < first.getBlocks().size(); i++){
                if(first.getBlocks().get(i).getY() < 0) {
                    first.moveUp();
                    killCurrent();
                    break;
                }
            }

        //clearRows();

        for(int i = 0; i < dead.size(); i++)
            dead.get(i).draw(shapeBatch, colors.get(dead.get(i).getColorIndex()));

        first.draw(shapeBatch, colors.get(first.getColorIndex()));

        shapeBatch.end();


        shapeBatch.begin(ShapeRenderer.ShapeType.Line);

        for(int i = 0; i < containers.size(); i++) {
            containers.get(i).draw(shapeBatch, 5 * width / 6, height -  (i + 1) * width / 3, colors.get(containers.get(i).getPolyomino().getColorIndex()));
        }

        shapeBatch.setColor(0.25f ,0.25f ,0.25f ,0.25f);

        for(ArrayList<Rectangle> rectangles : tiles){
            for(Rectangle rect : rectangles) {
                shapeBatch.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            }
        }

        shapeBatch.end();
    }

    public void killCurrent(){
        for(Rectangle rect : first.getBlocks()){
            deadBlocks.add(rect);
        }

        dead.add(first);
        first = new Polyomino(containers.get(0).getPolyomino());

        for(int i = 0; i < containers.size(); i++) {
            if(i == containers.size() - 1){
                containers.get(i).setPolyomino(new Polyomino(Polyominoes.get(rand.nextInt(Polyominoes.size()))));
            }

            else
                 containers.get(i).setPolyomino(new Polyomino(containers.get(i + 1).getPolyomino()));
        }

        first.setPosition(new Vector2(width / 2, 4 * width / 3));
    }

    public void drop(){
        boolean b = false;

        while(!checkCollisions(first)){
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
        first = new Polyomino(containers.get(0).getPolyomino());

        for(int i = 0; i < containers.size(); i++) {
            if(i == containers.size() - 1){
                containers.get(i).setPolyomino(new Polyomino(Polyominoes.get(rand.nextInt(Polyominoes.size()))));
            }

            else
                containers.get(i).setPolyomino(new Polyomino(containers.get(i + 1).getPolyomino()));
        }

        first.setPosition(new Vector2(width / 2, 4 * width / 3));
    }

    public void clearRows(){
        boolean b = true;

        for(int i = 0; i < tiles.size(); i++){
            for(int j = 0; j < tiles.get(i).size(); j++){
                //Gdx.app.log("DEBUG", tiles.get(i).get(j).getX() + "");

                for(int ii = 0; ii < deadBlocks.size(); ii++){
                    if(!((deadBlocks.get(ii).getX() - tiles.get(i).get(j).getX()) < 1)){
                        //Gdx.app.log("DEBUG", "DON'T CLEAR");
                        b = false;
                        break;
                    }
                }

                //Gdx.app.log("DEBUG", b + "");

                if(!b){
                    break;
                }
                else {
                    for(Polyomino p : dead){
                        p.moveDown();
                    }
                }

            }

            b = true;
        }
    }

    public void project(Polyomino p){
        Polyomino temp = new Polyomino(p);
        temp.setPosition(p.getPosition());

        boolean b = false;

        while(!checkCollisions(temp)){
            for(int i = 0; i < temp.getBlocks().size(); i++) {
                if(!(temp.getBlocks().get(i).getY() > 0))
                    b = true;
            }
            if(b)
                break;
            else
                temp.moveDown();
        }

        temp.moveUp();

        shapeBatch.begin(ShapeRenderer.ShapeType.Filled);

        temp.draw(shapeBatch, Color.BLUE);

        shapeBatch.end();
    }

    public boolean checkCollisions(Polyomino p) {
        boolean collided = false;

        for (int i = 0; i < dead.size(); i++) {
            for (int j = 0; j < p.getBlocks().size(); j++) {
                for (int ii = 0; ii < p.getBlocks().size(); ii++) {
                    if (p.getBlocks().get(ii).overlaps(dead.get(i).getBlocks().get(j))) {
                        collided = true;
                        break;
                    }
                }
            }
        }

        return collided;
    }

    public ArrayList<Polyomino> getDead() {
        return dead;
    }

    public void reset(){
        dead.clear();
        deadBlocks.clear();
        Polyominoes = world.getCurrentGeneration();
        first = new Polyomino(Polyominoes.get(rand.nextInt(Polyominoes.size())));
        first.setPosition(new Vector2(width / 2, 4 * width / 3));
    }

    public ArrayList<Rectangle> getDeadBlocks() {
        return deadBlocks;
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
