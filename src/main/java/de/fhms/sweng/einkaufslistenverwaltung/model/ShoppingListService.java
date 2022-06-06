package de.fhms.sweng.einkaufslistenverwaltung.model;

import de.fhms.sweng.einkaufslistenverwaltung.inbound.EntryDto;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.ShoppingListProductDto;
import feign.RetryableException;
import org.hibernate.StaleStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Retryable(include = {OptimisticLockException.class, StaleStateException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100, maxDelay = 500))
public class ShoppingListService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
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
     * Adds a new user and creates a new ShoppingList
     *
     * @param userId
     * @param userName
     * @param email
     */
    @Transactional
    public Boolean addUserWithNewShoppingList(int userId, String userName, String email) {
        LOGGER.info("Execute addUserWithNewShoppingList({},{},{}).", userId, userName, email);
        User user = new User(userId, userName, email);
        ShoppingList shoppingList = new ShoppingList(user);
        shoppingListRepository.save(shoppingList);
        return true;
    }

    /**
     * Adds a new user to an existing ShoppingList
     */
    @Transactional
    public void addUserToShoppingList(int userId, String userName, String email, String inviteCode) {
        LOGGER.info("Execute addUserToShoppingList({},{},{}, {}).", userId, userName, email, inviteCode);
        //TODO
    }

    /**
     * Remove user from shopping list and delete the list, when no other user left
     *
     * @param id of the user that was deleted
     */
    @Transactional
    public void deleteShoppingList(Integer id) {
        LOGGER.info("Execute deleteShoppingList({}).", id);
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findByUsers_id(user.getId());
            if (shoppingListOptional.isPresent()) {
                ShoppingList shoppingList = shoppingListOptional.get();
                if (shoppingList.getUserCount() < 2) {
                    shoppingListRepository.delete(shoppingList);
                } else {
                    shoppingList.removeUser(user);
                    //shoppingListRepository.save(shoppingList);
                }
                userRepository.deleteById(id);
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
    @Transactional(readOnly = true)
    public Set<ShoppingListProductDto> getAllProductsFromShoppingList(Integer id) {
        LOGGER.info("Execute getAllProductsFromShoppingList({}).", id);
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findByUsers_id(id);
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            Set<ShoppingListProduct> shoppingListProducts = shoppingListProductRepository.findAllByShoppingList_Id(shoppingList.getId());
            Set<ShoppingListProductDto> shoppingListProductDtos = new HashSet<>();
            for (ShoppingListProduct entrie : shoppingListProducts) {
                shoppingListProductDtos.add(new ShoppingListProductDto(entrie));
            }
            return shoppingListProductDtos;
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
    @Transactional
    public Boolean addProductToList(EntryDto entryDto) {
        LOGGER.info("Execute addProductToList({},{},{}).", entryDto.getUserId(), entryDto.getProductId(), entryDto.getAmount());
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findByUsers_id(entryDto.getUserId());
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            Set<ShoppingListProduct> shoppingListProducts = shoppingListProductRepository.findAllByShoppingList_Id(shoppingList.getId());
            for (ShoppingListProduct entry : shoppingListProducts) {
                if (entry.getProduct().getId().equals(entryDto.getProductId())) {
                    throw new RuntimeException("Product already exists on Shopping List");
                }
            }
            Optional<Product> productOptional = productRepository.findById(entryDto.getProductId());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                ShoppingListProduct shoppingListProduct = new ShoppingListProduct(product, shoppingList, entryDto.getAmount());
                shoppingListProductRepository.save(shoppingListProduct);
                return true;
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
    @Transactional
    public ShoppingListProductDto updateAmount(Integer id, Integer num, Integer amount) {
        //TODO: change to userid ?
        LOGGER.info("Execute updateAmount({}, {}, {}).", id, num, amount);
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(id);
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            Set<ShoppingListProduct> shoppingListProducts = shoppingListProductRepository.findAllByShoppingList_Id(shoppingList.getId());
            for (ShoppingListProduct i : shoppingListProducts) {
                if (i.getProduct().getId().equals(num)) {
                    i.setAmount(amount);
                    //shoppingListProductRepository.save(i);
                    return new ShoppingListProductDto(i);
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
     * @param productId
     * @param userId
     */
    @Transactional
    public void deleteProductFromList(Integer productId, Integer userId) {
        LOGGER.info("Execute deleteProductFromList({}, {}).", userId, productId);
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findByUsers_id(userId);
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            Set<ShoppingListProduct> shoppingListProducts = shoppingListProductRepository.findAllByShoppingList_Id(shoppingList.getId());
            for (ShoppingListProduct i : shoppingListProducts) {
                if (i.getProduct().getId().equals(productId)) {
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
     * Deletes a product from a shopping list
     * and sends the entry to the foodService
     *
     * @param entryDto
     */
    @Transactional
    public void deleteProductFromListAndSendFoodClient(EntryDto entryDto) {
        Boolean result = addFoodEntry(entryDto);
        if (result) {
            deleteProductFromList(entryDto.getProductId(), entryDto.getUserId());
        }
    }

    /**
     * This method is used to send an entry from the shopping list to the food service.
     * Uses the foodServiceClient to send an external request.
     *
     * @param entryDto
     */
    @Retryable(include = RetryableException.class,
            maxAttempts = 3, //first attempt and 2 retries
            backoff = @Backoff(delay = 100, maxDelay = 500))
    public Boolean addFoodEntry(EntryDto entryDto) {
        LOGGER.info("Execute addFoodEntry({},{}, {}).", entryDto.getProductId(), entryDto.getProductId(), entryDto.getAmount());
        foodServiceClient.postFoodEntry(entryDto);
        return true;
    }


}
