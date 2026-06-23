package user.service.User.Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import user.service.User.Service.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
