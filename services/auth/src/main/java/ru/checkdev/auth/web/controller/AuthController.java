package ru.checkdev.auth.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.dto.PersonDTO;
import ru.checkdev.auth.dto.RoleDTO;
import ru.checkdev.auth.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

/**
 * @author parsentev
 * @since 26.09.2016
 */
@RestController
public class AuthController {
    private final PersonService persons;
    private final String ping = "{}";

    @Autowired
    public AuthController(final PersonService persons) {
        this.persons = persons;
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @GetMapping("/ping")
    public String ping() {
        return this.ping;
    }

    @GetMapping("/auth/activated/{key}")
    public Object activated(@PathVariable String key) {
        if (this.persons.activated(key)) {
            return new Object() {
                public boolean getSuccess() {
                    return true;
                }
            };
        } else {
            return new Object() {
                public String getError() {
                    return "Notify has already activated";
                }
            };
        }
    }

    @PostMapping("/registration")
    public Object registration(@RequestBody Profile profile) {
        Optional<Profile> result = this.persons.reg(profile);
        return result.<Object>map(prs -> new Object() {
            public Profile getPerson() {
                return prs;
            }
        }).orElseGet(() -> new Object() {
            public String getError() {
                return String.format("Пользователь с почтой %s уже существует.", profile.getEmail());
            }
        });
    }

    @PostMapping("/forgot")
    public Object forgot(@RequestBody Profile profile) {
        Optional<Profile> result = this.persons.forgot(profile);
        if (result.isPresent()) {
            return new Object() {
                public String getOk() {
                    return "ok";
                }
            };
        } else {
            return new Object() {
                public String getError() {
                    return "E-mail не найден.";
                }
            };
        }
    }

    @GetMapping("/revoke")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request) {

    }

    @GetMapping("/auth/userInfo/{email}")
    public ResponseEntity<PersonDTO> getProfile(@PathVariable String email) {
        Optional<Profile> result = this.persons.findByEmail(email);
        PersonDTO res = null;
        if (result.isPresent()) {
            var r = result.get();
            res = new PersonDTO(r.getEmail(), r.getPassword(), true,
                    r.getRoles().stream().map(e -> new RoleDTO(e.getId())).toList(),
                    r.getCreated(), r.getUsername());
            System.out.println("res = " + res);
        }

        return new ResponseEntity<>(
                res,
                result.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @DeleteMapping("/auth/deleteByLoginPassword/{login}/{password}")
    public ResponseEntity<Void> deleteById(@PathVariable String login, @PathVariable String password) {
        System.out.println("login = " + login);
        System.out.println("password = " + password);
        var user = this.persons.findByLoginAndPassword(login, password);
        if (user.isPresent()) {
            if (this.persons.deleteById(user.get().getId())) {
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
