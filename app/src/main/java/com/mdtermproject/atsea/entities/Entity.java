package com.mdtermproject.atsea.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.mdtermproject.atsea.graphics.ImageLibrary;
import com.mdtermproject.atsea.utils.ResourceLoader;

/**
 * Created by Alec Krawciw on 2017-05-03.
 */

public abstract class Entity {
    String TAG = "Entity";

    /**
     * The area considered to be the entity for collisions
     * */
    private Rect hitBox;
    private int width = 0;
    private int height = 0;

    /**
     * The speeds both x and y of the entity on the screen.
     * Remember that positive y is down the screen
     */
    private double vx = 0;
    private double vy = 0;

    /**
     * The entities current position on the screen.
     * Remember that (0,0) is the top left corner
     */
    private double x = 0;
    private double y = 0;

    /**
     * The last time (in milliseconds) that the update method was called.
     * This allows you to account for changes in the game loop's execution speed.
     * This gives you smoother motion regardless
     */
    protected long lastUpdateTime = System.currentTimeMillis();

    /**
     * The image to be drawn representing the Entity.
     * Bitmas are simiar to BufferedImages in AWT
     */
    protected Bitmap image;

    /**
     * Flag denoting whether or not the game loop should remove this entity from the list of entities
     */
    protected boolean valid = true;

    /**
     * Use this to load Android resources such as
     * Bitmaps, Colours and Strings
     */
    protected ResourceLoader resourceLoader = new ResourceLoader();

    /**
     * Create a new Entity object.
     * @param resId The Android resource for the Entity image. In form R.drawable.[filename no extension]
     * @param vx The horizontal speed
     * @param vy The vertical speed
     * @param x The horizontal location
     * @param y The vertical location
     * @param width The width of the object
     * @param height The height of the object
     */
    public Entity(int resId, double vx, double vy, double x, double y, int width, int height){
        image = Bitmap.createScaledBitmap(ImageLibrary.getImage(resId), width, height, false);
        this.vx = vx;
        this.vy = vy;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        updateHitBox();
    }

    /**
     * This draws the entity's image on the sscreen at the current location
     * @See android.graphics.Canvas
     * @param c The Canvas to be drawn on
     */
    public void draw(Canvas c){
        c.drawBitmap(image, (int)x, (int)y, new Paint());
    }

    /**
     * This updates the location of the Entity's hitbox once the entity has moved
     */
    protected void updateHitBox(){
        this.hitBox = new Rect((int)x,  (int)y, (int)(x + width), (int)(y + height));
    }

    /**
     * @return The Entity's current hitbox
     */
    public Rect getHitBox(){
        return hitBox;
    }

    /**
     * Called every game loop to update the Entity's state.
     * By default it moves the Entity by its speed multiplied the time since the last
     * loop execution. This makes for smoother graphics.
     * If you want an animated image, this would be the place to update the frame.
     */
    public void update(){
        x += vx * (System.currentTimeMillis() - lastUpdateTime);
        y += vy * (System.currentTimeMillis() - lastUpdateTime);
        lastUpdateTime = System.currentTimeMillis();
        updateHitBox();
        checkHitBox();
    }

    /**
     * To be implemented by subclasses.
     * Determines if there is any sectiom that needs to be taken based on
     * the Entity's current position.
     */
    public abstract void checkHitBox();

    //Getter and Setter Methods

    /**
     * @return The current vertical velocity.
     */
    public double getVy() {
        return vy;
    }

    /**
     * Set the current vertical velocity.
     * @param vy The new vertical velocity
     */
    public void setVy(double vy) {
        this.vy = vy;
    }

    /**
     * Get the current horizontal velocity
     * @return The current horizontal velocity.
     */
    public double getVx() {
        return vx;
    }

    /**
     * Set the horizontal velocity.
     * @param vx The new horizontal velocity to be set.
     */
    public void setVx(double vx) {
        this.vx = vx;
    }

    /**
     * Get the current horizontal location
     * @return The Entity's current horizontal location.
     */
    public double getX() {
        return x;
    }

    /**
     * Set the x position
     * @param x The new horizontal position to be set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Get the current y position.
     * @return The current vertical position of the top of the entity.
     */
    public double getY() {
        return y;
    }

    /**
     * Set the current vertical position of the Entity.
     * @param y The new vertical position to be set.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Get the height of the Entity.
     * @return The current height of the entity.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the width of the entity.
     * @return the current width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Check to see if the Entity has collided with another entity.
     * @param other The Entity to see if the have collided with
     * @return true if they have collided false otherwise
     */
    public boolean collidedWith(Entity other){
        return Rect.intersects(hitBox, other.hitBox);
    }

    /**
     * To be implemented by subclasses.
     * Actions to be performed in the event that this entity has collided with another.
     * @param other The Entity that collided with this Entity
     */
    public abstract void collideAction(Entity other);

    /**
     * Tells the gameloop whether or not to destroy this entity.
     * @return true if the entity is still valid false if it should be destroyed
     */
    public boolean isValid(){
        return valid;
    }
}
