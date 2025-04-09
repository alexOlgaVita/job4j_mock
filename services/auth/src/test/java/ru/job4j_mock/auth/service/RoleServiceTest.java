package ru.job4j_mock.auth.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j_mock.auth.domain.Profile;
import ru.job4j_mock.auth.domain.Role;
import ru.job4j_mock.auth.repository.PersonRepository;

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author parsentev
 * @since 21.09.2016
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleServiceTest {
    @Autowired
    private RoleService service;

    @Autowired
    private PersonRepository persons;

    @Test
    public void whenAddRolesThenPersonHasRoles() {
        Role role = this.service.save(new Role("ROLE_ADMIN"));
        Profile profile = new Profile("Петр Арсентьев", String.format("%s@yandex.ru", System.currentTimeMillis()), "password");
        profile.setRoles(Collections.singletonList(role));
        this.persons.save(profile);
        Profile result = this.persons.findByEmail(profile.getEmail());
        assertThat(result.getRoles().isEmpty(), is(false));
    }
}