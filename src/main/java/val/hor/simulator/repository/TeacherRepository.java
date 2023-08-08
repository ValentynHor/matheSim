package val.hor.simulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import val.hor.simulator.entity.Teacher;
import val.hor.simulator.entity.User;

import java.util.List;


public interface TeacherRepository extends JpaRepository<Teacher,Integer> {

    @Query("SELECT t FROM Teacher t WHERE t.teacher = :user AND t.student = :user")
    Teacher findUserByTeacherAndStudent(User user);

    @Query("SELECT t FROM Teacher t WHERE t.teacher.id = t.student.id")
    List<Teacher> findAllByTeacherIdEqualsStudentId();

    @Query("SELECT t FROM Teacher t WHERE t.teacher = :user")
    List<Teacher> findUserByTeacher(User user);


    @Modifying
    @Query("DELETE FROM Teacher t WHERE t.teacher = ?1 AND t.student = ?1")
    void deleteByTeacherAndStudent(User user);

    @Modifying
    @Query("DELETE FROM Teacher t WHERE t.teacher = ?1 OR t.student = ?1")
    void deleteByTeacherOrStudent(User user);

    @Modifying
    @Query("DELETE FROM Teacher t WHERE t.teacher = ?1 AND t.student = ?2")
    void deleteByTeacherAndStudent(User teacher, User student);


}
