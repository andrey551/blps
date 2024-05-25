package tad.blps.services;

import bitronix.tm.BitronixTransactionManager;
import java.io.IOException;
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
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class FileService {
//    We Can use transaction through @Transactional or 
//    BitronixTransactionManamger, both of them are working fine
    @Autowired
    private BitronixTransactionManager userTransaction;
    
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final FileRepository fileRepository;

    private final int STORAGE_LIMIT = 100000;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    
    @Transactional
    public File getFileById(Long id) {
        return fileRepository.getById(id);
    }
    
    @Transactional
    public List<File> getFilesByUserId(Long userId) {
        Optional<List<File>> fileList = 
                fileRepository.findByUserIdOrderBySizeAsc(userId);
        return fileList.orElse(null);
    }
    
    @Transactional
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
    
    @Transactional
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
                        FileHandler fileHandler = null;
                try {
                    fileHandler = new FileHandler("src/main/resources/status.log");
                } catch (IOException ex) {
                    Logger.getLogger(
                            FileService
                                    .class
                                    .getName()).
                            log(
                                    Level.SEVERE, 
                                    null, 
                                    ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(FileService
                            .class
                            .getName())
                            .log(
                                    Level.SEVERE, 
                                    null, 
                                    ex);
                }
                
                logger.addHandler(fileHandler);
                logger.log(Level.INFO, 
                        "User with Id: {0} has use maximum amount of storage", 
                        userId);
            }
        }

        fileToUpload.setIdstr(
                generateIdString(file.getFilename())
        );
        fileToUpload.setDownload_url(
                generateDownloadUrl(file.getFilename())
        );
        fileToUpload.setUserId(userId);
        
        try {
            userTransaction.begin();
            
            fileRepository.save(fileToUpload);
            
            userTransaction.commit();
        } catch( Exception e) {
            try {
                userTransaction.rollback();
            } catch (IllegalStateException ex) {
                Logger.getLogger(FileService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(FileService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (javax.transaction.SystemException ex) {
                Logger.getLogger(FileService.class.getName()).log(Level.SEVERE, null, ex);
            }
          
        }
        
    }
    
    public void updateFile(File file) {
        assert file.getId() != null;
        Long fileId = file.getId();
        Optional<File> existingFile = fileRepository.findById(fileId);

        if(existingFile.isPresent()) {
            try {
                userTransaction.begin();
                
                File fileToUpdate = existingFile.get();

                fileToUpdate.setFile_password(file.getFile_password());
                fileToUpdate.setDownload_url(file.getDownload_url());
                
                userTransaction.commit();
            } catch ( Exception e) {
                try {
                    userTransaction.rollback();
                } catch (IllegalStateException ex) {
                    Logger.getLogger(FileService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(FileService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (javax.transaction.SystemException ex) {
                    Logger.getLogger(FileService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
     }
    
    @Transactional
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
    
    @Transactional
    public boolean checkBelongToUser(String idStr, Long userId) {
        return Objects.equals(fileRepository
                .findFileByIdstr(idStr)
                .get()
                .getUserId(), userId);
    }
    
    @Transactional
    public void incDownloadCount(Long id) {
        File file = getFileById(id);
        if(file != null ) {
            file.setDownload_cnt(file.getDownload_cnt() + 1);
            updateFile(file);
        }
    }
    
//    For schedule task
    @Transactional 
    public Long DelBySizemax() {
        File fileToDel = fileRepository
                .findAll(Sort.by(Sort.Direction.DESC, "size"))
                .get(0);
        fileRepository.deleteById(fileToDel.getId());
        return fileToDel.getId();
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
    
    public boolean isExecutable(FileDTO file) {
        return file.getFilename().endsWith(".exe");
    }


}
