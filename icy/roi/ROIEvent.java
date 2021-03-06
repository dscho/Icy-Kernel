/*
 * Copyright 2010-2015 Institut Pasteur.
 * 
 * This file is part of Icy.
 * 
 * Icy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Icy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Icy. If not, see <http://www.gnu.org/licenses/>.
 */
package icy.roi;

import icy.common.CollapsibleEvent;
import icy.util.StringUtil;

/**
 * @author stephane
 */
public class ROIEvent implements CollapsibleEvent
{
    @Deprecated
    public enum ROIPointEventType
    {
        NULL, POINT_ADDED, POINT_REMOVED, POINT_CHANGED;
    }

    public enum ROIEventType
    {
        FOCUS_CHANGED, SELECTION_CHANGED, /**
                                           * ROI position or/and content change event.<br>
                                           * property = {@link ROI#ROI_CHANGED_POSITION} when only
                                           * position has changed
                                           */
        ROI_CHANGED, /**
                      * ROI property change event.<br>
                      * check property field to know which property has actually changed
                      */
        PROPERTY_CHANGED, @Deprecated PAINTER_CHANGED, @Deprecated NAME_CHANGED;
    }

    private final ROI source;
    private final ROIEventType type;
    private String propertyName;

    @Deprecated
    private Object point;
    @Deprecated
    private ROIPointEventType pointEventType;

    /**
     * @deprecated Use {@link #ROIEvent(ROI, ROIEventType)} constructor instead
     */
    @Deprecated
    public ROIEvent(ROI source, ROIEventType type, ROIPointEventType pointEventType, Object point)
    {
        super();

        this.source = source;
        this.type = type;
        propertyName = null;

        this.point = point;
        this.pointEventType = pointEventType;
    }

    public ROIEvent(ROI source, ROIEventType type, String propertyName)
    {
        super();

        this.source = source;
        this.type = type;
        this.propertyName = propertyName;
    }

    public ROIEvent(ROI source, String propertyName)
    {
        this(source, ROIEventType.PROPERTY_CHANGED, propertyName);
    }

    public ROIEvent(ROI source, ROIEventType type)
    {
        this(source, type, null);
    }

    /**
     * @return the source
     */
    public ROI getSource()
    {
        return source;
    }

    /**
     * @return the type
     */
    public ROIEventType getType()
    {
        return type;
    }

    /**
     * @return the propertyName
     */
    public String getPropertyName()
    {
        return propertyName;
    }

    @Deprecated
    public Object getPoint()
    {
        return point;
    }

    @Deprecated
    public ROIPointEventType getPointEventType()
    {
        return pointEventType;
    }

    @Override
    public boolean collapse(CollapsibleEvent event)
    {
        if (equals(event))
        {
            // nothing to do here
            return true;
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        int res = source.hashCode() ^ type.hashCode();

        if (type == ROIEventType.PROPERTY_CHANGED)
            res ^= propertyName.hashCode();

        return res;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof ROIEvent)
        {
            final ROIEvent e = (ROIEvent) obj;

            if ((e.getSource() == source) && (e.getType() == type))
            {
                switch (type)
                {
                    case ROI_CHANGED:
                        return StringUtil.equals(propertyName, e.getPropertyName());
                        
                    case FOCUS_CHANGED:
                    case SELECTION_CHANGED:
                    case NAME_CHANGED:
                    case PAINTER_CHANGED:
                        return true;

                    case PROPERTY_CHANGED:
                        return StringUtil.equals(propertyName, e.getPropertyName());
                }
            }
        }

        return super.equals(obj);
    }
}