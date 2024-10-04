package application.pp_3_1_3_bootstrap.service;

import application.pp_3_1_3_bootstrap.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    boolean saveUser(User user);

    boolean updateUser(User user);

    User getUser(int id);

    boolean deleteUser(int id);

}