package com.app.happybox.entity.file;

import com.app.happybox.entity.customer.Notice;
import com.app.happybox.entity.type.FileRepresent;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @ToString(exclude = "notice", callSuper = true)
@Table(name = "TBL_NOTICE_FILE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeFile extends Files {
    @Enumerated(EnumType.STRING)
    private FileRepresent fileRepresent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Notice notice;

    public NoticeFile(String filePath, String fileUuid, String fileOrgName, FileRepresent fileRepresent) {
        super(filePath, fileUuid, fileOrgName);
        this.fileRepresent = fileRepresent;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
