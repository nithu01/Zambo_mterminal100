package com.zambo.zambo_mterminal100.model.startuid;

import org.simpleframework.xml.Attribute;

public class ParamStartek {
    @Attribute(name = "name", required = false)
    private String name;
    @Attribute(name = "value", required = false)
    private String value;

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

}
