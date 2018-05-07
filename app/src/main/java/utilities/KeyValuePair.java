package utilities;

/**
 * A class that stores a key-value pair.
 * @param <K> The type of the key.
 * @param <V> The type of the value.
 */
public class KeyValuePair<K, V>
{

    //region Members

    private K m_key;
    private V m_value;

    //endregion

    //region Constructor

    /**
     * Initializes a new instance of a KeyValuePair
     * @param key The key.
     * @param value The value.
     */
    public KeyValuePair(K key, V value)
    {
        m_key = key;
        m_value = value;
    }

    //endregion

    //region Getters & Setters

    /**
     * Gets the key.
     * @return The key.
     */
    public K getKey()
    {
        return m_key;
    }

    /**
     * Sets the key.
     * @param key The desired key.
     */
    public void setKey(K key)
    {
        m_key = key;
    }

    /**
     * Gets the value.
     * @return The value.
     */
    public V getValue()
    {
        return m_value;
    }

    /**
     * Sets the value.
     * @param value The desired value.
     */
    public void setValue(V value)
    {
        m_value = value;
    }

    //endregion

}
