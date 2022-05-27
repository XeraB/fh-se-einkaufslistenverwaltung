package de.fhms.sweng.einkaufslistenverwaltung;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.EntryDto;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.ShoppingListController;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.ShoppingListProductDto;
import de.fhms.sweng.einkaufslistenverwaltung.model.Product;
import de.fhms.sweng.einkaufslistenverwaltung.model.ProductService;
import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShoppingListController.class)
public class ShoppingListControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShoppingListService shoppingListService;

    @MockBean
    private ProductService productService;

    @Test
    public void getAllProductsFromShoppingListByUserId() throws Exception {
        Set<ShoppingListProductDto> entries = new HashSet<>();
        entries.add(new ShoppingListProductDto(1, 1, 3, "Brot", 4, 2));
        given(this.shoppingListService.getAllProductsFromShoppingList(1)).willReturn(entries);
        this.mvc.perform(get("/rest/shoppingList/all").contentType(MediaType.APPLICATION_JSON).content("1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addProductToListByUser() throws Exception {
        EntryDto entryDto = new EntryDto(1, 1, 2);
        given(this.shoppingListService.addProductToList(entryDto)).willReturn(true);
        this.mvc.perform(post("/rest/shoppingList/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(entryDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateAmount() throws Exception {
        ShoppingListProductDto entry = new ShoppingListProductDto(1, 1, 2, "Brot", 4, 2);
        given(this.shoppingListService.updateAmount(1, 1, 2)).willReturn(entry);
        this.mvc.perform(put("/rest/shoppingList/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(entry)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteProductFromList() throws Exception {
        EntryDto entryDto = new EntryDto(1, 1, 2);
        this.mvc.perform(delete("/rest/shoppingList/entry").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(entryDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteShoppingList() throws Exception {
        this.mvc.perform(delete("/rest/shoppingList/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(1)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getAllProducts() throws Exception {
        Set<Product> products = new HashSet<>();
        products.add(new Product("Test", 3, 4));
        given(this.productService.getAllProducts()).willReturn(products);
        this.mvc.perform(get("/rest/shoppingList/products/all"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getProductById() throws Exception {
        Product product = new Product("Test", 3, 4);
        given(this.productService.getProductById(1)).willReturn(product);
        this.mvc.perform(get("/rest/shoppingList/products/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addProduct() throws Exception {
        Product product = new Product("Test", 3, 4);
        given(this.productService.addProduct(product)).willReturn(product);
        this.mvc.perform(post("/rest/shoppingList/products/new").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateProduct() throws Exception {
        Product product = new Product("Test", 3, 4);
        given(this.productService.updateProduct(product)).willReturn(product);
        this.mvc.perform(put("/rest/shoppingList/products/update").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteProduct() throws Exception {
        this.mvc.perform(delete("/rest/shoppingList/products/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
