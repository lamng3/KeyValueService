import java.util.*;

// class for Key-Value Store Database
public class KeyValueStore {

    // hash-map containing the key-value store
    private final HashMap<String, Integer> map;

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
    public int get(String key) {
        if (!map.containsKey(key)) return -1;
        return map.get(key);
    }

    /**
     * Update the value of corresponding key in the key-value store
     * or Create a new pair of key-value in the key-value store
     *    if the key does not exist
     * @param key the key that will be updated or added into KV store
     * @param value the value associated with the key
     */
    public void put(String key, int value) {
        map.put(key, value);
    }

    /**
     * Retrieve all the key-value pairs from the key-value store
     * @return list of all the key-value pairs from the key-value store
     */
    public List<String> mappings() {
        List<String> mps = new ArrayList<>();
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            mps.add("\"" + e.getKey() + " -> " + e.getValue() + "\"");
        }
        return mps;
    }

    /**
     * Retrieve all the keys from the key-value store
     * @return list of keys of the key-value store
     */
    public List<String> keyset() {
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            keys.add(e.getKey());
        }
        return keys;
    }

    /**
     * Retrieve all the values from the key-value store
     * @return list of values of the key-value store
     */
    public List<Integer> values() {
        List<Integer> vals = new ArrayList<>();
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            vals.add(e.getValue());
        }
        return vals;
    }
}
