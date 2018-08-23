package com.login.controller;

import com.login.autologin.Autologin;
import com.login.config.InstagramConfig;
import com.login.config.LineConfig;
import com.login.model.RequestLogin;
import com.login.model.ResponseEntityBase;
import com.login.model.UserBean;
import com.login.repository.UserRepository;
import com.login.social.providers.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class LoginController {

	@Autowired
	LineProvider lineProvider;
	@Autowired
	FacebookProvider facebookProvider;
	@Autowired
	GoogleProvider googleProvider;
	@Autowired
	TwitterProvider twitterProvider;
	@Autowired
	InstagramProvider instagramProvider;
	@Autowired
	LineConfig lineConfig;
	@Autowired
	InstagramConfig instagramConfig;
	@Autowired
	YahooJapanProvider yahooJapanProvider;

	@Autowired
	private PhoneProvider phoneProvider;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private Autologin autologin;

	@RequestMapping(value = { "/", "/login" })
	public String login() {
		return "login";
	}

	@GetMapping("/registration")
	public String showRegistration(UserBean userBean) {
		return "registration";
	}

	@PostMapping("/registration")
	public String registerUser(HttpServletResponse httpServletResponse, Model model, @Valid UserBean userBean,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "registration";
		}
		userBean.setProvider("REGISTRATION");
		if (StringUtils.isNotEmpty(userBean.getPassword())) {
			userBean.setPassword(bCryptPasswordEncoder.encode(userBean.getPassword()));
		}
		userRepository.save(userBean);
		autologin.setSecuritySocialContext(userBean);
		model.addAttribute("loggedInUser", userBean);
		return "secure/user";
	}

	@ResponseBody
	@RequestMapping(value = "/api/loginSocialByToken", method = RequestMethod.POST)
	public ResponseEntity<?> loginSocialByToken(@RequestBody RequestLogin requestLogin)
			throws Exception {
		ResponseEntityBase<UserBean> responseEntityBase;
		switch (requestLogin.getType()) {
		case "FACEBOOK":
			responseEntityBase = new ResponseEntityBase<>(HttpStatus.OK.value(), "",
					facebookProvider.populateUserDetailsFromFacebook(requestLogin.getToken()));
			break;
		case "GOOGLE":
			responseEntityBase = new ResponseEntityBase<>(HttpStatus.OK.value(), "",
					googleProvider.populateUserDetailsFromGoogle(requestLogin.getToken()));
			break;
		case "TWITTER":
			responseEntityBase = new ResponseEntityBase<>(HttpStatus.OK.value(), "",
					twitterProvider.populateUserDetailsFromTwitter(requestLogin.getToken()));
			break;
		case "INSTAGRAM":
			responseEntityBase = new ResponseEntityBase<>(HttpStatus.OK.value(), "",
					instagramProvider.getInstagramUserData(requestLogin.getToken()));
			break;
		case "LINE":
			responseEntityBase = new ResponseEntityBase<>(HttpStatus.OK.value(), "",
					lineProvider.loginLineByToken(requestLogin.getToken()));
			break;
		case "YAHOOJP":
			responseEntityBase = new ResponseEntityBase<>(HttpStatus.OK.value(), "",
					yahooJapanProvider.getyObject(requestLogin.getToken()));
			break;

		// TrinhNX : 08/2018 - Add phone login, required phoneNumber
		case "PHONE":
			final String phoneNumber = requestLogin.getPhoneNumber();
			UserBean user = phoneProvider.populateUserDetailsFromFAK(phoneNumber, requestLogin.getToken());
			if(user == null) {
				responseEntityBase = new ResponseEntityBase<>(HttpStatus.BAD_REQUEST.value(), "", null);
			} else {
				responseEntityBase = new ResponseEntityBase<>(HttpStatus.OK.value(), "", user);
			}
			break;
		default:
			responseEntityBase = new ResponseEntityBase<>(HttpStatus.BAD_REQUEST.value(), "", null);
			break;
		}
		return new ResponseEntity<>(responseEntityBase, HttpStatus.OK);
	}
}
