package ru.job4j_mock.auth.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j_mock.auth.domain.Role;
import ru.job4j_mock.auth.repository.RoleRepository;

import java.util.List;

/**
 * @author parsentev
 * @since 26.09.2016
 */
@Service
public class RoleService {
    private final RoleRepository roles;

    @Autowired
    public RoleService(RoleRepository roles) {
        this.roles = roles;
    }

    public Role save(Role role) {
        return this.roles.save(role);
    }

    public List<Role> findAll() {
        return Lists.newArrayList(this.roles.findAll());
    }
}
