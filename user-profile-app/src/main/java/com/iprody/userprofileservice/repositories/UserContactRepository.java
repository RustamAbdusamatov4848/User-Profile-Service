package com.iprody.userprofileservice.repositories;

import com.iprody.userprofileservice.models.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserContactRepository extends JpaRepository<UserContact,Long> {
}
