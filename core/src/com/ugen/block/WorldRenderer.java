package com.ugen.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;
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
    private Matrix gameWindow;

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


        first = (Polyominoes.get(rand.nextInt(Polyominoes.size())));
        first.setPosition(new Vector2(width / 2, 4 * width / 3));

        for(int i = 0; i < Polyominoes.size(); i++){
            Polyominoes.get(i).setBlockWidth(width / (9 + 3 * (tileNum - 3)));
        }

        for(float i = 0; i < 4 * width / 3; i += first.getBlockWidth()){
            tiles.add(new ArrayList<Rectangle>());
            for(float j = 0; j < 2 * width / 3; j += first.getBlockWidth()){
                tiles.get((int)(i / first.getBlockWidth())).add(new Rectangle(width / 6 + j, i, first.getBlockWidth(), first.getBlockWidth()));
            }
        }

        gameWindow = new Matrix(tiles.size(), tiles.get(0).size());

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


        for(int i = 0; i < dead.size(); i++)
            dead.get(i).draw(shapeBatch, colors.get(dead.get(i).getColorIndex()));

        first.draw(shapeBatch, colors.get(first.getColorIndex()));

        shapeBatch.end();


        shapeBatch.begin(ShapeRenderer.ShapeType.Filled);

        for(int i = 0; i < containers.size(); i++) {
            containers.get(i).draw(shapeBatch, 5 * width / 6, height -  (i + 2) * width / 3, colors.get(containers.get(i).getPolyomino().getColorIndex()));
        }

        shapeBatch.end();

        shapeBatch.begin(ShapeRenderer.ShapeType.Line);
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
            //Gdx.app.log("DEBUG", "" + (int)(rect.getY() / first.getBlockWidth()) + " , " + (int)((rect.getX() - width / 6) / first.getBlockWidth()));
            gameWindow.put(1, gameWindow.getRows() - 1 - (int)(rect.getY() / first.getBlockWidth()), (int)((rect.getX() - width / 6) / first.getBlockWidth()));
            //Gdx.app.log("DEBUG", "" + gameWindow.getData()[(int)(rect.getY() / first.getBlockWidth())][(int)((rect.getX() - width / 6) / first.getBlockWidth())]);
        }

        gameWindow.show();

        dead.add(first);

        clearRows();

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

        for(Rectangle rect : first.getBlocks()){
            deadBlocks.add(rect);

            gameWindow.put(1, gameWindow.getRows() - 1 - (int)(rect.getY() / first.getBlockWidth()), (int)((rect.getX() - width / 6) / first.getBlockWidth()));
        }

        //gameWindow.show();

        dead.add(first);

        clearRows();

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

        for(int i = 0; i < gameWindow.getRows(); i++){
            for(int j = 0; j < gameWindow.getData()[i].length - 1; j++){
                if(gameWindow.getData()[i][j] == 0){
                    b = false;
                }

                if(!b){
                    break;
                }
            }

            if(b){
                for(Polyomino p : dead) {

                    for(Iterator<Rectangle> iter = p.getBlocks().iterator(); iter.hasNext();) {
                        Rectangle rect = iter.next();

                        if (gameWindow.getRows() - 1 - (int) (rect.getY() / rect.getWidth()) == i) {
                            iter.remove();
                            deadBlocks.remove(deadBlocks.indexOf(rect));
                        }
                    }
                }


                for(Rectangle rect : deadBlocks){
                    if(rect.getY() > (gameWindow.getRows() - i) * first.getBlockWidth()){
                        rect.setPosition(rect.getX(), rect.getY() - rect.getWidth() - .5f);
                    }
                }

                for(int j = 0; j < gameWindow.getRows(); j++){
                    for(int a = 0; a < gameWindow.getData()[j].length; a++){
                        gameWindow.getData()[j][a] = 0;
                    }
                }

                for(Rectangle rect : deadBlocks){
                    gameWindow.put(1, gameWindow.getRows() - 1 - (int)(rect.getY() / first.getBlockWidth()), (int)((rect.getX() - width / 6) / first.getBlockWidth()));
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
            for (int j = 0; j < dead.get(i).getBlocks().size(); j++) {
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
