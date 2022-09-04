package de.fhms.sweng.einkaufslistenverwaltung.inbound;

import de.fhms.sweng.einkaufslistenverwaltung.model.ProductService;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class ProductController implements ProductControllerApi {

    private ProductService productService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public Set<Product> getAllProducts() {
        LOGGER.info("GET-Request of all Products received.");
        return this.productService.getAllProducts();
    }

    public Product getProductById(@PathVariable Integer id) {
        LOGGER.info("GET-Request of Product {} received.", id);
        return this.productService.getProductById(id);
    }

    public Product addProduct(@RequestBody Product product) {
        LOGGER.info("POST-Request of Product received.");
        return this.productService.addProduct(product);
    }

    public Product updateProduct(@RequestBody Product product) {
        LOGGER.info("PUT-Request of Product received.");
        return this.productService.updateProduct(product);
    }

    public void deleteProduct(@PathVariable Integer id) {
        LOGGER.info("DELETE-Request of Product {} received.", id);
        this.productService.deleteProduct(id);
    }
}
