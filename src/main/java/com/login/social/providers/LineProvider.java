package com.login.social.providers;

import com.login.config.LineConfig;
import com.login.exception.ResourceNotFoundException;
import com.login.model.LineEntityProfile;
import com.login.model.UserBean;
import com.login.repository.UserRepository;
import com.login.security.JwtService;
import com.login.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LineProvider {

    private static final String LINE = "line";
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LineConfig lineConfig;
    @Autowired
    BaseProvider baseProvider;
    @Autowired
    JwtService jwtService;

    public UserBean loginLineByToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.LineConst.authorization, Constants.LineConst.bearer + token);
        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(headers);
        LineEntityProfile lineEntityProfile = restTemplate.exchange(Constants.LineConst.urlProfile, HttpMethod.GET, httpEntity, LineEntityProfile.class).getBody();
        if (lineEntityProfile == null || lineEntityProfile.getUserId() == null) {
            throw new ResourceNotFoundException("Token is invalid");
        }
        UserBean userBean = userRepository.findByUserId(lineEntityProfile.getUserId());
        if (userBean == null) {
            userBean = new UserBean();
        }
        userBean.setFullName(lineEntityProfile.getDisplayName());
        userBean.setAvatar(lineEntityProfile.getPictureUrl());
        userBean.setUserId(lineEntityProfile.getUserId());
        userBean.setAccesstoken(token);
        userBean.setProvider(LINE);
        userBean.setPassword(LINE);
        if (baseProvider.checkLoginSocial(userBean)) {
            baseProvider.saveUserDetails(userBean);
            baseProvider.autoLoginUser(userBean);
            return userBean;
        }
        return null;
    }
}
