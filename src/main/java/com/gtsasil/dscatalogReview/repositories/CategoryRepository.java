package com.gtsasil.dscatalogReview.repositories;

import com.gtsasil.dscatalogReview.enties.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
