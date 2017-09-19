Fix Bug (15 min)
================
* maybe you have already found some nasty parts of the code
    * the customer/tester feels that a single rainy day increases the average price of all products for all time after
    * a tester mentioned that some event seems to modify the properties of all products
* find the variable that represents the product
* find the unintended change to the product
* find the tests that expect changes in the products configuration
* now we fix this bug test-driven
    * change these (or some) tests to expect no change, consequently these tests fail after this modification
    * then we fix the code and the adjusted tests should be green
* try out the other tests
    * there will be tests that fail because they expect a product change -> these are easy to fix
    * there will be probably other tests that fail because they expect other prices -> discuss with your peer why
    * the latter can be fixed manually, but also by recording the test -> try it out