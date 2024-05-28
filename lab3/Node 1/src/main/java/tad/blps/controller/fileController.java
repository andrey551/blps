package tad.blps.controller;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tad.blps.DTO.FileDTO;
import tad.blps.DTO.TokenDTO;
import tad.blps.entity.File;
import tad.blps.services.FileService;
import tad.blps.services.UserService;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import tad.blps.entity.Role;
import tad.blps.entity.User;
import tad.blps.services.FileToDelService;

@RestController
@RequestMapping("/file")
public class fileController {
    private final FileService fileService;
    private final UserService userService;
    private final FileToDelService fileToDelService;

    @Autowired
    public fileController(FileService fileService,
                          UserService userService,
                          FileToDelService fileToDelService) {
        this.fileService = fileService;
        this.userService = userService;
        this.fileToDelService = fileToDelService;
    }
    
    

    TokenDTO getToken(String BasicToken) {
        String token = BasicToken.substring(6);
        return new TokenDTO(token);
    }

    @GetMapping(path = "/list")
    @RolesAllowed({ "USER", "ADMIN"})
    @ResponseBody
    public List<File> getFiles(@RequestHeader("Authorization") String token,
                                @RequestParam(name = "sort_by", defaultValue = "id") String sort_by,
                                @RequestParam(name = "order", defaultValue = "ASC") String order,
                                @RequestParam(name = "filter_by", defaultValue = "None") String filter,
                                @RequestParam(name = "value", defaultValue = "None") String value) {
        long userId = userService.getCurrentUser(getToken(token)).getId();
        return fileService.getFilesByFilter(userId, sort_by, order, filter, value);
    }

    @PostMapping(path = "/add")
    @PreAuthorize("isAuthenticated()")
    @RolesAllowed("{USER, ADMIN}")
    public ResponseEntity<?> addFile(@RequestBody FileDTO file,
                                     @RequestHeader("Authorization") String token)
                                            throws NullPointerException{
        try{
            User user = userService.getCurrentUser(getToken(token));
            Set<Role> roles = user.getRoles();
            boolean isAdmin = false;
            for (Role role : roles) {
                if( "ADMIN".equals(role.getName())) {
                    isAdmin = true;
                    break;
                }
                
            }
            if(!isAdmin) {
                if(fileService.isExecutable(file)) 
                    return new ResponseEntity<>(HttpStatusCode.valueOf(415));
            }
            fileService.uploadFile(file, user.getId());
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping(path = "/{idStr}")
    @PreAuthorize("isAuthenticated()")
    @RolesAllowed({ "USER", "ADMIN"})
    public ResponseEntity<?> deleteFile(@PathVariable String idStr,
                                        @RequestHeader("Authorization") String token) {
        File file = fileService.getByIdStr(idStr);
        fileToDelService.addFile(file.getId());
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

}
