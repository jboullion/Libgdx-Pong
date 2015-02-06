package com.badlogic.androidgames.framework;

import com.badlogic.androidgames.framework.math.Circle;
import com.badlogic.androidgames.framework.math.Rectangle;
import com.badlogic.androidgames.framework.math.Vector2;

public class GameObject {
    public Vector2 position;
    public Circle bounds;
    public Rectangle rect;
    
    public GameObject(float x, float y, float radius) {
        this.position = new Vector2(x,y);
        this.bounds = new Circle(x, y, radius);
        this.rect = new Rectangle(x, y, radius, radius);
    }
    
    public GameObject(float x, float y, float width, float height) {
        this.position = new Vector2(x,y);
        this.bounds = new Circle(x, y, height);
        this.rect = new Rectangle(x, y, width, height);
    }
    
}
