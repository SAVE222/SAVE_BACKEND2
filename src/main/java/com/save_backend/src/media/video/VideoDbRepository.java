package com.save_backend.src.media.video;

import com.save_backend.src.media.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoDbRepository extends JpaRepository<Video, Long> {
}
