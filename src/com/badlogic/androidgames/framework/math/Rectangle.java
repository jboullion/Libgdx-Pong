package com.badlogic.androidgames.framework.math;

public class Rectangle{
    public final Vector2 lowerLeft;
    public float width, height;
    public float x, y;
    
    public Rectangle(float x, float y, float width, float height) {
        this.lowerLeft = new Vector2(x,y);
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }
}
