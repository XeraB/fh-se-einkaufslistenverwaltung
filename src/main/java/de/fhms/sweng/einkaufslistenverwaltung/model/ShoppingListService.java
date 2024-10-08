package de.fhms.sweng.einkaufslistenverwaltung.model;

import de.fhms.sweng.einkaufslistenverwaltung.inbound.types.EntryDto;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.ShoppingListProductDto;
import de.fhms.sweng.einkaufslistenverwaltung.model.exceptions.AlreadyExistException;
import de.fhms.sweng.einkaufslistenverwaltung.model.exceptions.ResourceNotFoundException;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ProductRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ShoppingListProductRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ShoppingListRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.UserRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.*;
import feign.RetryableException;
import org.hibernate.StaleStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
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
     * Creates a new ShoppingList and adds the user
     *
     * @param userEmail
     */
    @Transactional
    public Boolean addUserWithNewShoppingList(String userEmail) {
        LOGGER.info("Execute addUserWithNewShoppingList({}).", userEmail);
        User user = userRepository.findByEmail(userEmail);
        ShoppingList shoppingList = new ShoppingList(user);
        shoppingListRepository.save(shoppingList);
        return true;
    }

    /**
     * Adds a user to an existing ShoppingList
     *
     * @param userEmail
     * @param inviteCode for an existing ShoppingList
     */
    @Transactional
    public void addUserToShoppingList(String userEmail, String inviteCode) {
        LOGGER.info("Execute addUserToShoppingList({}, {}).", userEmail, inviteCode);
        User user = userRepository.findByEmail(userEmail);
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findByInviteCode(inviteCode);
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            shoppingList.addUser(user);
        } else {
            throw new ResourceNotFoundException("No ShoppingList found for requested Invitecode");
        }
    }

    /**
     * Remove user from shopping list and delete the list, when no other user left
     *
     * @param userEmail of the current user
     */
    @Transactional
    public void deleteShoppingList(String userEmail) {
        LOGGER.info("Execute deleteShoppingList({}).", userEmail);
        User user = userRepository.findByEmail(userEmail);
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findByUsers_id(user.getId());
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            if (shoppingList.getUserCount() < 2) {
                shoppingListProductRepository.deleteAllByShoppingList_Id(shoppingList.getId());
                shoppingListRepository.delete(shoppingList);
            } else {
                shoppingList.removeUser(user);
            }
            userRepository.deleteById(user.getId());
        } else {
            throw new ResourceNotFoundException("No ShoppingList Found for User");
        }
    }

    /**
     * Get all Products from a Shopping List from the database
     *
     * @param userEmail of the current user
     * @return a list with shopping lists
     */
    @Transactional(readOnly = true)
    public Set<ShoppingListProductDto> getAllProductsFromShoppingList(String userEmail) {
        LOGGER.info("Execute getAllProductsFromShoppingList({}).", userEmail);
        User user = userRepository.findByEmail(userEmail);
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findByUsers_id(user.getId());
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            Set<ShoppingListProduct> shoppingListProducts = shoppingListProductRepository.findAllByShoppingList_Id(shoppingList.getId());
            Set<ShoppingListProductDto> shoppingListProductDtos = new HashSet<>();
            for (ShoppingListProduct entrie : shoppingListProducts) {
                shoppingListProductDtos.add(new ShoppingListProductDto(entrie));
            }
            return shoppingListProductDtos;
        } else {
            throw new ResourceNotFoundException("No ShoppingList Found for User");
        }
    }

    /**
     * Adds a product to a shopping list
     *
     * @param userEmail of the current user
     * @param entryDto
     * @return
     */
    @Transactional
    public ShoppingListProductDto addProductToList(String userEmail, EntryDto entryDto) {
        LOGGER.info("Execute addProductToList({},{},{}).", userEmail, entryDto.getProductId(), entryDto.getAmount());
        User user = userRepository.findByEmail(userEmail);
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findByUsers_id(user.getId());
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            Set<ShoppingListProduct> shoppingListProducts = shoppingListProductRepository.findAllByShoppingList_Id(shoppingList.getId());
            for (ShoppingListProduct entry : shoppingListProducts) {
                if (entry.getProduct().getId().equals(entryDto.getProductId())) {
                    throw new AlreadyExistException("Product already exists on Shopping List");
                }
            }
            Optional<Product> productOptional = productRepository.findById(entryDto.getProductId());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                ShoppingListProduct shoppingListProduct = new ShoppingListProduct(product, shoppingList, entryDto.getAmount());
                if (entryDto.getUnit() != null) {
                    shoppingListProduct.setUnit(entryDto.getUnit());
                } else {
                    shoppingListProduct.setUnit(Unit.STUECK);
                }
                shoppingListProductRepository.save(shoppingListProduct);
                return new ShoppingListProductDto(shoppingListProduct);
            } else {
                throw new ResourceNotFoundException("Requested Product does not exist");
            }
        } else {
            throw new ResourceNotFoundException("No ShoppingList Found for User");
        }
    }

    /**
     * Changes the amount of a product on a shopping list
     *
     * @param userEmail of the current user
     * @param num       of the product
     * @param amount    to be set
     * @return the changed product entity
     */
    @Transactional
    public ShoppingListProductDto updateAmount(String userEmail, Integer num, Integer amount) {
        LOGGER.info("Execute updateAmount({}, {}, {}).", userEmail, num, amount);
        User user = userRepository.findByEmail(userEmail);
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findByUsers_id(user.getId());
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            Set<ShoppingListProduct> shoppingListProducts = shoppingListProductRepository.findAllByShoppingList_Id(shoppingList.getId());
            for (ShoppingListProduct i : shoppingListProducts) {
                if (i.getProduct().getId().equals(num)) {
                    i.setAmount(amount);
                    return new ShoppingListProductDto(i);
                }
            }
            throw new ResourceNotFoundException("Requested Product is not in Shopping List");
        } else {
            throw new ResourceNotFoundException("No ShoppingList Found for User");
        }
    }

    /**
     * Deletes a product from a shopping list
     *
     * @param userEmail of the current user
     * @param productId
     */
    @Transactional
    public void deleteProductFromList(String userEmail, Integer productId) {
        LOGGER.info("Execute deleteProductFromList({}, {}).", userEmail, productId);
        User user = userRepository.findByEmail(userEmail);
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findByUsers_id(user.getId());
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            Set<ShoppingListProduct> shoppingListProducts = shoppingListProductRepository.findAllByShoppingList_Id(shoppingList.getId());
            for (ShoppingListProduct i : shoppingListProducts) {
                if (i.getProduct().getId().equals(productId)) {
                    shoppingListProductRepository.delete(i);
                    return;
                }
            }
            throw new ResourceNotFoundException("Requested Product is not in Shopping List");
        } else {
            throw new ResourceNotFoundException("No ShoppingList Found for User");
        }
    }

    /**
     * Deletes a product from a shopping list
     * and sends the entry to the foodService
     *
     * @param userEmail of the current user
     * @param entryDto
     */
    @Transactional
    public void deleteProductFromListAndSendFoodClient(String userEmail, EntryDto entryDto) {
        Boolean result = addFoodEntry(entryDto);
        if (result) {
            deleteProductFromList(userEmail, entryDto.getProductId());
        } else {
            throw new RuntimeException("Product could not be added to Fridge/stock");
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
        FoodEntry foodEntry = foodServiceClient.postFoodEntry(entryDto);
        if (foodEntry != null) {
            return true;
        } else {
            return false;
        }
    }


}
