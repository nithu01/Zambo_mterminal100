package com.zambo.zambo_mterminal100.model.startuid;

public class Startek {

    private PidDatastar PidData;

    public PidDatastar getPidData ()
    {
        return PidData;
    }

    public void setPidData (PidDatastar PidData)
    {
        this.PidData = PidData;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [PidData = "+PidData+"]";
    }
}
