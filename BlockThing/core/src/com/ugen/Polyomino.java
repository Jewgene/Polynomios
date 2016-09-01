package com.ugen;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by WilsonCS30 on 9/1/2016.
 */
public class Polyomino {

    private ArrayList<Rectangle> blocks;
    private Vector position;

    public Polyomino(int n){
        for(int i = 0; i < n; i++)
            blocks.add(new Rectangle());
    }

    public void generateBlock(int blockIndex){

    }

    public void move(Vector2 deltaPos){
        for(int i = 0; i < blocks.size(); i++){
            blocks.get(i).setPosition(blocks.get(i).getX() + deltaPos.x, blocks.get(i).getY() + deltaPos.y);
        }
    }

    public void setPosition(Vector2 newPos){
        for(int i = 0; i < blocks.size(); i++){
            blocks.get(i).setPosition(newPos);
        }
    }

}
