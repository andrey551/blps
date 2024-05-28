/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.services;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tad.blps.entity.FileToDel;
import tad.blps.repositories.FileToDelRepository;

/**
 *
 * @author Never
 */

@Service
public class FileToDelService {
    
    private FileToDelRepository repos;
    
    @Autowired
    public FileToDelService(FileToDelRepository repos) {
        this.repos = repos;
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
        repos.deleteById(todel.getId());
    }
}
