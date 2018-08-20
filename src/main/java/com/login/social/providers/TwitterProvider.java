package com.login.social.providers;

import com.login.model.UserBean;
import com.login.repository.UserRepository;
import com.login.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class TwitterProvider {
    private static final String TWITTER = "twitter";
    private static final String REDIRECT_LOGIN_TWITTER = "redirect:/login";

    @Autowired
    BaseProvider baseProvider ;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;

    public String getTwitterUserData(Model model, UserBean userForm) {

        ConnectionRepository connectionRepository = baseProvider.getConnectionRepository();
        if (connectionRepository.findPrimaryConnection(TwitterProvider.class) == null) {
            return REDIRECT_LOGIN_TWITTER;
        }
        //Populate the Bean
        populateUserDetailsFromTwitter(userForm);
        //Save the details in DB
        baseProvider.saveUserDetails(userForm);
        //Login the User
        baseProvider.autoLoginUser(userForm);
        model.addAttribute("loggedInUser",userForm);
        return "secure/user";
    }

    protected void populateUserDetailsFromTwitter(UserBean userform) {
        org.springframework.social.twitter.api.Twitter twitter = baseProvider.getTwitter();
        TwitterProfile twitterProfile = twitter.userOperations().getUserProfile();
        userform.setUserId(Long.toString(twitterProfile.getId()));
        userform.setFullName(twitterProfile.getName());
        userform.setAvatar(twitterProfile.getProfileImageUrl());
        userform.setProvider(TWITTER);
    }

    public UserBean populateUserDetailsFromTwitter(String token) throws Exception {
        org.springframework.social.twitter.api.Twitter twitter = new TwitterTemplate(token);
        TwitterProfile twitterProfile = twitter.userOperations().getUserProfile();
        if(twitterProfile == null || Long.toString(twitterProfile.getId()) == null) {
            throw new Exception("Token is invalid");
        }
        UserBean userBean = userRepository.findByUserId(Long.toString(twitterProfile.getId()));
        if (userBean == null) {
            userBean = new UserBean();
        }
        userBean.setUserId(Long.toString(twitterProfile.getId()));
        userBean.setFullName(twitterProfile.getName());
        userBean.setAvatar(twitterProfile.getProfileImageUrl());
        userBean.setProvider(TWITTER);
        userBean.setPassword(TWITTER);
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
        return userBean;
    }
}
