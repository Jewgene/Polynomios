package com.ugen.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by eugen_000 on 9/3/2016.
 */
public class Polyomino {

    float blockWidth;
    private boolean moving;

    private ArrayList<Rectangle> blocks;
    private Matrix grid;
    private ArrayList<Vector2> adjacents;
    private ArrayList<Polyomino> children;
    private Vector2 position;

    private int cellNum;


    public Polyomino(int cellNum){
        grid = new Matrix(cellNum, cellNum);
        blocks = new ArrayList<Rectangle>();
        children = new ArrayList<Polyomino>();
        adjacents = new ArrayList<Vector2>();
        position = new Vector2();

        for(int i = 0; i < grid.getRows(); i++){
            blocks.add(new Rectangle());
        }

        this.cellNum = cellNum;
    }

    public Polyomino(){
        grid = new Matrix(2, 2);
        blocks = new ArrayList<Rectangle>();
        children = new ArrayList<Polyomino>();
        adjacents = new ArrayList<Vector2>();
        position = new Vector2();
        this.cellNum = getGrid().getRows();
        grid.put(1, 0, 0);

        for(int i = 0; i < grid.getRows(); i++){
            blocks.add(new Rectangle());
        }
    }

    public void giveBirth(){
        calculateAdjacents();

        for(int i = 0; i < adjacents.size(); i++){
            Polyomino child = new Polyomino(cellNum + 1);

            for(int j = 0; j < cellNum; j++){
                for(int a = 0; a < cellNum; a++){
                   // Gdx.app.log("DEBUG", grid.getData()[i][j] + "");

                    if(grid.getData()[j][a] == 1)
                        child.getGrid().put(1, j, a);
                }
            }
           // Gdx.app.log("DEBUG", (int)adjacents.get(i).x + " , " + adjacents.get(i).y + " AHHHHHHHHHHHHHHHHHH");

            child.grid.put(1, (int)adjacents.get(i).x, (int)adjacents.get(i).y);

            children.add(child);
        }
    }

    public void rotate(){
        this.grid = (grid.rotateClockwise());
    }

    public void draw(ShapeRenderer sr, Color color){
        sr.setColor(color);
        for(int i = 0; i < grid.getRows(); i++){
            for(int j = 0; j < grid.getRows(); j++){
               //dx.app.log("DEBUG", grid.getData()[i][j] + "");

                if(grid.getData()[i][j] == 1){
                    sr.rect(position.x + j * blockWidth,  position.y - (i+1) * blockWidth , blockWidth, blockWidth);
                }
            }
        }
    }

    public int getCellNum(){
        return cellNum;
    }

    public ArrayList<Polyomino> getChildren(){
        return children;
    }

    public void calculateAdjacents(){
        for(int i = 0; i < cellNum; i++){
            for(int j = 0; j < cellNum; j++){
                if(grid.getData()[i][j] == 1){
                    if(j-1 >= 0 && grid.getData()[i][j-1] == 0)
                        this.adjacents.add(new Vector2(i, j-1));
                    if(j+1 < grid.getRows() && grid.getData()[i][j+1] == 0)
                        this.adjacents.add(new Vector2(i, j+1));
                    if(i+1 < grid.getRows() && grid.getData()[i+1][j] == 0)
                        this.adjacents.add(new Vector2(i+1, j));
                    if(i-1 >= 0 && grid.getData()[i-1][j] == 0)
                        this.adjacents.add(new Vector2(i-1, j));
                }
            }
        }
    }

    public Matrix getGrid(){
        return grid;
    }

    public void setGrid(Matrix grid){
        this.grid = grid;
    }

    public <T> boolean contains(T[] array, T toContain){
        boolean b = false;

        for(int i = 0; i < array.length; i++)
            if (array[i] == toContain) {
                b = true;
                break;
            }

        return b;
    }

    public void setPosition(Vector2 position){
        int rectNum = 0;
        this.position = position;
        for(int i = 0; i < grid.getRows(); i++){
            for(int j = 0; j < grid.getRows(); j++){
                if(grid.getData()[i][j] == 1) {
                    blocks.set(rectNum, new Rectangle(position.x + blockWidth * j, position.y - blockWidth * (i + 1), blockWidth, blockWidth));
                    rectNum++;
                }

            }
        }


    }

    public void moveDown(){
        int rectNum = 0;
        position.y -= blockWidth;
        for(int i = 0; i < grid.getRows(); i++){
            for(int j = 0; j < grid.getRows(); j++){
                if(grid.getData()[i][j] == 1) {
                    blocks.set(rectNum, new Rectangle(position.x + blockWidth * j, position.y - blockWidth * (i + 1), blockWidth, blockWidth));
                    rectNum++;
                }
            }
        }
    }

    public void moveUp(){
        int rectNum = 0;
        position.y += blockWidth;
        for(int i = 0; i < grid.getRows(); i++){
            for(int j = 0; j < grid.getRows(); j++){
                if(grid.getData()[i][j] == 1) {
                    blocks.set(rectNum, new Rectangle(position.x + blockWidth * j, position.y - blockWidth * (i + 1), blockWidth, blockWidth));
                    rectNum++;
                }
            }
        }
    }

    public void moveRight(){
        int rectNum = 0;

        position.x += blockWidth;
        for(int i = 0; i < grid.getRows(); i++){
            for(int j = 0; j < grid.getRows(); j++){
                if(grid.getData()[i][j] == 1) {
                    blocks.set(rectNum, new Rectangle(position.x + blockWidth * j, position.y - blockWidth * (i + 1), blockWidth, blockWidth));
                    rectNum++;
                }
            }
        }
    }

    public void moveLeft(){
        int rectNum = 0;

        position.x -= blockWidth;
        for(int i = 0; i < grid.getRows(); i++){
            for(int j = 0; j < grid.getRows(); j++){
                if(grid.getData()[i][j] == 1) {
                    blocks.set(rectNum, new Rectangle(position.x + blockWidth * j, position.y - blockWidth * (i + 1), blockWidth, blockWidth));
                    rectNum++;
                }
            }
        }
    }

    public Vector2 getPosition(){
        return position;
    }

    public void setBlockWidth(float blockWidth){
        this.blockWidth = blockWidth;
    }

    public float getBlockWidth(){
        return blockWidth;
    }

    public ArrayList<Rectangle> getBlocks() {
        return blocks;
    }

    public void setMoving(boolean moving){
        this.moving = moving;
    }

    public boolean getMoving(){
        return moving;
    }
}


