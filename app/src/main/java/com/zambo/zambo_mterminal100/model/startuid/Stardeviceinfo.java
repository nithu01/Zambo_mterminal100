package com.zambo.zambo_mterminal100.model.startuid;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Stardeviceinfo")
public class Stardeviceinfo {
    public Stardeviceinfo() {
    }


    @Attribute(name = "mc")
    public String mc;

    @Attribute(name = "dpId")
    public String dpId;

    @Attribute(name = "rdsId")
    public String rdsId;

    @Attribute(name = "mi")
    public String mi;

    @Attribute(name = "mrdsVerc")
    public String rdsVer;

    @Attribute(name = "dc")
    public String dc;

    @Element(name = "additional_info")
    public StarAdditional_info additional_info;

//    public StarAdditional_info getAdditional_info ()
//    {
//        return additional_info;
//    }
//
//    public void setAdditional_info (StarAdditional_info additional_info)
//    {
//        this.additional_info = additional_info;
//    }
//
//    public String getMc ()
//    {
//        return mc;
//    }
//
//    public void setMc (String mc)
//    {
//        this.mc = mc;
//    }
//
//    public String getDpId ()
//    {
//        return dpId;
//    }
//
//    public void setDpId (String dpId)
//    {
//        this.dpId = dpId;
//    }
//
//    public String getRdsId ()
//    {
//        return rdsId;
//    }
//
//    public void setRdsId (String rdsId)
//    {
//        this.rdsId = rdsId;
//    }
//
//    public String getMi ()
//    {
//        return mi;
//    }
//
//    public void setMi (String mi)
//    {
//        this.mi = mi;
//    }
//
//    public String getRdsVer ()
//    {
//        return rdsVer;
//    }
//
//    public void setRdsVer (String rdsVer)
//    {
//        this.rdsVer = rdsVer;
//    }
//
//    public String getDc ()
//    {
//        return dc;
//    }
//
//    public void setDc (String dc)
//    {
//        this.dc = dc;
//    }

//    @Override
//    public String toString()
//    {
//        return "ClassPojo [additional_info = "+additional_info+", mc = "+mc+", dpId = "+dpId+", rdsId = "+rdsId+", mi = "+mi+", rdsVer = "+rdsVer+", dc = "+dc+"]";
//    }
}
