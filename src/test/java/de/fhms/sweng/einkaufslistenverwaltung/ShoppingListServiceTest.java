package de.fhms.sweng.einkaufslistenverwaltung;

import de.fhms.sweng.einkaufslistenverwaltung.inbound.types.EntryDto;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.ShoppingListProductDto;
import de.fhms.sweng.einkaufslistenverwaltung.model.*;
import de.fhms.sweng.einkaufslistenverwaltung.model.exceptions.AlreadyExistException;
import de.fhms.sweng.einkaufslistenverwaltung.model.exceptions.ResourceNotFoundException;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ProductRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ShoppingListProductRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ShoppingListRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.UserRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ShoppingListServiceTest {

    @Mock
    private ShoppingListRepository shoppingListRepository;

    @Mock
    private ShoppingListProductRepository shoppingListProductRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FoodServiceClient foodServiceClient;

    private ShoppingListService shoppingListService;

    private User user;

    private ShoppingList shoppingList;

    private Product product;

    private ShoppingListProduct shoppingListProduct;
    private Set<ShoppingListProduct> entries;
    private EntryDto entryDto;

    private static final Integer TEST_USERID = 8;
    private static final String TEST_USERNAME = "Test User";
    private static final String TEST_USERMAIL = "test@test.de";
    private static final String TEST_INVITECODE = "Ifn79j";
    private static final Integer TEST_LISTID = 1;
    private static final Integer TEST_ID = 1;
    private static final String TEST_NAME = "Apfel";
    private static final Integer TEST_TIME = 5;
    private static final Integer TEST_PRICE = 4;
    private static final Integer TEST_AMOUNT = 4;
    private static final Integer TEST_AMOUNTNEW = 5;
    private static final Unit TEST_UNIT = Unit.STUECK;

    @BeforeEach
    void setUp() {
        this.shoppingListService = new ShoppingListService(shoppingListRepository, shoppingListProductRepository, productRepository, userRepository, foodServiceClient);
        this.user = new User(TEST_USERID, TEST_USERNAME, TEST_USERMAIL, Role.USER);
        this.shoppingList = new ShoppingList(this.user);
        this.shoppingList.setId(TEST_LISTID);
        this.product = new Product(TEST_NAME, TEST_TIME, TEST_PRICE);
        this.product.setId(TEST_ID);
        this.shoppingListProduct = new ShoppingListProduct(this.product, this.shoppingList, TEST_AMOUNT);
        this.entries = new HashSet<ShoppingListProduct>();
        this.entryDto = new EntryDto(TEST_ID, TEST_AMOUNT, TEST_UNIT);
    }

    @Test
    public void addUserWithNewShoppingList() {
        Boolean result = shoppingListService.addUserWithNewShoppingList(TEST_USERMAIL);
        assertTrue(result);
    }

    @Test
    public void addUserToShoppingList() {
        ShoppingList newShoppingList = new ShoppingList(new User(2, "OtherTestUser", "test@email", Role.USER));
        given(shoppingListRepository.findByInviteCode(TEST_INVITECODE)).willReturn(Optional.of(newShoppingList));

        Assertions.assertDoesNotThrow(() -> {
            shoppingListService.addUserToShoppingList(TEST_USERMAIL, TEST_INVITECODE);
        });
        Mockito.verify(shoppingListRepository, times(1)).findByInviteCode(TEST_INVITECODE);
    }

    @Test
    public void addUserToShoppingListExceptionNoShoppingList() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            shoppingListService.addUserToShoppingList(TEST_USERMAIL, TEST_INVITECODE);
        });
    }

    @Test
    public void deleteShoppingList() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        shoppingListService.deleteShoppingList(TEST_USERMAIL);
        Mockito.verify(userRepository, times(1)).deleteById(TEST_USERID);
    }

    @Test
    public void deleteShoppingListExceptionNoShoppingList() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            shoppingListService.deleteShoppingList(TEST_USERMAIL);
        });
    }

    @Test
    public void getAllProductsFromShoppingList() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        given(shoppingListProductRepository.findAllByShoppingList_Id(TEST_LISTID)).willReturn(entries);
        entries.add(shoppingListProduct);
        Set<ShoppingListProductDto> listProductDtos = shoppingListService.getAllProductsFromShoppingList(TEST_USERMAIL);
        assertFalse(listProductDtos.isEmpty());
    }

    @Test
    public void getAllProductsFromShoppingListException() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            shoppingListService.getAllProductsFromShoppingList(TEST_USERMAIL);
        });
    }

    @Test
    public void addProductToListProductAlreadyExists() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        given(shoppingListProductRepository.findAllByShoppingList_Id(TEST_LISTID)).willReturn(entries);
        entries.add(shoppingListProduct);

        Assertions.assertThrows(AlreadyExistException.class, () -> {
            shoppingListService.addProductToList(TEST_USERMAIL, new EntryDto(TEST_ID, TEST_AMOUNT, TEST_UNIT));
        });
    }

    @Test
    public void addProductToList2() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        given(shoppingListProductRepository.findAllByShoppingList_Id(TEST_LISTID)).willReturn(entries);
        given(productRepository.findById(TEST_ID)).willReturn(Optional.of(product));
        assertFalse(shoppingListService.getAllProductsFromShoppingList(TEST_USERMAIL).contains(shoppingListProduct));

        ShoppingListProductDto result = shoppingListService.addProductToList(TEST_USERMAIL, new EntryDto(TEST_ID, TEST_AMOUNT, TEST_UNIT));
        assertTrue(result != null);
    }

    @Test
    public void addProductToListExceptionNoProduct() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        given(shoppingListProductRepository.findAllByShoppingList_Id(TEST_LISTID)).willReturn(entries);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            shoppingListService.addProductToList(TEST_USERMAIL, new EntryDto(TEST_ID, TEST_AMOUNT, TEST_UNIT));
        });
    }

    @Test
    public void addProductToListExceptionNoShoppingList() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            shoppingListService.addProductToList(TEST_USERMAIL, new EntryDto(TEST_ID, TEST_AMOUNT, TEST_UNIT));
        });
    }

    @Test
    public void updateAmount() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        given(this.shoppingListProductRepository.findAllByShoppingList_Id(TEST_LISTID)).willReturn(entries);
        entries.add(shoppingListProduct);
        ShoppingListProductDto entry = shoppingListService.updateAmount(TEST_USERMAIL, TEST_ID, TEST_AMOUNTNEW);
        assertThat(entry.getAmount(), is(TEST_AMOUNTNEW));
    }

    @Test
    public void updateAmountExceptionNoProduct() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        given(this.shoppingListProductRepository.findAllByShoppingList_Id(TEST_LISTID)).willReturn(entries);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            shoppingListService.updateAmount(TEST_USERMAIL, TEST_ID, TEST_AMOUNTNEW);
        });
    }

    @Test
    public void updateAmountExceptionNoShoppingList() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            shoppingListService.updateAmount(TEST_USERMAIL, TEST_ID, TEST_AMOUNTNEW);
        });
    }

    @Test
    public void deleteProductFromListAndSendFoodClient() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        given(foodServiceClient.postFoodEntry(entryDto)).willReturn(new FoodEntry(1, 1, LocalDate.of(2022, 5, 27), true));
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        given(shoppingListProductRepository.findAllByShoppingList_Id(TEST_LISTID)).willReturn(entries);
        entries.add(shoppingListProduct);

        shoppingListService.deleteProductFromListAndSendFoodClient(TEST_USERMAIL, entryDto);
        Mockito.verify(shoppingListProductRepository, times(1)).delete(shoppingListProduct);
        Mockito.verify(foodServiceClient, times(1)).postFoodEntry(entryDto);
    }

    @Test
    public void deleteProductFromListAndSendFoodClientException() {
        given(foodServiceClient.postFoodEntry(entryDto)).willReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> {
            shoppingListService.deleteProductFromListAndSendFoodClient(TEST_USERMAIL, entryDto);
        });
    }

    @Test
    public void deleteProductFromList() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        given(shoppingListProductRepository.findAllByShoppingList_Id(TEST_LISTID)).willReturn(entries);
        entries.add(shoppingListProduct);
        shoppingListService.deleteProductFromList(TEST_USERMAIL, entryDto.getProductId());
        Mockito.verify(shoppingListProductRepository, times(1)).delete(shoppingListProduct);
    }

    @Test
    public void deleteProductFromListExceptionNoProduct() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        given(shoppingListProductRepository.findAllByShoppingList_Id(TEST_LISTID)).willReturn(entries);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            shoppingListService.deleteProductFromList(TEST_USERMAIL, entryDto.getProductId());
        });
    }

    @Test
    public void deleteProductFromListExceptionNoShoppingList() {
        given(userRepository.findByEmail(TEST_USERMAIL)).willReturn(user);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            shoppingListService.deleteProductFromList(TEST_USERMAIL, entryDto.getProductId());
        });
    }

    @Test
    public void addFoodEntry() {
        given(foodServiceClient.postFoodEntry(entryDto)).willReturn(new FoodEntry(1, 1, LocalDate.of(2022, 5, 27), true));
        Boolean result = shoppingListService.addFoodEntry(entryDto);
        assertTrue(result);
        Mockito.verify(foodServiceClient, times(1)).postFoodEntry(entryDto);
    }
}
