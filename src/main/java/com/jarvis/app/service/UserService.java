package com.jarvis.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jarvis.app.dao.UserDAO;
import com.jarvis.app.to.UserTO;
import com.jarvis.cache.annotation.CacheDeleteTransactional;

@Service
public class UserService {

    private static final Logger logger=LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

    public UserTO getUserById2(Integer id) throws Exception {
        return userDAO.getUserById2(id);
    }

    @CacheDeleteTransactional
    public void deleteUser() {
        userDAO.clearUserById2Cache(10);
        userDAO.clearUserById2Cache(20);
        userDAO.clearUserById2Cache(30);
        logger.debug("deleteUser------");

    }
}
