/**
 * Author: Devante Wilson - 100554361
 *
 * Class stores the information for a given product
 */

package assignment.mobiledev.mobileassignment2;

class Product
{
    // define instance variables
    private int productId;
    private String name;
    private String description;
    private float price;

    // constructor
    public Product(int ProductId, String name, String description, float price)
    {
        // initialize instance variables
         this.productId = ProductId;
         this.name = name;
         this.description = description;
         this.price = price;
    }

    // accessor method
    public int getProductId()
    {
        return productId;
    }

    // accessor method
    public String getName()
    {
        return name;
    }

    // accessor method
    public String getDescription()
    {
        return description;
    }

    // accessor method
    public float getPrice()
    {
        return price;
    }
}
