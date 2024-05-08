/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Never
 */
@Entity 
@Setter
@Getter
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    
    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;
    
    public Privilege(String name) {
        this.name = name;
    }
}
