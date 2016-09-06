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
			currentGeneration.get(ii).draw(renderer, temp, ii * 60, 200);
		}


		renderer.end();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
