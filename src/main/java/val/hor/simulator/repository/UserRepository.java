package val.hor.simulator.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import val.hor.simulator.entity.Role;
import val.hor.simulator.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email ")
    User getUserByEmail (@Param("email") String email);
    Long countById(Integer id);
    @Query("SELECT u FROM User u WHERE " +
            "CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) LIKE %?1%")
    Page<User> findAll(String keyword, Pageable pageable);
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    public User findByVerificationCode(String code);
    @Modifying
    @Query("UPDATE User u SET u.enabled = true, u.verificationCode = null WHERE u.id = ?1")
    public void enable(Integer id);
    public List<User> findByRoles(Role role);

    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    void updateEnableStatus(Integer id, boolean enabled);




}
