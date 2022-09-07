package de.fhms.sweng.einkaufslistenverwaltung;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.ShoppingListController;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.ShoppingListProductDto;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.security.JwtValidator;
import de.fhms.sweng.einkaufslistenverwaltung.inbound.types.EntryDto;
import de.fhms.sweng.einkaufslistenverwaltung.model.ShoppingListService;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShoppingListController.class)
public class ShoppingListControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShoppingListService shoppingListService;

    @MockBean
    private JwtValidator jwtValidator;

    private final String AUTH_HEADER = "Bearer ANY-JWT-STRING";
    ;
    private final String TEST_USER_EMAIL = "user@test.de";
    private final String TEST_INVITE_CODE = "jDz6u4";

    @BeforeEach
    public void setUp() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(TEST_USER_EMAIL)
                .password("***")
                .authorities(Role.USER.getAuthority())
                .build();

        given(jwtValidator.isValidJWT(any(String.class))).willReturn(true);
        given(jwtValidator.getUserEmail(any(String.class))).willReturn(TEST_USER_EMAIL);
        given(jwtValidator.resolveToken(any(HttpServletRequest.class))).willReturn(AUTH_HEADER.substring(7));
        given(jwtValidator.getAuthentication(any(String.class))).willReturn(new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities()));
    }

    @Test
    public void addUserToNewOrExistingListWith() throws Exception {
        this.mvc.perform(post("/rest/shoppingList/addUser")
                        .header("Authorization", this.AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk());
        Mockito.verify(shoppingListService, times(1)).addUserWithNewShoppingList(TEST_USER_EMAIL);
    }

    @Test
    public void addUserToNewOrExistingListWithInviteCode() throws Exception {
        this.mvc.perform(post("/rest/shoppingList/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TEST_INVITE_CODE).header("Authorization", this.AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk());
        Mockito.verify(shoppingListService, times(1)).addUserToShoppingList(TEST_USER_EMAIL, TEST_INVITE_CODE);
    }

    @Test
    public void getAllProductsFromShoppingListByUserId() throws Exception {
        Set<ShoppingListProductDto> entries = new HashSet<>();
        entries.add(new ShoppingListProductDto(1, 1, 3, "Brot", 4, 2));
        given(this.shoppingListService.getAllProductsFromShoppingList("test1@test.com")).willReturn(entries);
        this.mvc.perform(get("/rest/shoppingList/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1").header("Authorization", this.AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addProductToListByUser() throws Exception {
        EntryDto entryDto = new EntryDto(1, 2);
        ShoppingListProductDto entry = new ShoppingListProductDto(1, 1, 2, "Brot", 4, 2);
        given(this.shoppingListService.addProductToList(TEST_USER_EMAIL, entryDto)).willReturn(entry);
        this.mvc.perform(post("/rest/shoppingList/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entryDto))
                        .header("Authorization", this.AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateAmount() throws Exception {
        ShoppingListProductDto entry = new ShoppingListProductDto(1, 1, 2, "Brot", 4, 2);
        given(this.shoppingListService.updateAmount(TEST_USER_EMAIL, 1, 2)).willReturn(entry);
        this.mvc.perform(put("/rest/shoppingList/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entry))
                        .header("Authorization", this.AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteProductFromListAndSendFoodClient() throws Exception {
        EntryDto entryDto = new EntryDto(1, 3);
        this.mvc.perform(delete("/rest/shoppingList/food")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entryDto))
                        .header("Authorization", this.AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteProductFromList() throws Exception {
        EntryDto entryDto = new EntryDto(1, 2);
        this.mvc.perform(delete("/rest/shoppingList/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entryDto))
                        .header("Authorization", this.AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteShoppingList() throws Exception {
        this.mvc.perform(delete("/rest/shoppingList/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(1))
                        .header("Authorization", this.AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
