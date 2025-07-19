package equinox.account.jpa;

import equinox.account.model.dto.UserShortDto;
import equinox.account.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    @Query("SELECT u FROM User u " +
            "JOIN FETCH u.accounts a " +
            "JOIN FETCH a.currency " +
            "WHERE u.login = :login")
    Optional<User> findByLoginWithAccounts(@Param("login") String login);

    @Query("SELECT u FROM User u WHERE u.login <> :login")
    List<User> findAllExcept(@Param("login") String login);
}
