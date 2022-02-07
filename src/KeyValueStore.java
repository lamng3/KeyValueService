import java.util.*;

// class for Key-Value Store Database
public class KeyValueStore {

    // hash-map containing the key-value store
    private final HashMap<String, String> map;

    /**
     * Constructor creating the key-value store
     */
    public KeyValueStore() {
        map = new HashMap<>();
    }

    /**
     * Gets the value of the corresponding key from the key-value store
     * @param key the key that we need to access to the database
     * @return the value associated with the key
     */
    public String get(String key) {
        return map.get(key);
    }

    /**
     * Update the value of corresponding key in the key-value store
     * or Create a new pair of key-value in the key-value store
     *    if the key does not exist
     * @param key the key that will be updated or added into KV store
     * @param value the value associated with the key
     */
    public void put(String key, String value) {
        map.put(key, value);
    }

    /**
     * Retrieve all the key-value pairs from the key-value store
     */
    public void mappings() {
        for (Map.Entry<String, String> e : map.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }
    }

    /**
     * Retrieve all the keys from the key-value store
     */
    public void keyset() {
        for (Map.Entry<String, String> e : map.entrySet()) {
            System.out.println(e.getKey());
        }
    }

    /**
     * Retrieve all the values from the key-value store
     */
    public void values() {
        for (Map.Entry<String, String> e : map.entrySet()) {
            System.out.println(e.getValue());
        }
    }
}
