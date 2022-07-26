package com.save_backend.src.media.picture;

import com.save_backend.src.media.picture.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureDbRepository extends JpaRepository<Picture, Long> {
}
