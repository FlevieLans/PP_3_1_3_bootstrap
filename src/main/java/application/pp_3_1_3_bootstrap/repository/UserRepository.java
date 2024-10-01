package application.pp_3_1_3_bootstrap.repository;

import application.pp_3_1_3_bootstrap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

}
