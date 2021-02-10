package com.zambo.zambo_mterminal100.model.startuid;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class StartSkey {

    public StartSkey() {
    }

    @Attribute(name = "ci", required = false)
    public String ci;

    @Text(required = true)
    public String value;
}
