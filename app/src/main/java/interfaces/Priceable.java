package interfaces;

/**
 * Interface that specifies the functionality provided by any item that can be priced.
 */
public interface Priceable //TODO: Refactor code using this (should be Priceable & Nameable)
{

	/**
	 * Gets the name of the item.
	 * @return The name of the item.
	 */
	String getName();

	/**
	 * Sets the name of the item.
	 * @param name The desired name.
	 */
	void setName(String name);

	/**
	 * Gets the price of the item.
	 * @return The price of the item.
	 */
	long getPrice();

	/**
	 * Sets the price of the item.
	 * @param price The desired price.
	 */
	void setPrice(long price);

}
