package com.example.ProductCategory.Service;

import com.example.ProductCategory.Dao.UserDao;
import com.example.ProductCategory.DTO.UserDto;
import com.example.ProductCategory.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    private static final PasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    // Save User
    public void saveUser(User u) {

        u.setPassword(passwordEncoder.encode(u.getPassword()));
        userDao.save(u);
    }

    // Get All Users DTO
    public List<UserDto> getAll() {

        List<User> users = userDao.findAll();
        List<UserDto> dtoList = new ArrayList<>();

        for (User u : users) {
            UserDto dto = new UserDto();

            dto.setUid(u.getUid());
            dto.setUsername(u.getUsername());
            dto.setRole(u.getRole());

            dtoList.add(dto);
        }

        return dtoList;
    }

    // Find By Username DTO
    public UserDto findByUserName(String username) {

        User u = userDao.findByUsername(username);

        if (u == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setUid(u.getUid());
        dto.setUsername(u.getUsername());
        dto.setRole(u.getRole());

        return dto;
    }

    //get total count of user
    public long getCount() {
        return userDao.count();
    }

    // Get User By Id DTO
    public UserDto getUserById(Integer id) {

        User u = userDao.findById(id).orElse(null);

        if (u == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setUid(u.getUid());
        dto.setUsername(u.getUsername());
        dto.setRole(u.getRole());

        return dto;
    }

    // Delete User
    public void deleteUser(Integer uid) {

        userDao.deleteById(uid);
    }

    public User getUserEntityByUsername(String username)
    {
        return userDao.findByUsername(username);
    }

    public User getUserEntityById(Integer id)
    {
        return userDao.findById(id).orElse(null);
    }
}