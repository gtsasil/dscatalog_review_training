package com.gtsasil.dscatalogReview.repositories;

import com.gtsasil.dscatalogReview.enties.Role;
import com.gtsasil.dscatalogReview.enties.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
