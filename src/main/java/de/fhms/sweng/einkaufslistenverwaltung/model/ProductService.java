package de.fhms.sweng.einkaufslistenverwaltung.model;

import org.hibernate.StaleStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.Optional;
import java.util.Set;

@Service
@Retryable(include = {OptimisticLockException.class, StaleStateException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100, maxDelay = 500))
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
    @Transactional(readOnly = true)
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
    @Transactional
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
    @Transactional
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
    @Transactional
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
     * @param updatedProduct
     * @return the updated product
     */
    @Transactional
    public Product updateProduct(Product updatedProduct) {
        LOGGER.info("Execute updateProduct({}).", updatedProduct.getName());
        Optional<Product> productOptional = productRepository.findById(updatedProduct.getId());
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (updatedProduct.equals(product)) {
                return updatedProduct;
            }
            productRepository.save(updatedProduct);
            var event = new ProductAddedEvent(product.getId(), product.getName(), product.getBestBeforeTime(), product.getPrice());
            var published = this.eventPublisher.publishEvent(event);
            if (!published) {
                LOGGER.error("Event could not be published. Performing rollback.");
                productRepository.save(product);
                throw new RuntimeException("Error while publishing Product");
            }
            LOGGER.info("Product {} sucessfully updated.", product.getName());
            return product;
        } else {
            LOGGER.error("Product could not be added.");
            throw new RuntimeException("Error while updating Product");
        }
    }
}
