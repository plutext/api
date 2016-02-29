/**
 * GridTopology.java
 *
 * Created on 12. 11. 2014, 10:33:00 by burgetr
 */
package org.fit.layout.impl;

import java.awt.Color;
import java.awt.Graphics;

import org.fit.layout.api.OutputDisplay;
import org.fit.layout.model.Area;
import org.fit.layout.model.AreaTopology;
import org.fit.layout.model.Rectangular;

/**
 * The default topology that obtains the area layout from a grid.
 * 
 * @author burgetr
 */
public class DefaultGridTopology implements AreaTopology
{
    private DefaultArea area;
    private AreaGrid grid;
    
    public DefaultGridTopology(DefaultArea area)
    {
        this.area = area;
        grid = new AreaGrid(area);
    }

    public Area getArea()
    {
        return area;
    }
    
    @Override
    public void update()
    {
        grid = new AreaGrid(area);
    }
    
    @Override
    public int getTopologyWidth()
    {
        return grid.getWidth();
    }

    @Override
    public int getTopologyHeight()
    {
        return grid.getHeight();
    }

    @Override
    public int getWidth()
    {
        return area.getGridWidth();
    }

    @Override
    public int getHeight()
    {
        return area.getGridHeight();
    }
    
    @Override
    public Rectangular getPosition()
    {
        return area.getGridPosition();
    }

    @Override
    public Area getPreviousOnLine()
    {
        return area.getPreviousOnLine();
    }

    @Override
    public Area getNextOnLine()
    {
        return area.getNextOnLine();
    }

    @Override
    public int getMinIndent()
    {
        return grid.getMinIndent();
    }

    @Override
    public int getMaxIndent()
    {
        return grid.getMaxIndent();
    }

    @Override
    public void drawLayout(OutputDisplay disp)
    {
        Graphics ig = disp.getGraphics();
        Color c = ig.getColor();
        ig.setColor(Color.BLUE);
        int xo = getArea().getX1();
        for (int i = 1; i <= grid.getWidth(); i++)
        {
            xo += grid.getCols()[i-1];
            /*System.out.println(i + " : " + xo);
            if (i == 42) ig.setColor(Color.GREEN);
            else if (i == 47) ig.setColor(Color.RED);
            else ig.setColor(Color.BLUE);*/
            ig.drawLine(xo, getArea().getY1(), xo, getArea().getY2());
        }
        int yo = getArea().getY1();
        for (int i = 0; i < grid.getHeight(); i++)
        {
            yo += grid.getRows()[i];
            ig.drawLine(getArea().getX1(), yo, getArea().getX2(), yo);
        }
        ig.setColor(c);
        
    }

}
