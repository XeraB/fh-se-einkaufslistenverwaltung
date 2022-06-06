package de.fhms.sweng.einkaufslistenverwaltung;

import de.fhms.sweng.einkaufslistenverwaltung.inbound.EntryDto;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.ShoppingListProductDto;
import de.fhms.sweng.einkaufslistenverwaltung.model.*;
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
import static org.junit.jupiter.api.Assertions.*;
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
    private static final Integer TEST_LISTID = 1;
    private static final Integer TEST_ID = 1;
    private static final String TEST_NAME = "Apfel";
    private static final Integer TEST_TIME = 5;
    private static final Integer TEST_PRICE = 4;
    private static final Integer TEST_AMOUNT = 4;
    private static final Integer TEST_AMOUNTNEW = 5;

    @BeforeEach
    void setUp() {
        this.shoppingListService = new ShoppingListService(shoppingListRepository, shoppingListProductRepository, productRepository, userRepository, foodServiceClient);
        this.user = new User(TEST_USERID, TEST_USERNAME, TEST_USERMAIL);
        this.shoppingList = new ShoppingList(this.user);
        this.shoppingList.setId(TEST_LISTID);
        this.product = new Product(TEST_NAME, TEST_TIME, TEST_PRICE);
        this.product.setId(TEST_ID);
        this.shoppingListProduct = new ShoppingListProduct(this.product, this.shoppingList, TEST_AMOUNT);
        this.entries = new HashSet<ShoppingListProduct>();
        this.entryDto = new EntryDto(TEST_ID, TEST_USERID, TEST_AMOUNT);
    }

    @Test
    public void addUserWithNewShoppingList() {
        Boolean result = shoppingListService.addUserWithNewShoppingList(TEST_USERID, TEST_USERNAME, TEST_USERMAIL);
        assertTrue(result);
    }

    @Test
    public void addUserToShoppingList() {
        //TODO
    }

    @Test
    public void deleteShoppingList() {
        given(userRepository.findById(TEST_USERID)).willReturn(Optional.of(user));
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        shoppingListService.deleteShoppingList(TEST_USERID);
        Mockito.verify(userRepository, times(1)).deleteById(TEST_USERID);
    }

    @Test
    public void getAllProductsFromShoppingList() {
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        given(shoppingListProductRepository.findAllByShoppingList_Id(shoppingList.getId())).willReturn(entries);
        entries.add(shoppingListProduct);
        Set<ShoppingListProductDto> listProductDtos = shoppingListService.getAllProductsFromShoppingList(TEST_USERID);
        assertFalse(listProductDtos.isEmpty());
    }

    @Test
    public void addProductToList() {

    }

    @Test
    public void addProductToList2() {
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        given(shoppingListProductRepository.findAllByShoppingList_Id(TEST_LISTID)).willReturn(entries);
        given(productRepository.findById(TEST_ID)).willReturn(Optional.of(product));
        assertFalse(shoppingListService.getAllProductsFromShoppingList(TEST_USERID).contains(shoppingListProduct));

        Boolean result = shoppingListService.addProductToList(new EntryDto(TEST_ID, TEST_USERID, TEST_AMOUNT));
        assertTrue(result);
    }

    @Test
    public void updateAmount() {
        given(shoppingListRepository.findById(TEST_LISTID)).willReturn(Optional.of(shoppingList));
        given(this.shoppingListProductRepository.findAllByShoppingList_Id(TEST_LISTID)).willReturn(entries);
        entries.add(shoppingListProduct);
        ShoppingListProductDto entry = shoppingListService.updateAmount(TEST_LISTID, TEST_ID, TEST_AMOUNTNEW);
        assertThat(entry.getAmount(), is(TEST_AMOUNTNEW));
    }

    @Test
    public void deleteProductFromListAndSendFoodClient() {
        given(foodServiceClient.postFoodEntry(entryDto)).willReturn(new FoodEntry(1, 1, LocalDate.of(2022, 5, 27), true));
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        given(shoppingListProductRepository.findAllByShoppingList_Id(TEST_LISTID)).willReturn(entries);
        entries.add(shoppingListProduct);

        shoppingListService.deleteProductFromListAndSendFoodClient(entryDto);
        Mockito.verify(shoppingListProductRepository, times(1)).delete(shoppingListProduct);
        Mockito.verify(foodServiceClient, times(1)).postFoodEntry(entryDto);
    }

    @Test
    public void deleteProductFromList() {
        given(shoppingListRepository.findByUsers_id(TEST_USERID)).willReturn(Optional.of(shoppingList));
        given(shoppingListProductRepository.findAllByShoppingList_Id(TEST_LISTID)).willReturn(entries);
        entries.add(shoppingListProduct);
        shoppingListService.deleteProductFromList(entryDto.getProductId(), entryDto.getUserId());
        Mockito.verify(shoppingListProductRepository, times(1)).delete(shoppingListProduct);
    }

    @Test
    public void addFoodEntry() {
        given(foodServiceClient.postFoodEntry(entryDto)).willReturn(new FoodEntry(1, 1, LocalDate.of(2022, 5, 27), true));
        Boolean result = shoppingListService.addFoodEntry(entryDto);
        assertTrue(result);
        Mockito.verify(foodServiceClient, times(1)).postFoodEntry(entryDto);
    }
}
