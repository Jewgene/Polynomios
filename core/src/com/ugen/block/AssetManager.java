package com.ugen.block;

/**
 * Created by WilsonCS30 on 10/17/2016.
 */

public class AssetManager {

    private static ButtonSkin buttonSkin;
    private static ButtonSkin toggleButtonSkin;

    private AssetManager(){

    }

    public static void load(){
        buttonSkin = new ButtonSkin("button.png", 100, 50, 0, 0, false);
        toggleButtonSkin = new ButtonSkin("button.png", 100, 50, 0, 0, true);
    }

    public static ButtonSkin getButtonSkin(){
        return buttonSkin;
    }

    public static ButtonSkin getToggleButtonSkin(){
        return toggleButtonSkin;
    }
}
