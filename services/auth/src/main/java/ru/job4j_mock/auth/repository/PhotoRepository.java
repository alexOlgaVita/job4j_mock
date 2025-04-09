package ru.job4j_mock.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j_mock.auth.domain.Photo;

/**
 * @author Hincu Andrei (andreih1981@gmail.com)on 28.05.2018.
 * @version $Id$.
 * @since 0.1.
 */
public interface PhotoRepository extends CrudRepository<Photo, Integer> {
}
