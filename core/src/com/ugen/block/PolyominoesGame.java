package com.ugen.block;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.ArrowShapeBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sun.javafx.scene.control.skin.VirtualFlow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class PolyominoesGame extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer renderer;
	OrthographicCamera cam;
	Polyomino first;
	long initTime = System.currentTimeMillis();
	long timeElapsed = initTime;

	private ArrayList<Polyomino> currentGeneration;
	private ArrayList<Polyomino> nextGeneration;
	private ArrayList<int[][]> childrenData;
	private ArrayList<Color> colors;
	private ArrayList<Vector2> positions;

	float red;
	float green;
	float blue;

	int posx = 0, posy = 200;

	private Color temp;
	int generationCap = 4;

	@Override
	public void create () {
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();

		cam = new OrthographicCamera(1.0f, (float)(Gdx.graphics.getWidth()/ Gdx.graphics.getHeight()));
		renderer.setAutoShapeType(true);

		currentGeneration = new ArrayList<Polyomino>();
		nextGeneration = new ArrayList<Polyomino>();
		colors = new ArrayList<Color>();
		positions = new ArrayList<Vector2>();
		first = new Polyomino();

		first.giveBirth();
		currentGeneration = first.getChildren();

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

		//Collections.shuffle(currentGeneration, new Random());
		removeDuplicates(currentGeneration);

		Gdx.app.log("ASDFASDFASDFASDF", currentGeneration.size() + "");
		temp = new Color(0, 0, 1.0f, 1.0f);

		for(int i = 0; i < currentGeneration.size(); i++){
			red = (float) Math.sin(i / (currentGeneration.size()/7)) * 0.5f + 0.5f;
			green = (float) Math.sin(i / (currentGeneration.size()/7) + 3*Math.PI/4) * 0.5f + 0.5f;
			blue = (float) Math.sin(i / (currentGeneration.size()/7) + 3*Math.PI/2) * 0.5f + 0.5f;

			colors.add(new Color(red, green, blue, 1.0f));
		}

		for(int i = 0; i < currentGeneration.size(); i++){

			if(posx > 880){
				posx = 0;
				posy += 200;
			}

			positions.add(new Vector2(posx, posy));
			posx += 80;
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

	@Override
	public void render () {
		timeElapsed = System.currentTimeMillis() - initTime;

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		renderer.begin();
		renderer.set(ShapeRenderer.ShapeType.Filled);

		for(int ii = 0 ; ii < currentGeneration.size(); ii++) {
			currentGeneration.get(ii).draw(renderer, colors.get(ii), (int)positions.get(ii).x, (int)positions.get(ii).y);
		}


		renderer.end();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
