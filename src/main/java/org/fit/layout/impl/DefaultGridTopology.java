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
 * The default topology that uses the internal grid of areas internally
 * maintained by the {@link DefaultArea} implementation.
 * 
 * @author burgetr
 */
public class DefaultGridTopology implements AreaTopology
{
    private DefaultArea area;
    
    public DefaultGridTopology(DefaultArea area)
    {
        this.area = area;
    }

    public Area getArea()
    {
        return area;
    }
    
    @Override
    public void update()
    {
        area.createGrid();
    }

    @Override
    public int getTopologyWidth()
    {
        return area.getGrid().getWidth();
    }

    @Override
    public int getTopologyHeight()
    {
        return area.getGrid().getHeight();
    }

    @Override
    public Rectangular getTopologyPosition()
    {
        return area.getGrid().getAbsolutePosition();
    }

    @Override
    public Rectangular getPosition(Area a)
    {
        if (a.getParentArea() == area && a instanceof DefaultArea)
            return ((DefaultArea) a).getGridPosition();
        else
            return null;
    }

    @Override
    public void setPosition(Area a, Rectangular gp)
    {
        if (a.getParentArea() == area && a instanceof DefaultArea)
            ((DefaultArea) a).setGridPosition(gp);
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
        return area.getGrid().getMinIndent();
    }

    @Override
    public int getMaxIndent()
    {
        return area.getGrid().getMaxIndent();
    }

    @Override
    public void drawLayout(OutputDisplay disp)
    {
        Graphics ig = disp.getGraphics();
        AreaGrid grid = area.getGrid();
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
