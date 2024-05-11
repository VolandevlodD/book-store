package org.example.bookstore.repository;

import java.util.Optional;
import org.example.bookstore.model.Role;
import org.example.bookstore.model.Role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleName roleName);
}
