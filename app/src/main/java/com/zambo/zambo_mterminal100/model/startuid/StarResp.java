package com.zambo.zambo_mterminal100.model.startuid;

public class StarResp {

    private String qScore;

    private String errInfo;

    private String fType;

    private String errCode;

    private String fCount;

    private String nmPoints;

    public String getQScore ()
    {
        return qScore;
    }

    public void setQScore (String qScore)
    {
        this.qScore = qScore;
    }

    public String getErrInfo ()
    {
        return errInfo;
    }

    public void setErrInfo (String errInfo)
    {
        this.errInfo = errInfo;
    }

    public String getFType ()
    {
        return fType;
    }

    public void setFType (String fType)
    {
        this.fType = fType;
    }

    public String getErrCode ()
    {
        return errCode;
    }

    public void setErrCode (String errCode)
    {
        this.errCode = errCode;
    }

    public String getFCount ()
    {
        return fCount;
    }

    public void setFCount (String fCount)
    {
        this.fCount = fCount;
    }

    public String getNmPoints ()
    {
        return nmPoints;
    }

    public void setNmPoints (String nmPoints)
    {
        this.nmPoints = nmPoints;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [qScore = "+qScore+", errInfo = "+errInfo+", fType = "+fType+", errCode = "+errCode+", fCount = "+fCount+", nmPoints = "+nmPoints+"]";
    }
}
