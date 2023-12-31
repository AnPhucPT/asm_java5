package com.example.anphuc.specification;

import com.example.anphuc.model.Category;
import com.example.anphuc.payload.request.ProductQueryParam;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.anphuc.model.Product;

@Component
public class ProductSpecification {
    public Specification<Product> hasNameLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
    }

    public Specification<Product> priceGreaterThan(Integer minPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("price"), minPrice);
    }

    public Specification<Product> priceLessThan(Integer maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public Specification<Product> belongToCategory(Integer categoryId) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Category> joinTable = root.join("category");
            return criteriaBuilder.equal(joinTable.get("id"), categoryId);
        };
    }

    public Specification<Product> isAvailable(Boolean available) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("available"), available);
    }

    public Specification<Product> getProductSpecification(ProductQueryParam productQueryParam) {
        Specification<Product> spec = Specification.where(null);
        if (productQueryParam.getKeyword() != null) {
            spec = spec.and(hasNameLike(productQueryParam.getKeyword()));

        }
        if (productQueryParam.getMaxPrice() != null) {
            spec = spec.and(priceLessThan(productQueryParam.getMaxPrice()));
        }
        if (productQueryParam.getMinPrice() != null) {
            spec = spec.and(priceGreaterThan(productQueryParam.getMinPrice()));
        }
        if (productQueryParam.getCategoryId() != null) {
            spec = spec.and(belongToCategory(productQueryParam.getCategoryId()));
        }
        if (productQueryParam.getAvailable() != null) {
            spec = spec.and(isAvailable(productQueryParam.getAvailable()));
        }
        return spec;
    }
}
