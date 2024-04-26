package tad.blps.controller;

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

@RestController
@RequestMapping("/file")
public class fileController {
    private final FileService fileService;
    private final UserService userService;

    @Autowired
    public fileController(FileService fileService,
                          UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    TokenDTO getToken(String BearerToken) {
        String token = BearerToken.substring(7);
        return new TokenDTO(token);
    }

    @GetMapping(path = "/list")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public List<File> getFiles(@RequestHeader("Authorization") String token,
                                @RequestParam(name = "sort_by", defaultValue = "id") String sort_by,
                                @RequestParam(name = "order", defaultValue = "ASC") String order,
                                @RequestParam(name = "filter_by", defaultValue = "None") String filter,
                                @RequestParam(name = "value", defaultValue = "None") String value) {
        long userId = userService.getCurrentUser(getToken(token)).getId();
        System.out.println(userId);
        return fileService.getFilesByFilter(userId, sort_by, order, filter, value);
    }

    @PostMapping(path = "/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addFile(@RequestBody FileDTO file,
                                     @RequestHeader("Authorization") String token)
                                            throws NullPointerException{
        try{
            long userId = userService.getCurrentUser(getToken(token)).getId();
            fileService.uploadFile(file, userId);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    @DeleteMapping(path = "/{idStr}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteFile(@PathVariable String idStr,
                                        @RequestHeader("Authorization") String token) {
        long userId = userService.getCurrentUser(getToken(token)).getId();
        fileService.deleteFile(idStr, userId);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

}
