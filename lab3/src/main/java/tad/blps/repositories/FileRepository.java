package tad.blps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tad.blps.entity.File;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository
        extends JpaRepository<File, Long> {

    Optional<List<File>> findByUserId(Long userId);
    Optional<List<File>> findByUserIdOrderBySizeAsc(Long userId);
    Optional<File> findFileByIdstr(String idStr);

    void deleteByIdstr(String idstr);
    Optional<File> getFileByIdstr(String idstr);

}
