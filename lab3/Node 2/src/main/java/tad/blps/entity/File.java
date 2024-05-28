package tad.blps.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import tad.blps.DTO.FileDTO;

import java.io.Serializable;
import java.sql.Date;


@Entity
@Getter
@Setter
@Table(name = "file")
public class File implements Serializable, Persistable<Long> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "download_cnt")
    private int download_cnt;

    @Column(name = "download_url")
    private String download_url;

    @Column(name = "dt_added")
    private Date dt_added;

    @Column(name = "dt_expires")
    private Date dt_expires;

    @Column(name = "file_password")
    private String file_password;

    @Column(name = "filename_source")
    private String filename_source;

    @Column(name = "idstr")
    private String idstr;

    @Column(name = "size")
    private Long size;

    @Column(name = "user_id")
    private Long userId;

    public File() {
        this.download_cnt = 0;
        this.dt_added = new Date(System.currentTimeMillis());
        this.dt_expires = new Date(System.currentTimeMillis() + 14400000);

    }

    public File(FileDTO file) {
        this.filename_source = file.getFilename();
        this.file_password = file.getFile_password();
        this.size = file.getSize();
    }
    @Override
    public boolean isNew() {
        return false;
    }
}
