package com.zambo.zambo_mterminal100.model.startuid;

public class PidDatastar {

    private String Hmac;

    private StarResp Resp;

    private Stardeviceinfo DeviceInfo;

    private Stardata Data;

    private StartSkey Skey;

    public String getHmac ()
    {
        return Hmac;
    }

    public void setHmac (String Hmac)
    {
        this.Hmac = Hmac;
    }

    public StarResp getResp ()
    {
        return Resp;
    }

    public void setResp (StarResp Resp)
    {
        this.Resp = Resp;
    }

    public Stardeviceinfo getDeviceInfo ()
    {
        return DeviceInfo;
    }

    public void setDeviceInfo (Stardeviceinfo DeviceInfo)
    {
        this.DeviceInfo = DeviceInfo;
    }

    public Stardata getData ()
    {
        return Data;
    }

    public void setData (Stardata Data)
    {
        this.Data = Data;
    }

    public StartSkey getSkey ()
    {
        return Skey;
    }

    public void setSkey (StartSkey Skey)
    {
        this.Skey = Skey;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Hmac = "+Hmac+", Resp = "+Resp+", DeviceInfo = "+DeviceInfo+", Data = "+Data+", Skey = "+Skey+"]";
    }
}
