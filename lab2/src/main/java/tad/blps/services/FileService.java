package tad.blps.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tad.blps.DTO.FileDTO;
import tad.blps.entity.File;
import tad.blps.repositories.FileRepository;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class FileService {

    private FileRepository fileRepository;

    private final int STORAGE_LIMIT = 1000000000;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public File getFileById(Long id) {
        return fileRepository.getById(id);
    }

    public List<File> getFilesByUserId(Long userId) {
        Optional<List<File>> fileList = 
                fileRepository.findByUserIdOrderBySizeAsc(userId);
        return fileList.orElse(null);
    }

    public List<File> getFilesByUserIdAndSpecificSorting(
                                            Long userId, 
                                            String param, 
                                            String order) {
        List<File> fileList;
        if(order.equals("DESC")) {
            fileList = fileRepository
                    .findAll(Sort
                            .by(
                                    Sort.Direction.DESC, 
                                    param)
                    );
        } else {
            fileList = fileRepository
                    .findAll(Sort
                            .by(
                                    Sort.Direction.ASC, 
                                    param)
                    );
        }
        return fileList
                .stream()
                .filter(c -> c.getUserId()
                        == userId)
                .toList();

    }
    
    public List<File> getFilesByFilter(
                                        Long userId, 
                                        String param, 
                                        String order, 
                                        String filterBy, 
                                        String value) {
        List<File> res = getFilesByUserIdAndSpecificSorting(
                                                            userId, 
                                                            param, 
                                                            order);
        switch(filterBy) {
            case "download_url" -> {
                return res
                        .stream()
                        .filter(c->c.getDownload_url()
                                .equals(value))
                        .toList();
            }
            case "filename_source" -> {
                return res
                        .stream()
                        .filter(c->c.getFilename_source()
                                .equals(value))
                        .toList();
            }
            case "idstr" -> {
                return res
                        .stream()
                        .filter(c->c.getIdstr()
                                .equals(value))
                        .toList();
            }
            case "None" -> {
                return res;
            }
            case "download_cnt" -> {
                return res
                        .stream()
                        .filter(c->c.getDownload_cnt()
                                == Integer.parseInt(value))
                        .toList();
            }
            case "size" -> {
                return res
                        .stream()
                        .filter(c -> c.getSize()
                                ==  Integer.parseInt(value))
                        .toList();
            }
            default -> {
                return null;
            }
        }
    }



//    return status of file
    public void uploadFile(FileDTO file, Long userId)
                        throws NoSuchAlgorithmException {
        File fileToUpload = new File(file);
        Optional<List<File>> fileList = fileRepository.findByUserId(userId);
        if(fileList.isPresent()) {
            Long totalSize = 
                    fileList
                            .get()
                            .stream()
                            .mapToLong(File::getSize)
                            .sum();
            if(totalSize > STORAGE_LIMIT) {
                throw new OutOfMemoryError();
            }
        }

        fileToUpload.setIdstr(
                generateIdString(file.getFilename())
        );
        fileToUpload.setDownload_url(
                generateDownloadUrl(file.getFilename())
        );
        fileToUpload.setUserId(userId);
        fileRepository.save(fileToUpload);
    }

    @Transactional
    public void updateFile(File file) {
        assert file.getId() != null;
        Long fileId = file.getId();
        Optional<File> existingFile = fileRepository.findById(fileId);

        if(existingFile.isPresent()) {
            File fileToUpdate = existingFile.get();

            fileToUpdate.setFile_password(file.getFile_password());
            fileToUpdate.setDownload_url(file.getDownload_url());
        }
     }

    public void deleteFile(String idStr, Long userId) {
        System.out.println(idStr + " " + userId);
        if(checkBelongToUser(idStr, userId)) {
            File toRemove = fileRepository.getFileByIdstr(idStr).orElse(null);
            fileRepository.deleteById(toRemove.getId());
        } else {
            throw new PermissionDeniedDataAccessException(
                    "File does not belong to current user",
                    new Exception()
            );
        }

    }

    public boolean checkBelongToUser(String idStr, Long userId) {
        return Objects.equals(fileRepository
                .findFileByIdstr(idStr)
                .get()
                .getUserId(), userId);
    }

    public void incDownloadCount(Long id) {
        File file = getFileById(id);
        if(file != null ) {
            file.setDownload_cnt(file.getDownload_cnt() + 1);
            updateFile(file);
        }
    }

    public String generateDownloadUrl(String fileName) {
        return "https:dfiles.eu/files/" + fileName;
    }
    public String generateIdString(String name) 
            throws NoSuchAlgorithmException {
        String ret = "";
        for( int i = 0; i < name.length(); ++i) {
            ret += ((int)name.charAt(i));
        }

        return ret;
    }


    public Optional<List<File>> getUserFiles(long id) {
        return fileRepository.findByUserId(id);
    }


}
