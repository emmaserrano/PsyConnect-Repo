package ni.edu.uam.psyconnect_backend.repository;

import ni.edu.uam.psyconnect_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}