/**
 * DefaultArea.java
 *
 * Created on 21. 11. 2014, 11:16:51 by burgetr
 */
package org.fit.layout.impl;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.fit.layout.model.Area;
import org.fit.layout.model.AreaTopology;
import org.fit.layout.model.Box;
import org.fit.layout.model.Rectangular;
import org.fit.layout.model.Tag;

/**
 * Default generic Area implementation.
 * 
 * @author burgetr
 */
public class DefaultArea extends DefaultContentRect implements Area
{
    /** Area name to be displayed to the users */
    private String name;
    
    /** The topology assigned to the area */
    private AreaTopology topology;

    /** The visual boxes that form this area. */
    private Vector<Box> boxes;
    
    /** Assigned tags */
    private Map<Tag, Float> tags;

    /** Effective bounds of the area content. */
    private Rectangular contentBounds;


    public DefaultArea(Rectangular r)
    {
        super();
        name = null;
        //TODO create default topology?
        boxes = new Vector<Box>();
        tags = new HashMap<Tag, Float>();
        setBounds(new Rectangular(r));
        setBackgroundColor(null);
    }
    
    public DefaultArea(DefaultArea src)
    {
        super(src);
        name = (src.name == null) ? null : new String(src.name);
        contentBounds = (src.contentBounds == null) ? null : new Rectangular(src.contentBounds);
        
        
    }
    
    public DefaultArea(int x1, int y1, int x2, int y2)
    {
        this(new Rectangular(x1, y1, x2, y2));
    }
    
    public DefaultArea(Box box)
    {
        this(box.getBounds());
        addBox(box);
        setBounds(new Rectangular(contentBounds)); //update bounds to the box content bounds
        setName(box.toString());
        setBackgroundColor(box.getBackgroundColor());
        setBackgroundSeparated(box.isBackgroundSeparated());
        setBorders(box.getTopBorder(), box.getRightBorder(), box.getBottomBorder(), box.getLeftBorder());
    }
    
    public DefaultArea(List<Box> boxList)
    {
        //use the first box for the area properties
        this(boxList.get(0));
        //update bounds to the box content bounds
        for (Box box : boxList)
            addBox(box); //expands the content bounds appropriately
        setBounds(new Rectangular(contentBounds));
    }
    
    /**
     * Sets the name of the area. The name is used when the area information is displayed
     * using <code>toString()</code>
     * @param The new area name
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public Rectangular getContentBounds()
    {
        return contentBounds;
    }
    
    @Override
    public AreaTopology getTopology()
    {
        return topology;
    }
    
    @Override
    public Color getEffectiveBackgroundColor()
    {
        if (getBackgroundColor() != null)
            return getBackgroundColor();
        else
        {
            if (getParentArea() != null)
                return getParentArea().getEffectiveBackgroundColor();
            else
                return Color.WHITE; //use white as the default root color
        }
    }
    
    @Override
    public Area getParentArea()
    {
        return (Area) getParent();
    }

    @Override
    public Area getPreviousSibling()
    {
        return (Area) getPreviousSibling();
    }
    
    @Override
    public Area getNextSibling()
    {
        return (Area) getNextSibling();
    }

    
    @Override
    public Area getChildArea(int index) throws ArrayIndexOutOfBoundsException
    {
        return (Area) getChildAt(index);
    }

    @Override
    public List<Area> getChildAreas()
    {
        Vector<Area> ret = new Vector<Area>(getChildCount());
        for (GenericTreeNode child : getChildren())
            ret.add((Area) child);
        return ret;
    }

    @Override
    public int getIndex(Area child)
    {
        return super.getIndex((DefaultArea) child);
    }
    
    @Override
    public void appendChild(Area child)
    {
        add((DefaultArea) child);
        getBounds().expandToEnclose(child.getBounds());
    }
    
    @Override
    public void insertChild(Area child, int index)
    {
        insert((DefaultArea) child, index);
    }

    @Override
    public void removeChild(Area child)
    {
        remove((DefaultArea) child); 
    }
    
    @Override
    public String getText()
    {
        String ret = "";
        if (isLeaf())
            ret = getBoxText();
        else
            for (int i = 0; i < getChildCount(); i++)
                ret += getChildArea(i).getText();
        return ret;
    }
    
    @Override
    public boolean isReplaced()
    {
        boolean empty = true;
        for (Box root : boxes)
        {
            empty = false;
            if (root.getType() != Box.Type.REPLACED_CONTENT)
                return false;
        }
        return !empty;
    }
    
     //====================================================================================
    // boxes
    //====================================================================================
    
    /**
     * Adds a new box to the area.
     * @param box
     */
    protected void addBox(Box box)
    {
        boxes.add(box);
        
        Rectangular sb = box.getVisualBounds();
        if (contentBounds == null)
            contentBounds = new Rectangular(sb);
        else if (sb.getWidth() > 0 && sb.getHeight() > 0)
            contentBounds.expandToEnclose(sb);
    }
    
    /**
     * Returns a vector of boxes that are inside of this area
     * @return A vector containing the {@link org.burgetr.segm.BoxNode BoxNode} objects
     */
    public Vector<Box> getBoxes()
    {
        return boxes;
    }
    
    /** 
     * Obtains all the boxes from this area and all the child areas.
     * @return The list of boxes
     */
    @Override
    public Vector<Box> getAllBoxes()
    {
        Vector<Box> ret = new Vector<Box>();
        recursiveFindBoxes(this, ret);
        return ret;
    }
    
    private void recursiveFindBoxes(Area root, Vector<Box> result)
    {
        result.addAll(root.getBoxes());
        for (int i = 0; i < root.getChildCount(); i++)
            recursiveFindBoxes((Area) root.getChildArea(i), result);
    }

    /**
     * Returns the text string represented by a concatenation of all
     * the boxes contained directly in this area (no subareas)
     */
    public String getBoxText()
    {
        StringBuilder ret = new StringBuilder();
        boolean start = true;
        for (Iterator<Box> it = boxes.iterator(); it.hasNext(); )
        {
            if (!start) ret.append(' ');
            else start = false;
            ret.append(it.next().getText());
        }
        return ret.toString();
    }
    
    //====================================================================================
    // tagging
    //====================================================================================
    
    /**
     * Adds a tag to this area.
     * @param tag the tag to be added.
     */
    @Override
    public void addTag(Tag tag, float support)
    {
        tags.put(tag, support);
    }
    
    /**
     * Tests whether the area has this tag.
     * @param tag the tag to be tested.
     * @return <code>true</code> if the area has this tag
     */
    @Override
    public boolean hasTag(Tag tag)
    {
        return tags.get(tag) != null;
    }
    
    @Override
    public float getTagSupport(Tag tag)
    {
        Float f = tags.get(tag);
        if (f == null)
            return 0.0f;
        else
            return f;
    }
    
    @Override
    public Tag getMostSupportedTag()
    {
        float max = -1.0f;
        Tag ret = null;
        for (Map.Entry<Tag, Float> entry : tags.entrySet())
        {
            if (entry.getValue() > max)
            {
                max = entry.getValue();
                ret = entry.getKey();
            }
        }
        return ret;
    }
    
    /**
     * Removes all tags that belong to the given collection.
     * @param c A collection of tags to be removed.
     */
    public void removeAllTags(Collection<Tag> c)
    {
        for (Tag t : c)
            tags.remove(t);
    }
    
    /**
     * Tests whether the area or any of its <b>direct child</b> areas have the given tag.
     * @param tag the tag to be tested.
     * @return <code>true</code> if the area or its direct child areas have the given tag
     */
    public boolean containsTag(Tag tag)
    {
        if (hasTag(tag))
            return true;
        else
        {
            for (GenericTreeNode child : getChildren())
                if (((Area) child).hasTag(tag))
                    return true;
            return false;
        }
    }
    
    /**
     * Obtains the set of tags assigned to the area.
     * @return a set of tags
     */
    @Override
    public Map<Tag, Float> getTags()
    {
        return tags;
    }
    
    @Override
    public String toString()
    {
        String bs = "";
        if (hasTopBorder()) bs += "^";
        if (hasLeftBorder()) bs += "<";
        if (hasRightBorder()) bs += ">";
        if (hasBottomBorder()) bs += "_";
        if (isBackgroundSeparated()) bs += "*";
        
        if (name != null)
            return bs + " " + name + " " + getBounds().toString();
        else
            return bs + " " + "<area> " + getBounds().toString();
          
    }

}