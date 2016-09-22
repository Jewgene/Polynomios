package com.ugen.block;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by WilsonCS30 on 9/16/2016.
 */
public class ButtonSkin extends Skin{

    public ButtonSkin(Color neutral, Color pressed) {
        //super();

        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        add("white", new Texture(pixmap));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = newDrawable("white", neutral);
        style.checked = newDrawable("white", neutral);
        style.down = newDrawable("white", pressed);
        BitmapFont font = new BitmapFont();
        font.getData().setScale(.5f);
        style.font = font;
        add("default", style);

    }

    public ButtonSkin() {
        //super();

        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        add("white", new Texture(pixmap));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = newDrawable("white", Color.BLUE);
        style.checked = newDrawable("white", Color.BLUE);
        style.down = newDrawable("white", Color.RED);

        BitmapFont font = new BitmapFont();
        font.getData().setScale(.5f);
        style.font = font;
        add("default", style);
    }
}