package de.medical.app.repository;

import de.medical.app.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository <FileEntity, Long> {
}

