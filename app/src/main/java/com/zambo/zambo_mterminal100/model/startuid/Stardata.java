package com.zambo.zambo_mterminal100.model.startuid;

public class Stardata {
    private String type;

    private String content;

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getContent ()
    {
        return content;
    }

    public void setContent (String content)
    {
        this.content = content;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [type = "+type+", content = "+content+"]";
    }
}
