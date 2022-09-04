package de.fhms.sweng.einkaufslistenverwaltung;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.ProductController;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.security.JwtValidator;
import de.fhms.sweng.einkaufslistenverwaltung.model.ProductService;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.Product;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private ProductService productService;
    @MockBean
    private JwtValidator jwtValidator;

    private final String AUTH_HEADER = "Bearer ANY-JWT-STRING";

    private final String TEST_USER_EMAIL = "user@test.de";

    @BeforeEach
    public void setUp() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(TEST_USER_EMAIL)
                .password("***")
                .authorities(Role.ADMIN.getAuthority())
                .build();

        given(jwtValidator.isValidJWT(any(String.class))).willReturn(true);
        given(jwtValidator.getUserEmail(any(String.class))).willReturn(TEST_USER_EMAIL);
        given(jwtValidator.resolveToken(any(HttpServletRequest.class))).willReturn(AUTH_HEADER.substring(7));
        given(jwtValidator.getAuthentication(any(String.class))).willReturn(new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities()));
    }

    @Test
    public void getAllProducts() throws Exception {
        Set<Product> products = new HashSet<>();
        products.add(new Product("Test", 3, 4));
        given(this.productService.getAllProducts()).willReturn(products);
        this.mvc.perform(get("/rest/shoppingList/products/all")
                        .header("Authorization", this.AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getProductById() throws Exception {
        Product product = new Product("Test", 3, 4);
        given(this.productService.getProductById(1)).willReturn(product);
        this.mvc.perform(get("/rest/shoppingList/products/{id}", 1)
                        .header("Authorization", this.AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addProduct() throws Exception {
        Product product = new Product("Test", 3, 4);
        given(this.productService.addProduct(product)).willReturn(product);
        this.mvc.perform(post("/rest/shoppingList/products/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .header("Authorization", this.AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateProduct() throws Exception {
        Product product = new Product("Test", 3, 4);
        given(this.productService.updateProduct(product)).willReturn(product);
        this.mvc.perform(put("/rest/shoppingList/products/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .header("Authorization", this.AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteProduct() throws Exception {
        this.mvc.perform(delete("/rest/shoppingList/products/{id}", 1)
                        .header("Authorization", this.AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
