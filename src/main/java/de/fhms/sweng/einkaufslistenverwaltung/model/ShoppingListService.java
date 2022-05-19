package de.fhms.sweng.einkaufslistenverwaltung.model;

import de.fhms.sweng.einkaufslistenverwaltung.inbound.EntryDto;
import feign.RetryableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ShoppingListService {
    private ShoppingListRepository shoppingListRepository;
    private ShoppingListProductRepository shoppingListProductRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private FoodServiceClient foodServiceClient;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository, ShoppingListProductRepository shoppingListProductRepository, ProductRepository productRepository, UserRepository userRepository, FoodServiceClient foodServiceClient) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListProductRepository = shoppingListProductRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.foodServiceClient = foodServiceClient;
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
     * Find a shopping list in the database by the User id
     *
     * @param id of the user
     * @return a shopping list
     */
    public ShoppingList getShoppingListByUserId(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(user.getShoppingListId());
            if (shoppingListOptional.isPresent()) {
                ShoppingList shoppingList = shoppingListOptional.get();
                return shoppingList;
            } else {
                throw new RuntimeException("Requested ShoppingList is not in DB");
            }
        } else {
            throw new RuntimeException("Requested User is not in DB");
        }
    }

    /**
     * Creates a new shopping list for a User.
     *
     * @param userId
     * @param name   of the shopping list
     * @return the new shopping list entity
     */
    public ShoppingList addShoppingList(Integer userId, String name) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findByName(name);
            if (!shoppingListOptional.isPresent()) {
                ShoppingList shoppingList = new ShoppingList(user, name);
                shoppingListRepository.save(shoppingList);
                return shoppingList;
            } else {
                throw new AlreadyExistException("Requested ShoppingList is already in DB");
            }
        } else {
            throw new RuntimeException("Requested User is not in DB");
        }
    }

    /**
     * Remove user from shopping list and delete the list, when no other user left
     *
     * @param id of the user that was deleted
     */
    public void deleteShoppingList(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(user.getShoppingListId());
            if (shoppingListOptional.isPresent()) {
                ShoppingList shoppingList = shoppingListOptional.get();
                if (shoppingList.getUserCount() < 2) {
                    shoppingListRepository.delete(shoppingList);
                } else {
                    shoppingList.removeUser(user);
                    shoppingListRepository.save(shoppingList);
                }

            } else {
                throw new RuntimeException("Requested ShoppingList is not in DB");
            }
        } else {
            throw new RuntimeException("Requested User is not in DB");
        }
    }


    /**
     * Get all Products from a Shopping List from the database
     *
     * @param id from the shopping list
     * @return a list with shopping lists
     */
    public Set<ShoppingListProduct> getAllProductsFromShoppingList(Integer id) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(id);
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            return shoppingList.getShoppingListProducts();
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
            Set<ShoppingListProduct> shoppingListProducts = shoppingList.getShoppingListProducts();
            for (ShoppingListProduct i : shoppingListProducts) {
                if (i.getProduct().getId().equals(num)) {
                    throw new RuntimeException("Product already exists on Shopping List");
                }
            }
            Optional<Product> productOptional = productRepository.findById(num);
            if (productOptional.isPresent()) {
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
     * Adds a product to a shopping list
     *
     * @param entryDto
     * @return
     */
    public Boolean addProductToList(EntryDto entryDto) {
        //TODO:
        return false;
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
            Set<ShoppingListProduct> shoppingListProducts = shoppingList.getShoppingListProducts();
            for (ShoppingListProduct i : shoppingListProducts) {
                if (i.getProduct().getId().equals(num)) {
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
     * @param id  of the shopping list
     * @param num of the product
     */
    public void deleteProductFromList(Integer id, Integer num) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(id);
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            Set<ShoppingListProduct> shoppingListProducts = shoppingList.getShoppingListProducts();
            for (ShoppingListProduct i : shoppingListProducts) {
                if (i.getProduct().getId().equals(num)) {
                    shoppingListProductRepository.delete(i);
                    return;
                }
            }
            throw new RuntimeException("Requested Product is not in Shopping List");
        } else {
            throw new RuntimeException("Requested ShoppingList is not in DB");
        }
    }

    /**
     * This method is used to send an entry from the shopping list to the food service.
     * Uses the foodServiceClient to send an external request.
     *
     * @param userId
     * @param productId
     * @param amount
     */
    @Retryable(include = RetryableException.class,
            maxAttempts = 3, //first attempt and 2 retries
            backoff = @Backoff(delay = 100, maxDelay = 500))
    public void addFoodEntry(Integer userId, Integer productId, Integer amount) {
        //LOG.info("Execute addFoodEntry({},{}).", productName, discounter);
        FoodEntry foodEntry = foodServiceClient.postFoodEntry(userId, productId, amount);
        //TODO: Delete Entry from List if successful
    }


}
