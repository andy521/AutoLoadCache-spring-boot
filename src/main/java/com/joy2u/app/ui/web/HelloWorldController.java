package com.joy2u.app.ui.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joy2u.app.config.ApplicationConfig;
import com.joy2u.app.config.DynamicConfig;
import com.joy2u.app.dao.UserDAO;
import com.joy2u.app.to.UserTO;

@RestController
@RequestMapping("/test")
public class HelloWorldController {

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private DynamicConfig dynamicConfig;

    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value="/{name}", method=RequestMethod.GET)
    @ResponseBody
    public String sayWorld(@PathVariable("name") String name) {
        System.out.println("is dev=" + applicationConfig.isDev());
        System.out.println("dynamicConfig=" + dynamicConfig.getProValueFromEnviroment("dynamic-info"));
        UserTO user;
        try {
            user=userDAO.getUserById2(100);
            System.out.println(user);
        } catch(Exception e) {
            e.printStackTrace();
        }

        userDAO.clearUserById2Cache(100);
        return "Hello " + name;
    }
}
