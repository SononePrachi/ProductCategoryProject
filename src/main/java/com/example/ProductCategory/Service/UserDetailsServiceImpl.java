package com.example.ProductCategory.Service;

import com.example.ProductCategory.Dao.UserDao;
import com.example.ProductCategory.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userDao.findByUsername(username);
        if(user!=null)
        {
           UserDetails userDetails= org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole().toArray(new String[0]))
                    .build();
           return  userDetails;
        }
        throw new UsernameNotFoundException("User Not found with username:"+username);
    }
}
