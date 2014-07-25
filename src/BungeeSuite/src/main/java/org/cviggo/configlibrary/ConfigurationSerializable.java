package org.cviggo.configlibrary;

import java.util.Map;

public interface ConfigurationSerializable {
    public Map<String, Object> serialize();
}