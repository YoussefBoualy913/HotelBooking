package repository;
import java.util.Optional;
import java.util.List;
import java.util.UUID;
import model.User;
public interface UserRepository {

    void save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findAll();
}