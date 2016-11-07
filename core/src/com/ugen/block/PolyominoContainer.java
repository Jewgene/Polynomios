package com.ugen.block;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by WilsonCS30 on 11/7/2016.
 */

public class PolyominoContainer {

    Polyomino p;
    float width, height;

    public PolyominoContainer(){

    }

    public void draw(ShapeRenderer sr, float posX, float posY, Color color){
       // sr.rect(posX, posY, width, -height);

        p.setBlockWidth(p.getBlockWidth());
        p.setPosition(new Vector2(posX, posY - p.getBlockWidth()));
        p.draw(sr, color);
    }

    public void setHeight(float height){
        this.height = height;
    }

    public void setWidth(float width){
        this.width = width;
    }

    public void setPolyomino(Polyomino p){
        this.p = new Polyomino(p);
    }

    public Polyomino getPolyomino(){
        return p;
    }
}
