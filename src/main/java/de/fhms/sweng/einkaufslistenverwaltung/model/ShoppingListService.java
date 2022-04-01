package de.fhms.sweng.einkaufslistenverwaltung.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingListService {

    private ShoppingListRepository shoppingListRepository;
    private ShoppingListProductRepository shoppingListProductRepository;
    private ProductRepository productRepository;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository, ShoppingListProductRepository shoppingListProductRepository, ProductRepository productRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListProductRepository = shoppingListProductRepository;
        this.productRepository = productRepository;
    }

    /**
     * Get all Shopping Lists from the database
     *
     * @return a list with shopping lists
     */
    public List<ShoppingList> getShoppingLists() {
        Optional<List<ShoppingList>> shoppingListOptional = shoppingListRepository.getAll();
        if (shoppingListOptional.isPresent()) {
            return shoppingListOptional.get();
        } else {
            throw new RuntimeException("No ShoppingList in DB");
        }
    }


    /**
     * Find a shopping list in the database
     *
     * @param id of the shopping list
     * @return a shopping list
     */
    public ShoppingList getShoppingList(Integer id) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(id);
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            return shoppingList;
        } else {
            throw new RuntimeException("Requested ShoppingList is not in DB");
        }
    }

    /**
     * Adds a new shopping list.
     *
     * @param name of the shopping list
     * @return the new shopping list entity
     */
    public ShoppingList addShoppingList(String name) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findByName(name);
        if (!shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = new ShoppingList(name);
            shoppingListRepository.save(shoppingList);
            return shoppingList;
        } else {
            throw new RuntimeException("Requested ShoppingList is not in DB");
        }
    }

    /**
     * Deletes a Shopping List from the database
     *
     * @param id of the Shopping List that should be deleted
     */
    public void deleteShoppingList(Integer id) {
        Optional<ShoppingList> shoppingList = shoppingListRepository.findById(id);
        if (shoppingList.isPresent()) {
            shoppingListRepository.delete(shoppingList.get());
        } else {
            throw new RuntimeException("Requested Product is not in DB");
        }
    }


    /**
     * Get all Products from a Shopping List from the database
     *
     * @param id from the shopping list
     * @return a list with shopping lists
     */
    public List<ShoppingListProduct> getAllProductsFromShoppingList(Integer id) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(id);
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            return shoppingList.getShoppingListProducts();
        } else {
            throw new RuntimeException("Requested ShoppingList is not in DB");
        }
    }

    /**
     * Get a certain product from a shopping list from the database
     *
     * @param id  from the shopping list
     * @param num from the product
     * @return a shopping list product
     */
    public ShoppingListProduct getProductFromShoppingListById(Integer id, Integer num) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(id);
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            List<ShoppingListProduct> shoppingListProducts = shoppingList.getShoppingListProducts();
            for (ShoppingListProduct i : shoppingListProducts) {
                if (i.getProduct().getId().equals(num)) {
                    return i;
                }
            }
            throw new RuntimeException("Requested Product is not in DB");
        } else {
            throw new RuntimeException("Requested ShoppingList is not in DB");
        }
    }

    /**
     * Adds a product to a shopping list
     *
     * @param id     of the shopping list
     * @param num    of the product
     * @param amount to be set
     * @return the added shopping list product entity
     */
    public ShoppingListProduct addProductToList(Integer id, Integer num, Integer amount) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(id);
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            List<ShoppingListProduct> shoppingListProducts = shoppingList.getShoppingListProducts();
            for (ShoppingListProduct i : shoppingListProducts) {
                if (i.getProduct().getId().equals(num)) {
                    throw new RuntimeException("Product already exists on Shopping List");
                }
            }
            Optional<Product> productOptional = productRepository.findById(num);
            if(productOptional.isPresent()) {
                Product product = productOptional.get();
                ShoppingListProduct shoppingListProduct = new ShoppingListProduct(product, shoppingList, amount);
                shoppingListProductRepository.save(shoppingListProduct);
                return shoppingListProduct;
            } else {
                throw new RuntimeException("Requested Product is not in DB");
            }
        } else {
            throw new RuntimeException("Requested ShoppingList is not in DB");
        }
    }

    /**
     * Changes the amount of a product on a shopping list
     *
     * @param id     of the shopping list
     * @param num    of the product
     * @param amount to be set
     * @return the changed product entity
     */
    public ShoppingListProduct updateAmount(Integer id, Integer num, Integer amount) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(id);
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            List<ShoppingListProduct> shoppingListProducts = shoppingList.getShoppingListProducts();
            for(ShoppingListProduct i: shoppingListProducts){
                if(i.getProduct().getId().equals(num)){
                    i.setAmount(amount);
                    shoppingListProductRepository.save(i);
                    return i;
                }
            }
            throw new RuntimeException("Requested Product is not in Shopping List");
        } else {
            throw new RuntimeException("Requested ShoppingList is not in DB");
        }
    }

    /**
     * Deletes a product from a shopping list
     *
     * @param id     of the shopping list
     * @param num    of the product
     */
    public void deleteProductFromList(Integer id, Integer num) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(id);
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            List<ShoppingListProduct> shoppingListProducts = shoppingList.getShoppingListProducts();
            for(ShoppingListProduct i: shoppingListProducts){
                if(i.getProduct().getId().equals(num)){
                    shoppingListProductRepository.delete(i);
                    return;
                }
            }
            throw new RuntimeException("Requested Product is not in Shopping List");
        } else {
            throw new RuntimeException("Requested ShoppingList is not in DB");
        }
    }

}
