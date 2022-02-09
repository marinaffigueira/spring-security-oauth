package com.spiet.oauth.controller;

import com.spiet.oauth.constantes.RolesConstants;
import com.spiet.oauth.model.User;
import com.spiet.oauth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    public static final Logger log = LoggerFactory.getLogger(String.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping(value = "/join")
    public String joinUser(@RequestBody User user) {
        user.setRoles(RolesConstants.DEFAULT_ROLE);
        String encryptedPwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPwd);
        userRepository.save(user);

        return "HI " + user.getUsername() + " Welcome to group!";

    }

    @GetMapping(value = "/free")
    public String usersCanRead() {
        return "Hi, Users can read it!";
    }

    @GetMapping(value = "/restricted")
    public String onlyAdminsAndModsCanRead(Principal principal) {
        log.info("Cheguei até aqui");
        return "Hi, Only Administrator and moderator can read it! your name is: " + principal.getName();
    }

    @GetMapping(value = "/restricted/admin")
    @Secured("ROLE_ADMIN")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String onlyAdminCanRead(Principal principal) {
        return "Hi, Only Administrator and moderator can read it! your name is: " + principal.getName();
    }
}
