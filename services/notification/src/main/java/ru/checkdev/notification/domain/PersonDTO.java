package ru.checkdev.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.List;

/**
 * DTO модель класса Person сервиса Auth.
 *
 * @author parsentev
 * @since 25.09.2016
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private String email;
    private String password;
    private boolean privacy;
    private List<RoleDTO> roles;
    private Calendar created;
    private String username;

    public PersonDTO(String email, String password, boolean privacy, List<RoleDTO> roles, Calendar created) {
        this.email = email;
        this.password = password;
        this.privacy = privacy;
        this.roles = roles;
        this.created = created;
    }
}
