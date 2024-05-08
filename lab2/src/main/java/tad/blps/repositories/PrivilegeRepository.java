/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.repositories;

import org.springframework.stereotype.Repository;
import tad.blps.entity.Privilege;

/**
 *
 * @author Never
 */
@Repository
public interface PrivilegeRepository {

    public Privilege findByName(String name);

    public void save(Privilege privilege);
    
}
