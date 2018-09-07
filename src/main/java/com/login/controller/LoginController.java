package com.login.controller;

import java.util.Random;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.login.autologin.Autologin;
import com.login.config.InstagramConfig;
import com.login.config.LineConfig;
import com.login.model.RequestLogin;
import com.login.model.ResponseEntityBase;
import com.login.model.UserBean;
import com.login.repository.UserRepository;
import com.login.social.providers.FacebookProvider;
import com.login.social.providers.GoogleProvider;
import com.login.social.providers.InstagramProvider;
import com.login.social.providers.LineProvider;
import com.login.social.providers.PhoneProvider;
import com.login.social.providers.TwitterProvider;
import com.login.social.providers.YahooJapanProvider;

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

    @RequestMapping(value = {"/", "/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String showRegistration(UserBean userBean) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(HttpServletResponse httpServletResponse, Model model,
            @Valid UserBean userBean, BindingResult bindingResult) {
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
                UserBean user = phoneProvider.populateUserDetailsFromFAK(phoneNumber,
                        requestLogin.getToken());
                if (user == null) {
                    responseEntityBase =
                            new ResponseEntityBase<>(HttpStatus.BAD_REQUEST.value(), "", null);
                } else {
                    responseEntityBase = new ResponseEntityBase<>(HttpStatus.OK.value(), "", user);
                }
                break;
            default:
                responseEntityBase =
                        new ResponseEntityBase<>(HttpStatus.BAD_REQUEST.value(), "", null);
                break;
        }
        return new ResponseEntity<>(responseEntityBase, HttpStatus.OK);
    }

    @GetMapping("/1807/api/listFriend")
    @ResponseBody
    public ResponseEntity<?> getListFriend(HttpServletRequest request,
            HttpServletResponse response) {
        final String memberRequest = (String) request.getSession().getAttribute("member");
        System.out.println("token" + memberRequest);
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        ObjectNode result = mapper.createObjectNode();
        result.put("status", 200);
        result.put("message", "OK let's go");
        result.put("current_timestamp", System.currentTimeMillis());

        // Dummy
        final int dummyFriend = 10;
        final Random random = new Random();
        final String dummyAvatar = "https://platform-lookaside.fbsbx.com/platform/coverpic/?asid=2116953185291266&ext=1537008148&hash=AeRWNC1q42nRcCqx";
        for (int i = 0; i < dummyFriend; i++) {
            Member member = new Member();
            member.setAvatarUrl(dummyAvatar);
            member.setCode(random.nextInt(100000));
            member.setCountry("vn");
            member.setGender(random.nextInt(3));
            member.setMail("this.is.a.fake@email.com");
            member.setName("asfasdfasdf");
            member.setTel("+849999999");
            member.setVip(random.nextBoolean());
            array.addPOJO(member);
        }
        result.set("data", array);
        return ResponseEntity.ok(result);
    }


    private final static class Member {
        private int code;
        private String name;
        private String country;
        private int gender;
        private String mail;
        private String tel;
        private String avatarUrl;
        private boolean isVip;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public boolean isVip() {
            return isVip;
        }

        public void setVip(boolean isVip) {
            this.isVip = isVip;
        }

    }

}
