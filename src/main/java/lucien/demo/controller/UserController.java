package lucien.demo.controller;

import lucien.demo.model.UserModel;
import lucien.demo.service.UserService;

public class UserController {
    private UserService userService;
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserModel getUserById(Long id) {
        return userService.getUserById(id);
    }
}
