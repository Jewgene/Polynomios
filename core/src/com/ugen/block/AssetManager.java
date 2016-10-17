package com.ugen.block;

/**
 * Created by WilsonCS30 on 10/17/2016.
 */

public class AssetManager {

    private static ButtonSkin buttonSkin;

    private AssetManager(){

    }

    public static void load(){
        buttonSkin = new ButtonSkin("button.png", 100, 50, 0, 0);
    }

    public static ButtonSkin getButtonSkin(){
        return buttonSkin;
    }
}
