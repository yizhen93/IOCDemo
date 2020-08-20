package lucien.demo.service.impl;

import lucien.demo.dao.UserDao;
import lucien.demo.model.UserModel;
import lucien.demo.service.UserService;

public class UserServiceImpl implements UserService {
    private UserDao userDao;
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserModel getUserById(Long id) {
        return userDao.getUserById(id);
    }
}
