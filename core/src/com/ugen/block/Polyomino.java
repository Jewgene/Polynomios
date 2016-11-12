package com.ugen.block;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by eugen_000 on 9/3/2016.
 */
public class Polyomino {

    float blockWidth;
    private boolean moving;
    private int colorIndex;

    private ArrayList<Rectangle> blocks;
    private Matrix grid;
    private ArrayList<Vector2> adjacents;
    private ArrayList<Polyomino> children;
    private Vector2 position;

    private int cellNum;

    public Polyomino(Polyomino old){
        this.colorIndex = old.getColorIndex();
        this.blockWidth = old.getBlockWidth();
        //this.blocks = old.getBlocks();
        this.grid = old.getGrid();
        this.adjacents = old.getAdjacents();
        this.children = old.getChildren();
        this.cellNum = old.getCellNum();

        blocks = new ArrayList<Rectangle>();

        for(int i = 0; i < cellNum - 1; i++){
            blocks.add(new Rectangle());
        }
        //this.position = old.getPosition();
    }

    public Polyomino(int cellNum){
        grid = new Matrix(cellNum, cellNum);
        blocks = new ArrayList<Rectangle>();
        children = new ArrayList<Polyomino>();
        adjacents = new ArrayList<Vector2>();
        position = new Vector2();

        for(int i = 0; i < grid.getRows() - 1; i++){
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

        for(int i = 0; i < grid.getRows() - 1; i++){
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

        for(int i = 0; i < children.size(); i++){
            for(int j = 0; j < children.size(); j++){
                if(j != i){
                    if(Arrays.deepEquals(children.get(i).getGrid().getData(), children.get(j).getGrid().getData())){
                        children.remove(j);
                    }
                }
            }
        }
    }

    public void rotate(){
        int rectNum = 0;
        this.grid = (grid.rotateClockwise());
        //grid = grid.rotateClockwise();

        for(int i = 0; i < grid.getRows(); i++){
            for(int j = 0; j < grid.getRows(); j++){
                if(grid.getData()[i][j] == 1) {
                    blocks.set(rectNum, new Rectangle(position.x + blockWidth * j + .25f, position.y - blockWidth * (i) + .25f, blockWidth - .5f, blockWidth - .5f));
                    rectNum++;
                }
            }
        }
    }

    public void reverseRotate(){
        int rectNum = 0;
        this.grid = (grid.rotateCounterClockwise());
        //grid = grid.rotateClockwise();

        for(int i = 0; i < grid.getRows(); i++){
            for(int j = 0; j < grid.getRows(); j++){
                if(grid.getData()[i][j] == 1) {
                    blocks.set(rectNum, new Rectangle(position.x + blockWidth * j + .25f, position.y - blockWidth * (i) + .25f, blockWidth - .5f, blockWidth - .5f));
                    rectNum++;
                }
            }
        }
    }

    public void draw(ShapeRenderer sr, Color color){
        sr.setColor(color);

        for(Rectangle rect : blocks){
            sr.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        }
        /*for(int i = 0; i < grid.getRows(); i++){
            for(int j = 0; j < grid.getRows(); j++){
               //dx.app.log("DEBUG", grid.getData()[i][j] + "");

                if(grid.getData()[i][j] == 1){
                    sr.rect(position.x + j * blockWidth + .25f,  position.y - (i) * blockWidth + .25f, blockWidth - .5f, blockWidth - .5f);
                }
            }
        }*/
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
                    blocks.set(rectNum, new Rectangle(position.x + blockWidth * j + .25f, position.y - blockWidth * (i) + .25f, blockWidth - .5f, blockWidth - .5f));
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
                    blocks.set(rectNum, new Rectangle(position.x + blockWidth * j + .25f, position.y - blockWidth * (i) + .25f, blockWidth - .5f, blockWidth - .5f));
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
                    blocks.set(rectNum, new Rectangle(position.x + blockWidth * j + .25f, position.y - blockWidth * (i) + .25f, blockWidth - .5f, blockWidth - .5f));
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
                    blocks.set(rectNum, new Rectangle(position.x + blockWidth * j + .25f, position.y - blockWidth * (i) + .25f, blockWidth - .5f, blockWidth - .5f));
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
                    blocks.set(rectNum, new Rectangle(position.x + blockWidth * j + .25f, position.y - blockWidth * (i) + .25f, blockWidth - .5f, blockWidth - .5f));
                    rectNum++;
                }
            }
        }
    }

    public Vector2 getPosition(){
        return position;
    }

    public ArrayList<Vector2> getAdjacents() {
        return adjacents;
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

    public int getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }
}


