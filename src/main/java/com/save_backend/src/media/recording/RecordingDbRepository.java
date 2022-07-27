package com.save_backend.src.media.recording;

import com.save_backend.src.media.recording.entity.Recording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordingDbRepository extends JpaRepository<Recording, Long> {
}
