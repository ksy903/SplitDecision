package com.JaeLee.splitdecision.debug;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class ObjectUserData {
    public boolean reproduced;
    public boolean counted;
    public float size1, size2, size3, size4;
    public Texture texture;
    public ObjectUserData(float a, float b, float c, float d, Texture s){
        reproduced = false;
        counted = false;
        size1 = a;
        size2 = b;
        size3 = c;
        size4 = d;
        texture = s;
    }
}
