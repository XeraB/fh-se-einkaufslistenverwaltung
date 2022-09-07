package de.fhms.sweng.einkaufslistenverwaltung.model;

import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductAddedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductDeletedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.events.product.ProductUpdatedEvent;
import de.fhms.sweng.einkaufslistenverwaltung.model.exceptions.AlreadyExistException;
import de.fhms.sweng.einkaufslistenverwaltung.model.exceptions.ResourceNotFoundException;
import de.fhms.sweng.einkaufslistenverwaltung.model.repository.ProductRepository;
import de.fhms.sweng.einkaufslistenverwaltung.model.types.Product;
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
import java.util.stream.Collectors;

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
            throw new ResourceNotFoundException("Requested Product doesn't exist");
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
            throw new ResourceNotFoundException("No Product found");
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
            Boolean empty = products.stream()
                    .filter(p -> p.getName().equals(product.getName()))
                    .collect(Collectors.toList()).isEmpty();
            if (!empty) {
                LOGGER.error("Product already exists.");
                throw new AlreadyExistException("Product already exists");
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
            throw new ResourceNotFoundException("Requested Product was not found");
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
            var event = new ProductUpdatedEvent(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getBestBeforeTime(), updatedProduct.getPrice());
            var published = this.eventPublisher.publishEvent(event);
            if (!published) {
                LOGGER.error("Event could not be published. Performing rollback.");
                productRepository.save(product);
                throw new RuntimeException("Error while publishing Product");
            }
            LOGGER.info("Product {} sucessfully updated.", updatedProduct.getName());
            return updatedProduct;
        } else {
            LOGGER.error("Product could not be found.");
            throw new RuntimeException("Product doesn't exist. Please add Product first.");
        }
    }

    /**
     * Saves a product received as a ProductAddedEvent
     *
     * @param id
     * @param name
     * @param bestBeforeTime
     * @param price
     */
    @Transactional
    public void addProductFromEvent(Integer id, String name, Integer bestBeforeTime, Integer price) {
        Product product = new Product(name, bestBeforeTime, price);
        product.setId(id);
        productRepository.save(product);
    }

    /**
     * Updates a product received as a ProductUpdatedEvent
     *
     * @param id
     * @param name
     * @param bestBeforeTime
     * @param price
     */
    @Transactional
    public void updateProductFromEvent(Integer id, String name, Integer bestBeforeTime, Integer price) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(name);
            product.setBestBeforeTime(bestBeforeTime);
            product.setPrice(price);
        } else {
            LOGGER.error("Product could not be found.");
            throw new RuntimeException("Product doesn't exist. Please add Product first.");
        }
    }

    /**
     * Deletes a product received as a ProductDeletedEvent
     *
     * @param id
     */
    @Transactional
    public void deleteProductFromEvent(Integer id) {
        productRepository.deleteById(id);
    }
}
