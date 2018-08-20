package com.login.social.providers;

import com.login.repository.UserRepository;
import com.login.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.plus.Person;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.login.model.UserBean;

@Service
public class GoogleProvider   {

	private static final String GOOGLE = "google";

	@Autowired
	JwtService jwtService;

	@Autowired
	UserRepository userRepository;

	@Autowired
    BaseProvider baseProvider ;

	public UserBean getGoogleUserData(UserBean userForm) {
		ConnectionRepository connectionRepository = baseProvider.getConnectionRepository();
		if (connectionRepository.findPrimaryConnection(Google.class) == null) {
			return null;
		}
		String accessToken = connectionRepository.getPrimaryConnection(Google.class).createData().getAccessToken();
		userForm.setAccesstoken(accessToken);
		populateUserDetailsFromGoogle(userForm);
		//Save the details in DB
		baseProvider.saveUserDetails(userForm);
		baseProvider.autoLoginUser(userForm);
		return userForm;
	}


	protected void populateUserDetailsFromGoogle(UserBean userform) {
		Google google = baseProvider.getGoogle();
		Person googleUser = google.plusOperations().getGoogleProfile();
		userform.setEmail(googleUser.getAccountEmail());
		userform.setUserId(googleUser.getId());
		userform.setFullName(googleUser.getGivenName()+" "+googleUser.getFamilyName());
		userform.setAvatar(googleUser.getImageUrl());
		userform.setGender(googleUser.getGender());
		userform.setProvider(GOOGLE);
	}

	//Login google by token
	public UserBean populateUserDetailsFromGoogle(String token) throws Exception{
		Google google = new GoogleTemplate(token);
		Person googleUser = google.plusOperations().getGoogleProfile();
		if(googleUser == null || googleUser.getId()==null) {
			throw new Exception("Token is invalid");
		}
		UserBean userBean = userRepository.findByUserId(googleUser.getId());
		if (userBean == null) {
			userBean = new UserBean();
			userBean.setEmail(googleUser.getAccountEmail());
			userBean.setUserId(googleUser.getId());
			userBean.setFullName(googleUser.getGivenName()+" "+googleUser.getFamilyName());
			userBean.setAvatar(googleUser.getImageUrl());
			userBean.setGender(googleUser.getGender());
			userBean.setProvider(GOOGLE);
			userBean.setPassword(GOOGLE);
			baseProvider.saveUserDetails(userBean);

		}
		try {
			if (baseProvider.checkLoginSocial(userBean)) {
				String result = jwtService.generateTokenLogin(userBean.getEmail());
				userBean.setAccesstoken(result);
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		baseProvider.autoLoginUser(userBean);
		return userBean;
	}

}
