package val.hor.simulator.service;


import net.bytebuddy.utility.RandomString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import val.hor.simulator.entity.Role;
import val.hor.simulator.entity.RoleType;
import val.hor.simulator.entity.User;
import val.hor.simulator.repository.RoleRepository;
import val.hor.simulator.repository.UserRepository;
import val.hor.simulator.util.exception.UserNotFoundException;

import java.util.*;

@Service
@Transactional
public class UserService {

    public static final int USERS_PER_PAGE = 10;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword ){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum-1, USERS_PER_PAGE, sort);

        if(keyword != null){
            return userRepository.findAll(keyword,pageable);
        }
        return userRepository.findAll(pageable);
    }

    public List<User> listAll(){
        //return (List<User>) userRepository.findAll();
        return (List<User>) userRepository.findAll(Sort.by("id").ascending());
    }

    public List<Role> listRoles(){
        return (List<Role>) roleRepository.findAll();
    }


    public User save(User user) {
        boolean isUpdatingUser = ( user.getId() != null);
        if(isUpdatingUser){
            User existingUser = userRepository.findById(user.getId()).get();
            user.setEnabled(existingUser.isEnabled());
            user.setCreatedTime(existingUser.getCreatedTime());
            user.setRoles(existingUser.getRoles());
            if(user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            }else{
                encodePassword(user);
            }
        }else {
            encodePassword(user);
        }
        return userRepository.save(user);
    }

    public User registerUser(User user){
        encodePassword(user);
        user.addRole(roleRepository.findByName("Student"));
        user.setEnabled(true);
        user.setCreatedTime(new Date());
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        return userRepository.save(user);
    }

    private void encodePassword(User user){
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
    }

    public boolean isEmailUnique(Integer id,String email){
        User userByEmail = userRepository.getUserByEmail(email);
        if(userByEmail == null)
            return true;
        boolean isCreatingNew = (id == null);
        if(isCreatingNew){
            if(userByEmail != null) return  false;
        } else{
            if (userByEmail.getId() != id){
                return  false;
            }
        }
        return true;
    }

    public User get(Integer id) throws UserNotFoundException {
        try{
            return userRepository.findById(id).get();
        } catch (NoSuchElementException ex){
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
    }
    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);
        if (countById == null || countById == 0)
            throw new UserNotFoundException("Could not find any user with ID " + id);
        userRepository.deleteById(id);
    }
    public boolean verify(String verificationCode){
        User user = userRepository.findByVerificationCode(verificationCode);
        if(user == null || user.isEnabled()){
            return false;
        } else {
            userRepository.enable(user.getId());
            return true;
        }
    }
    public List<User> findByRoles(Role role){
        return userRepository.findByRoles(role);
    }

    public void changeRole(User user, RoleType roleType) {
        Role role;
        switch (roleType) {
            case Admin:
                role = roleRepository.findByName("Admin");
                break;
            case Teacher:
                role = roleRepository.findByName("Teacher");
                break;
            case Student:
                role = roleRepository.findByName("Student");
                break;
            default:
                return;
        }
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        user.updateRoles(roleSet);
    }

    public void updateEnableStatus(Integer id, boolean enabled) {
        userRepository.updateEnableStatus(id,enabled);
    }





}
