package com.project.back_end.repository;

import com.project.back_end.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * ADMIN REPOSITORY — Database operations for the admins table.
 * 
 * Simple repository — just find by username.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /*
     * findByUsername — Find admin by login username.
     * Spring generates: SELECT * FROM admin WHERE username = ?
     */
    Admin findByUsername(String username);
}
