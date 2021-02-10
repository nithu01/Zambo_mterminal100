package com.zambo.zambo_mterminal100.model.uid;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by SW11 on 3/4/2017.
 */
@Root(name = "Param")
public class Param {

    public Param() {
    }

    @Attribute(name = "name", required = false)
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Attribute(name = "value", required = false)
    public String value;
}
