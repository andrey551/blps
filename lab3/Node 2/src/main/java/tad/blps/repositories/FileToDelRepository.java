/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tad.blps.entity.FileToDel;
import tad.blps.entity.Payment;
import tad.blps.entity.Role;

/**
 *
 * @author Never
 */

@Repository
public interface FileToDelRepository extends JpaRepository<FileToDel, Long>{
    public FileToDel findByFileid(Long fileid);
}
