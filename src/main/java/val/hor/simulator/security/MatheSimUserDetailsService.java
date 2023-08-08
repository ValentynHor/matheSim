package val.hor.simulator.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import val.hor.simulator.entity.User;
import val.hor.simulator.repository.UserRepository;

public class MatheSimUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        User userByEmail = userRepository.getUserByEmail(email);
        if (userByEmail != null)
            return new MatheSimUserDetails(userByEmail);


        throw new UsernameNotFoundException("Could not find user with email " + email);
    }
}
