package de.fhms.sweng.einkaufslistenverwaltung.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Find a certain product in the database.
     * @param id of the requested product
     * @return a product
     */
    public Product getProductById(Integer id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return product;
        } else {
            throw new ResourceNotFoundException("Requested Product is not in DB");
        }
    }

    /**
     * Find a certain product in the database.
     * @return a list of all products
     */
    public List<Product> getAllProducts() {
        Optional<List<Product>> productOptional = productRepository.getAll();
        if (productOptional.isPresent()) {
            return productOptional.get();
        } else {
            throw new ResourceNotFoundException("No Product in DB");
        }
    }

    /**
     * Adds a new product
     * @param product new product
     * @return the new product entity
     */
    public Product addProduct(Product product) {
        Optional<List<Product>> productOptional = productRepository.getAll();
        if (productOptional.isPresent()) {
            List<Product> products = productOptional.get();
            for (Product i : products) {
                if (i.getName().equals(product.getName())) {
                    throw new AlreadyExistException("Product already exists");
                }
            }
            products.add(product);
            productRepository.save(product);
            return product;
        } else {
            throw new RuntimeException("Error while adding Product");
        }
    }

    /**
     * Deletes a product from the database
     *
     * @param id of the product that should be deleted
     */
    public void deleteProduct(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.delete(product.get());
        } else {
            throw new ResourceNotFoundException("Requested Product is not in DB");
        }
    }
}
