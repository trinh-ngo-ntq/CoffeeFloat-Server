package com.login.social.providers;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import com.login.exception.InvalidAccessToken;
import com.login.model.UserBean;
import com.login.repository.UserRepository;
import com.login.util.Constants;

@Service
public class PhoneProvider {
	private static final String PHONE = "phone";
	// TODO: Fixme
	private static final String DUMMY_EMAIL = "this.is.a.dummy.email.for.phone@dummy.com";

	@Autowired
	private UserRepository userRepos;

	@Autowired
	BaseProvider baseProvider;

	/**
	 * Get the user information from Facebook Account Kit based on
	 * {@code accessToken} <br />
	 * In case the user had not logged in yet, then register new {@code UserBean} If
	 * user had logged in, then update information
	 * 
	 * @param accessToken
	 * @return null if no information found with that token
	 */
	public UserBean populateUserDetailsFromFAK(String phoneNumber, String accessToken) throws InvalidAccessToken {
	    if(phoneNumber == null || accessToken == null) {
	        throw new InvalidAccessToken("Not accept null value");
	    }
		// Step 1 : Make an request to link
		// Step 2 : Parse the response
		String encodedURL = Constants.FacebookAccountKitConst.ACCOUNT_KIT_LINK + accessToken;
		ResponseErrorHandler responseErrorHandler = new DefaultResponseErrorHandler() {
			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				// Ignore throw exception
			}
		};
		// NOTE: Construct new RestTemplate, not invoke the bean RestTemplate here because we had update the setting
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(responseErrorHandler);
		String resultStr = restTemplate.getForObject(encodedURL, String.class);
		try {
			final JSONObject resultJson = new JSONObject(resultStr);
			if (resultJson.has("error")) {
			    // whatever error occurs -> Throw an invalid token
				throw new InvalidAccessToken("Invalid access token");
			}
			final String fakUserId = resultJson.getString("id");
			JSONObject phoneJSON = resultJson.getJSONObject("phone");
			final String nationalJSONKey = "national_number";
			final String numberJSONKey = "number";
			final String countryPrefixJSONKey = "country_prefix";
			
			final String nationalNumber = phoneJSON.getString(nationalJSONKey);
			final String number = phoneJSON.getString(numberJSONKey);
			final String countryPrefix = "+" + phoneJSON.getString(countryPrefixJSONKey); // Combine with plus sign
			
			// Follow E.164 format
			// If phone number start by + -> it should include country code
			// If phone number start by 0 -> it will use the national code
			boolean isEqualPhoneNumber = false;
			if(phoneNumber.startsWith(countryPrefix)) {
				// Should be equal
				isEqualPhoneNumber = phoneNumber.equals(number);
			} else if(phoneNumber.startsWith("0")) {
				isEqualPhoneNumber = phoneNumber.equals("0" + nationalNumber);
			}
			// Else isEqualPhoneNumber is false
			// Normalize the phone number (should include the + prefix)
			if (isEqualPhoneNumber) {
				UserBean userBean = userRepos.findByPhoneNumberAndProvider(number, PHONE);
				if (userBean == null) {
					userBean = new UserBean();
				}
				userBean.setUserId(fakUserId);
				userBean.setPhoneNumber(number);
				userBean.setProvider(PHONE);
				userBean.setPassword(PHONE);
				userBean.setEmail(DUMMY_EMAIL);
				
				baseProvider.checkLoginSocial(userBean); // Always true, update the access token
				baseProvider.saveUserDetails(userBean);
				baseProvider.autoLoginUser(userBean);
				return userBean;
			}
			// Else other case, fall down to null
			return null;
		} catch (JSONException e) {
		    throw new InvalidAccessToken(e);		    
		}
	}
}
