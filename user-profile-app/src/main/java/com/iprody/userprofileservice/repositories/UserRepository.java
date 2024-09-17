package com.iprody.userprofileservice.repositories;

import com.iprody.userprofileservice.models.Role;
import com.iprody.userprofileservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByUserRole(Role userRole);
}
