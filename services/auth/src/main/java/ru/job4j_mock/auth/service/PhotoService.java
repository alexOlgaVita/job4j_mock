package ru.job4j_mock.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j_mock.auth.domain.Photo;
import ru.job4j_mock.auth.repository.PhotoRepository;

/**
 * @author Hincu Andrei (andreih1981@gmail.com)on 28.05.2018.
 * @version $Id$.
 * @since 0.1.
 */
@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Photo save(Photo photo) {
        return photoRepository.save(photo);
    }

    public Photo findImage(Integer id) {
        return photoRepository.findById(id).get();
    }
}
