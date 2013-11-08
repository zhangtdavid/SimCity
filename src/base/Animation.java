package base;

import java.awt.Graphics2D;

public interface Animation {

    public void updatePosition();
    public void draw(Graphics2D g);
    public boolean getVisible();
    public int getXPos();
    public int getYPos();

}
