package val.hor.simulator.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import val.hor.simulator.entity.Teacher;
import val.hor.simulator.entity.User;
import val.hor.simulator.repository.TeacherRepository;

import java.util.List;

@Service
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher save(Teacher teacher){
        return teacherRepository.save(teacher);
    }

    public Teacher findUserByTeacherAndStudent(User user){
        return teacherRepository.findUserByTeacherAndStudent(user);
    }

    public List<Teacher> findUserByTeacher(User user){
        return teacherRepository.findUserByTeacher(user);
    }

    public List<Teacher> findAllByTeacherIdEqualsStudentId(){
        return teacherRepository.findAllByTeacherIdEqualsStudentId();
    }

    public void deleteByTeacherAndStudent(User user){
        teacherRepository.deleteByTeacherAndStudent(user);
    }

    public void deleteByTeacherOrStudent(User user){
        teacherRepository.deleteByTeacherOrStudent(user);
    }

    public void deleteByTeacherAndStudent(User teacher, User student){
        teacherRepository.deleteByTeacherAndStudent(teacher, student);
    }








}
