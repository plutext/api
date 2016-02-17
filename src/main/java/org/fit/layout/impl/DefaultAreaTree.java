/**
 * DefaultAreaTree.java
 *
 * Created on 16. 1. 2016, 20:50:33 by burgetr
 */
package org.fit.layout.impl;

import org.fit.layout.model.Area;
import org.fit.layout.model.AreaTree;
import org.fit.layout.model.Page;

/**
 * Default Page implementation.
 *  
 * @author burgetr
 */
public class DefaultAreaTree implements AreaTree
{
    protected Page page;
    private Area root;

    public DefaultAreaTree(Page page)
    {
        this.page = page;
    }
    
    public DefaultAreaTree(AreaTree src)
    {
        page = src.getPage();
        root = src.getRoot();
    }
    
    @Override
    public Page getPage()
    {
        return page;
    }
    
    @Override
    public Area getRoot()
    {
        return root;
    }

    public void setRoot(Area root)
    {
        this.root = root;
    }

    @Override
    public void updateTopologies()
    {
        //no default implementation
    }

    //=================================================================================
    // node search
    //=================================================================================
    
    public Area getAreaAt(int x, int y)
    {
        return recursiveGetAreaAt(root, x, y);
    }
    
    private Area recursiveGetAreaAt(Area root, int x, int y)
    {
        if (root.getBounds().contains(x, y))
        {
            for (int i = 0; i < root.getChildCount(); i++)
            {
                Area ret = recursiveGetAreaAt(root.getChildArea(i), x, y);
                if (ret != null)
                    return ret;
            }
            return root;
        }
        else
            return null;
    }
    
    public Area getAreaByName(String name)
    {
        return recursiveGetAreaByName(root, name);
    }
    
    private Area recursiveGetAreaByName(Area root, String name)
    {
        if (root.toString().indexOf(name) != -1) //TODO ???
            return root;
        else
        {
            for (int i = 0; i < root.getChildCount(); i++)
            {
                Area ret = recursiveGetAreaByName(root.getChildArea(i), name);
                if (ret != null)
                    return ret;
            }
            return null;
        }
    }

}