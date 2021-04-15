package com.gtsasil.dscatalogReview.repositories;

import com.gtsasil.dscatalogReview.enties.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
