/**
 * BaseParametrizedOperation.java
 *
 * Created on 27. 1. 2015, 15:22:03 by burgetr
 */
package org.fit.layout.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.fit.layout.api.ParametrizedOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author burgetr
 */
public abstract class BaseParametrizedOperation implements ParametrizedOperation
{
    private static Logger log = LoggerFactory.getLogger(BaseParametrizedOperation.class);
            
    /**
     * Sets the parameter using the appropriate setter method (if present).
     */
    @Override
    public boolean setParam(String name, Object value)
    {
        String sname = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        Method m;
        try {
            if (value instanceof Integer)
            {
                m = getClass().getMethod(sname, int.class);
                m.invoke(this, value);
            }
            else if (value instanceof Double)
            {
                m = getClass().getMethod(sname, float.class);
                m.invoke(this, ((Double) value).floatValue());
            }
            else if (value instanceof Float)
            {
                m = getClass().getMethod(sname, float.class);
                m.invoke(this, value);
            }
            else if (value instanceof Boolean)
            {
                m = getClass().getMethod(sname, boolean.class);
                m.invoke(this, value);
            }
            else
            {
                m = getClass().getMethod(sname, String.class);
                m.invoke(this, value.toString());
            }
            return true;
            
        } catch (NoSuchMethodException e) {
            log.warn("Setting unknown parameter: " + e.getMessage());
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }

    }

}
