package de.fhms.sweng.einkaufslistenverwaltung;

import de.fhms.sweng.einkaufslistenverwaltung.model.Product;
import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingList;
import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListProduct;
import de.fhms.sweng.einkaufslistenverwaltung.model.User;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ProductRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ShoppingListProductRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ShoppingListRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PersistenceTests {
    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingListProductRepository shoppingListProductRepository;

    /* Test own methods of the ShoppingListRepository
        findByUsers_Id(1) should find List with Id = 1
        findByUsers_Id(5) should not find any ShoppingList
     */
    @Test
    public void findByUsers_IdShouldWork() {
        Optional<ShoppingList> shoppingListOptional1 = shoppingListRepository.findByUsers_id(1);
        assertTrue(shoppingListOptional1.isPresent());
        assertEquals(1, shoppingListOptional1.get().getId());
        Optional<ShoppingList> shoppingListOptional2 = shoppingListRepository.findByUsers_id(5);
        assertTrue(!shoppingListOptional2.isPresent());
    }

    /* Test own methods of the UserRepository
       findByEmail("test1@test.com") should find User with Id = 1
       findByEmail("test4@test.com") should not find any User
     */
    @Test
    public void findByEmailShouldWork() {
        User user1 = userRepository.findByEmail("test1@test.com");
        assertEquals(1, user1.getId());
        User user = userRepository.findByEmail("test14@test.com");
        assertNull(user);
    }

    /* Test own methods of the ProductRepository
       findByName("Banane") should find Product with Id = 2
       findByEmail("Paprika") should not find any User
     */
    @Test
    public void findByNameShouldWork() {
        Optional<Product> product1 = productRepository.findByName("Banane");
        assertTrue(product1.isPresent());
        assertEquals(2, product1.get().getId());
        Optional<Product> product2 = productRepository.findByName("Paprika");
        assertTrue(!product2.isPresent());
    }

    /* Test own methods of the ProductRepository
        getAll() should find 3 Products
     */
    @Test
    public void getAllShouldWork() {
        Optional<Set<Product>> products = productRepository.getAll();
        assertTrue(products.isPresent());
        assertEquals(3, products.get().size());
        Iterator<Product> productIterator = products.get().iterator();
        assertEquals(1, productIterator.next().getId());
        assertEquals(2, productIterator.next().getId());
        assertEquals(3, productIterator.next().getId());
        assertFalse(productIterator.hasNext());
    }

    /* Test own methods of the ShoppingListProductRepository
        findAllByShoppingList_Id() should find 3 Entries
     */
    @Test
    public void findAllByShoppingList_IdShouldWork() {
        Set<ShoppingListProduct> entries = shoppingListProductRepository.findAllByShoppingList_Id(1);
        assertEquals(2, entries.size());
        Iterator<ShoppingListProduct> entryIterator = entries.iterator();
        assertEquals(1, entryIterator.next().getProduct().getId());
        assertEquals(2, entryIterator.next().getProduct().getId());
        assertFalse(entryIterator.hasNext());
    }

    /* Test methods of the ShoppingListRepository
     */
    @Test
    public void saveShouldWork() {
        User user = new User(1, "Test", "test@test.com");
        ShoppingList shoppingList = new ShoppingList(user);
        System.out.print("Service: addUser() " + shoppingList.toString());
        ShoppingList shoppingList1 = shoppingListRepository.save(shoppingList);
        assertTrue(shoppingList1.getId() != null);
    }

}
