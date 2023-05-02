package com.app.happybox.entity.board;

import com.app.happybox.audity.Period;
import com.app.happybox.entity.user.Member;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity @Table(name = "TBL_BOARD")
@Getter @ToString(exclude = {"member", "boardLikes"}) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends Period {
    @EqualsAndHashCode.Include
    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String boardTitle;
    @NotNull
    private String boardContent;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", orphanRemoval = true)
    private List<BoardLike> boardLikes;
}