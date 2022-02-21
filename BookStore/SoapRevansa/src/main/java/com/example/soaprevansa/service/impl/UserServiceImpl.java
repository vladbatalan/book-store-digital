package com.example.soaprevansa.service.impl;


import com.example.soaprevansa.model.dao.UserRepository;
import com.example.soaprevansa.model.dto.UserUpdate;
import com.example.soaprevansa.model.erd.User;
import com.example.soaprevansa.model.exception.HttpResponseException;
import com.example.soaprevansa.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {

        // Verifica ca username ul sa fie unic
        User existing = userRepository.findUserByUsername(user.getUsername());

        // Exista deja numele utilizatorului
        if (existing != null)
            throw new HttpResponseException("A user with this username already exists!", HttpStatus.NOT_ACCEPTABLE);

        // Validare rol
        List<String> roles = List.of("administrator", "client", "manager");

        if (!roles.contains(user.getRol()))
            throw new HttpResponseException("Role " + user.getRol() +
                    " does not exist", HttpStatus.NOT_ACCEPTABLE);

        // Validate password
        // Encode password
        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User getUserById(String userId) {

        Optional<User> user =  userRepository.findById(userId);
        if(user.isEmpty())
            throw new HttpResponseException("User does not exist.", HttpStatus.NOT_FOUND);
        return user.get();

    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }


//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        if ("javainuse".equals(username)) {
//            return new org.springframework.security.core.userdetails.User("javainuse",
//                    "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
//                    new ArrayList<>());
//        } else {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//    }

    @Override
    public User updateUser(UserUpdate userUpdate) {
        return null;
    }
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User created = userRepository.findUserByUsername(username);
//
//        if (created != null) {
//
//            List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority(created.getRol()));
//
//            return new org.springframework.security.core.userdetails.User(created.getUsername(), created.getPassword(),
//                    roles);
//        } else {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//    }
//
//    @Override
//    public User updateUser(UserUpdate userUpdate) {
//        Optional<User> exists = userRepository.findById(userUpdate.getUserId());
//
//        if (exists.isEmpty())
//            throw new HttpResponseException("User does not exist", HttpStatus.NOT_FOUND);
//
//        User toBeChanged = exists.get();
//
//        if (!userUpdate.getEmail().equals(toBeChanged.getEmail())) {
//
//            // Validare regex email
//            String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
//                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
//            boolean regexMatch = Pattern.compile(regexPattern)
//                    .matcher(userUpdate.getEmail())
//                    .matches();
//
//            if (!regexMatch)
//                throw new HttpResponseException("Email format is not respected!", HttpStatus.NOT_ACCEPTABLE);
//            User existing = userRepository.findUserByEmail(userUpdate.getEmail());
//
//            if (existing != null)
//                throw new HttpResponseException("A user with this email already exists!", HttpStatus.NOT_ACCEPTABLE);
//
//            toBeChanged.setEmail(userUpdate.getEmail());
//        }
//
//        if (userUpdate.getRol() != null && !userUpdate.getRol().equals(toBeChanged.getRol())) {
//            List<String> roles = List.of("ADMIN", "CLIENT", "MANAGER", "ANGAJAT");
//
//            if (!roles.contains(userUpdate.getRol()))
//                throw new HttpResponseException("Role " + userUpdate.getRol() +
//                        " does not exist", HttpStatus.NOT_ACCEPTABLE);
//
//            toBeChanged.setRol(userUpdate.getRol());
//        }
//
//        if(userUpdate.getRestaurantId() == null || !userUpdate.getRestaurantId().equals(toBeChanged.getRestaurantId())){
//            toBeChanged.setRestaurantId(userUpdate.getRestaurantId());
//        }
//
//        // The password must be encoded with passwordEncoder
//        if(userUpdate.getNew_password() != null && userUpdate.getPassword() != null){
//            toBeChanged.setPassword(userUpdate.getNew_password());
//        }
//
//        return userRepository.save(toBeChanged);
//    }


}
