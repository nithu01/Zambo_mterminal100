package com.zambo.zambo_mterminal100.model.startuid;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
@Root(name = "StarAdditional_info")
public class StarAdditional_info {

    public StarAdditional_info() {
    }

    @ElementList(name = "ParamStartek", required = false, inline = true)
    public List<ParamStartek> params;
}
