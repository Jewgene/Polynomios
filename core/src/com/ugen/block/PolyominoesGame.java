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

public class PolyominoesGame extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer renderer;
	OrthographicCamera cam;
	Polyomino first;

	private ArrayList<Polyomino> currentGeneration;
	private ArrayList<Polyomino> nextGeneration;

	private Color temp;
	int generationCap = 2;

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
				currentGeneration.get(i).giveBirth();
				for(int j = 0; j < currentGeneration.get(i).getChildren().size(); j++)
					nextGeneration.add(currentGeneration.get(i).getChildren().get(j));
			}

			currentGeneration.clear();
			for(int i = 0; i < nextGeneration.size(); i++)
				currentGeneration.add(nextGeneration.get(i));
			nextGeneration.clear();
		}

		temp = new Color(0, 0, 1.0f, 1.0f);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		renderer.begin();
		renderer.set(ShapeRenderer.ShapeType.Filled);

		for(int ii = 0 ; ii < currentGeneration.size(); ii++) {
			currentGeneration.get(ii).draw(renderer, temp, ii * 80, 200);
		}

		renderer.end();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
