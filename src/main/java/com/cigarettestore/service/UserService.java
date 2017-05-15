package com.cigarettestore.service;

import java.util.Set;

import com.cigarettestore.domain.UserShipping;
import com.cigarettestore.domain.User;
import com.cigarettestore.domain.UserBilling;
import com.cigarettestore.domain.UserPayment;
import com.cigarettestore.domain.security.PasswordResetToken;
import com.cigarettestore.domain.security.UserRole;

public interface UserService {
	PasswordResetToken getPasswordResetToken(final String token);
	
	void createPasswordResetTokenForUser(final User user, final String token);
	
	User findByUsername(String username);
	
	User findByEmail (String email);
	
	User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	User save(User user);

	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);

	void setUserDefaultPayment(Long defaultPaymentId, User user);
	
	void updateUserShipping(UserShipping userShipping, User user);

	User findById(Long id);
	
}
