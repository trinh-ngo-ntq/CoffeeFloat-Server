package com.login.repository;

import com.login.model.UserBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserBean, String> {

    UserBean findByEmail(String email);

    UserBean findByUserId(String userId);

}

