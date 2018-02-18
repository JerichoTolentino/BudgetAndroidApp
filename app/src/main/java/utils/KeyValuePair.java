package utils;

public class KeyValuePair<K,V>
{

    private K m_key;
    private V m_value;

    public KeyValuePair(K key, V value)
    {
        m_key = key;
        m_value = value;
    }

    public K getKey()
    {
        return m_key;
    }

    public V getValue()
    {
        return m_value;
    }

}
