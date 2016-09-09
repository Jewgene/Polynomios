package com.ugen.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by eugen_000 on 9/3/2016.
 */
public class GameWorld {
    final PolyominoesGame theGame;

    WorldRenderer renderer;

    Polyomino first;

    private ArrayList<Polyomino> currentGeneration;
    private ArrayList<Polyomino> nextGeneration;
    private ArrayList<Color> colors;
    private ArrayList<Vector2> positions;

    float red;
    float green;
    float blue;

    int posx = 50, posy = 200;

    int generationCap = 4;

    public GameWorld(PolyominoesGame game){
        this.theGame = game;

        currentGeneration = new ArrayList<Polyomino>();
        nextGeneration = new ArrayList<Polyomino>();
        colors = new ArrayList<Color>();
        positions = new ArrayList<Vector2>();
        first = new Polyomino();

        currentGeneration.add(first);

        for(int ii = 0; ii < generationCap; ii++) {
            for (int i = 0; i < currentGeneration.size(); i++){
                currentGeneration.get(i).rotate();
                currentGeneration.get(i).giveBirth();
                for(int j = 0; j < currentGeneration.get(i).getChildren().size(); j++)
                    nextGeneration.add(currentGeneration.get(i).getChildren().get(j));
            }

            currentGeneration.clear();
            for(int i = 0; i < nextGeneration.size(); i++)
                currentGeneration.add(nextGeneration.get(i));
            nextGeneration.clear();
        }
        removeDuplicates(currentGeneration);

        for(int i = 0; i < currentGeneration.size(); i++){
            red = (float) Math.sin((float)i * 6 / currentGeneration.size()) * 0.5f + 0.5f;
            green = (float) Math.sin((float)i * 6 / currentGeneration.size() + 1) * 0.5f + 0.5f;
            blue = (float) Math.sin((float)i * 6 /  currentGeneration.size() + 3) * 0.5f + 0.5f;


            colors.add(new Color(red, green, blue, 1.0f));
        }

        for(int i = 0; i < currentGeneration.size(); i++){

            if(posx > 880){
                posx = 50;
                posy += 250;
            }

            positions.add(new Vector2(posx, posy));
            posx += 250;
        }
    }

    public void removeDuplicates(ArrayList<Polyomino> children){
        for(int i = 0; i < children.size(); i++){
            for(int j = 0; j < 4; j++){
                for(int ii = children.size() - 1; ii > i; ii--){
                    if(Arrays.deepEquals(children.get(i).getGrid().getData(), children.get(ii).getGrid().getData()) & i != ii)
                        children.remove(ii);
                }
                children.get(i).rotate();
            }
        }
    }

    public void update(float delta){

    }

    public ArrayList<Polyomino> getCurrentGeneration() {
        return currentGeneration;
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

    public ArrayList<Vector2> getPositions() {
        return positions;
    }

    public void setRenderer(WorldRenderer renderer) {
        this.renderer = renderer;
    }
}
