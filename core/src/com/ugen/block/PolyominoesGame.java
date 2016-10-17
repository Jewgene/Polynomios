package com.ugen.block;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
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

import javax.swing.text.html.Option;

public class PolyominoesGame extends Game {
	GameScreen gameScreen;
	MainMenu mainMenu;
	OptionsScreen optionsScreen;


	@Override
	public void create () {
		AssetManager.load();

		gameScreen = new GameScreen(this);
		mainMenu = new MainMenu(this);
		optionsScreen = new OptionsScreen(this);
		setScreen(mainMenu);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}

	public GameScreen getGameScreen(){
		return gameScreen;
	}

	public void changeScreen(int i){
		switch(i){
			case 0:
				setScreen(mainMenu);
				break;
			case 1:
				setScreen(gameScreen);
				break;
			case 2:
				setScreen(optionsScreen);
				break;
		}
	}
}
