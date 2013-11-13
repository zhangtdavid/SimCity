package city;

import java.awt.Graphics2D;

/**
 * The base class for all SimCity201 agent animations.
 */
public abstract class Animation {

    public abstract void updatePosition();
    public abstract void draw(Graphics2D g);
    public abstract boolean getVisible();
    public abstract int getXPos();
    public abstract int getYPos();

}
