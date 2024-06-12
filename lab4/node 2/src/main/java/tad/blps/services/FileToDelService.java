/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.services;

import bitronix.tm.BitronixTransactionManager;
import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tad.blps.entity.FileToDel;
import tad.blps.repositories.FileToDelRepository;

/**
 *
 * @author Never
 */

@Service
@RequiredArgsConstructor
public class FileToDelService {
    @Autowired
    private BitronixTransactionManager userTransaction;
    private FileToDelRepository repos;
    
    private final FileService fileService;
    
    @Autowired
    public FileToDelService(FileToDelRepository repos, FileService fileService) {
        this.repos = repos;
        this.fileService = fileService;
    }
    public List<Long> getListNeedDel() {
        Date now = new Date(new java.util.Date().getTime()); 
        Date yesterday = new Date(now.getTime() - 24*60*60*1000);
        return repos
                .findAll()
                .stream()
                .filter(file -> file.getTimeCreated().before(yesterday))
                .map(FileToDel::getFileid)
                .collect(Collectors.toList());
    }
    
    public void addFile(Long fileId) {
        Date now = new Date(new java.util.Date().getTime()); 
        Date yesterday = new Date(now.getTime() - 2*24*60*60*1000);
        FileToDel toDel = new FileToDel();
        toDel.setFileid(fileId);
        toDel.setTimeCreated(yesterday);
        repos.save(toDel);
    }
    
    public void deleteFile(Long fileId) {
        FileToDel todel = repos.findByFileid(fileId);
        try {
            userTransaction.begin();
            repos.deleteById(todel.getId());
            fileService.deleteById(fileId);
            userTransaction.commit();
        } catch (IllegalStateException | SecurityException | HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException ex) {
            System.err.println("Delete file not successfully!");
            try {
                userTransaction.rollback();
            } catch (IllegalStateException | SecurityException | SystemException ex1) {
                Logger.getLogger(FileToDelService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }
    
    
}
