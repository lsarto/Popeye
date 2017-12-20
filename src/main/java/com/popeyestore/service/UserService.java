package com.popeyestore.service;

import java.util.Set;

import com.popeyestore.domain.UserShipping;
import com.popeyestore.domain.User;
import com.popeyestore.domain.UserBilling;
import com.popeyestore.domain.UserPayment;
import com.popeyestore.domain.security.PasswordResetToken;
import com.popeyestore.domain.security.UserRole;

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
