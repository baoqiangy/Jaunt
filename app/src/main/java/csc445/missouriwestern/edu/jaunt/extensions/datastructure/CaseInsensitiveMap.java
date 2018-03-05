package csc445.missouriwestern.edu.jaunt.extensions.datastructure;

import java.util.HashMap;

/**
 * Created by byan on 3/4/2018.
 */

public class CaseInsensitiveMap extends HashMap<String, String> {
    @Override
    public String put(String key, String value) {
        return super.put(key.toLowerCase(), value);
    }

    // not @Override because that would require the key parameter to be of type Object
    public String get(String key) {
        return super.get(key.toLowerCase());
    }
}
