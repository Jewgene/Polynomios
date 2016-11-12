package com.ugen.block;

import com.badlogic.gdx.Game;

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
