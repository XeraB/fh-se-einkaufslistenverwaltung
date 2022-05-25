package de.fhms.sweng.einkaufslistenverwaltung.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private EventPublisher eventPublisher;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public ProductService(ProductRepository productRepository, EventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Find a certain product in the database.
     *
     * @param id of the requested product
     * @return a product
     */
    public Product getProductById(Integer id) {
        LOGGER.info("Execute getProductById({}).", id);
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
     *
     * @return a list of all products
     */
    public Set<Product> getAllProducts() {
        LOGGER.info("Execute getAllProducts().");
        Optional<Set<Product>> productOptional = productRepository.getAll();
        if (productOptional.isPresent()) {
            return productOptional.get();
        } else {
            throw new ResourceNotFoundException("No Product in DB");
        }
    }

    /**
     * Adds a new product
     *
     * @param product new product
     * @return the new product entity
     */
    public Product addProduct(Product product) {
        LOGGER.info("Execute addProduct({}).", product.getName());
        Optional<Set<Product>> productOptional = productRepository.getAll();
        if (productOptional.isPresent()) {
            Set<Product> products = productOptional.get();
            for (Product i : products) {
                if (i.getName().equals(product.getName())) {
                    LOGGER.error("Product already exists.");
                    throw new AlreadyExistException("Product already exists");
                }
            }
            products.add(product);
            productRepository.save(product);
            var event = new ProductAddedEvent(product.getId(), product.getName(), product.getBestBeforeTime(), product.getPrice());
            var published = this.eventPublisher.publishEvent(event);
            if (!published) {
                LOGGER.error("Event could not be published. Performing rollback.");
                products.remove(product);
                productRepository.delete(product);
                throw new RuntimeException("Error while publishing Product");
            }
            LOGGER.info("Product {} sucessfully added.", product.getName());
            return product;
        } else {
            LOGGER.error("Product could not be added.");
            throw new RuntimeException("Error while adding Product");
        }
    }

    /**
     * Deletes a product from the database
     *
     * @param id of the product that should be deleted
     */
    public void deleteProduct(Integer id) {
        LOGGER.info("Execute deleteProduct({}).", id);
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            productRepository.delete(product);
            var event = new ProductDeletedEvent(product.getId(), product.getName());
            var published = this.eventPublisher.publishEvent(event);
            if (!published) {
                LOGGER.error("Event could not be published. Performing rollback.");
                productRepository.save(product);
                throw new RuntimeException("Error while publishing Product");
            }
        } else {
            throw new ResourceNotFoundException("Requested Product is not in DB");
        }
    }
    /**
     * Update a product
     *
     * @param product
     * @return the updated product
     */
    public Product updateProduct(Product product) {
        //TODO:
        return null;
    }
}
