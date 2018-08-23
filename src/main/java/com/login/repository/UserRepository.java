package com.login.repository;

import com.login.model.UserBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserBean, String> {

    UserBean findByEmail(String email);

    UserBean findByUserId(String userId);

    // TODO: Phone number is an ambiguous term (include region code?)
    /**
     * Expected one {@code UserBean} returned based on {@code phoneNumber} and {@code provider}
     * @param phoneNumber
     * @param provider
     * @return null if none match, exception thrown when more than 2 matched
     */
    UserBean findByPhoneNumberAndProvider(String phoneNumber, String provider);
    
}

