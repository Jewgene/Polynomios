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
    private ArrayList<Rectangle> blocks;
    private Matrix grid;
    private ArrayList<Vector2> adjacents;
    private ArrayList<Polyomino> children;

    private int cellNum;

    public Polyomino(int cellNum){
        grid = new Matrix(cellNum, cellNum);
        children = new ArrayList<Polyomino>();
        adjacents = new ArrayList<Vector2>();

        this.cellNum = cellNum;
    }

    public Polyomino(){
        grid = new Matrix(2, 2);
        children = new ArrayList<Polyomino>();
        adjacents = new ArrayList<Vector2>();
        this.cellNum = getGrid().getRows();
        grid.put(1, 0, 0);
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

    public void draw(ShapeRenderer sr, Color color, int posx, int posy){
        sr.setColor(color);
        for(int i = 0; i < grid.getRows(); i++){
            for(int j = 0; j < grid.getRows(); j++){
               //dx.app.log("DEBUG", grid.getData()[i][j] + "");

                if(grid.getData()[i][j] == 1){
                    sr.rect(posx + j * 20,  posy + i * 20 , 20, 20);
                }
            }
        }
    }

    public void massAbortion(){
        //Kill the autistic babies
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

        Gdx.app.log("ASDFAFASDFASDFASDFASDF", adjacents.size() + "");
    }

    public Matrix getGrid(){
        return grid;
    }
}
