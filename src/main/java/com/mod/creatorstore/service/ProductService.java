package com.mod.creatorstore.service;

import com.mod.creatorstore.entities.Product;
import com.mod.creatorstore.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct( Product product){
        return productRepository.save(product);
    }


    public Product updateProduct(Long id, Product product){

       Product existingProduct =  productRepository.findById(id)
               .orElseThrow(
                       ()-> new RuntimeException("Product not found with id "+ id)
               );

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());

        existingProduct.setCategory(product.getCategory());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStockQuantity(product.getStockQuantity());

        return productRepository.save(existingProduct);
    }

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Product not found with id "+ id)
        );
    }

    public boolean deleteProduct(Long id){
        Boolean found = productRepository.findById(id).map(
                product -> {
                    productRepository.delete(product);
                    return true;
                }
        ).orElse(
                 false
        );

        return found;
    }
}
