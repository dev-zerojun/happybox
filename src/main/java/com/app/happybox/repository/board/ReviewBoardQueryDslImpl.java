package com.app.happybox.repository.board;

import com.app.happybox.entity.board.QReviewBoardDTO;
import com.app.happybox.entity.board.ReviewBoard;
import com.app.happybox.entity.board.ReviewBoardDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static com.app.happybox.entity.board.QReviewBoard.reviewBoard;

@RequiredArgsConstructor
public class ReviewBoardQueryDslImpl implements ReviewBoardQueryDsl {
    private final JPAQueryFactory query;

//    최신순
    @Override
    public Slice<ReviewBoard> findAllByIdDescWithPaging_QueryDSL(Pageable pageable) {
        List<ReviewBoard> reviewBoards =  query.select(reviewBoard)
                .from(reviewBoard)
                .join(reviewBoard.member).fetchJoin()
                .rightJoin(reviewBoard.subscription.welfare)
                .join(reviewBoard.boardFiles).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return checkLastPage(pageable, reviewBoards);
    }

//    인기순
    @Override
    public Slice<ReviewBoard> findAllByLikeCountDescWithPaging_QueryDSL(Pageable pageable) {
        List<ReviewBoard> reviewBoards = query.select(reviewBoard)
                .from(reviewBoard)
                .join(reviewBoard.member).fetchJoin()
                .rightJoin(reviewBoard.subscription.welfare)
                .join(reviewBoard.boardFiles).fetchJoin()
                .from(reviewBoard)
                .orderBy(reviewBoard.reviewLikeCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return checkLastPage(pageable, reviewBoards);
    }

//    상세보기
    @Override
    public Optional<ReviewBoard> findById_QueryDSL(Long id) {
        return Optional.ofNullable(query.selectDistinct(reviewBoard)
                .from(reviewBoard)
                .join(reviewBoard.member).fetchJoin()
                .rightJoin(reviewBoard.subscription.welfare)
                .join(reviewBoard.boardFiles).fetchJoin()
                .where(reviewBoard.id.eq(id))
                .fetchOne());
    }

    @Override
    public Page<ReviewBoard> findAllByMemberIdDescWithPaging_QueryDSL(Pageable pageable, Long memberId) {
        List<ReviewBoard> reviewBoardList = query.select(reviewBoard)
                .from(reviewBoard)
                .join(reviewBoard.boardFiles).fetchJoin()
                .join(reviewBoard.member).fetchJoin()
                .where(reviewBoard.member.id.eq(memberId))
                .orderBy(reviewBoard.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query.select(reviewBoard.id.count()).from(reviewBoard).where(reviewBoard.member.id.eq(memberId)).fetchOne();

        return new PageImpl<>(reviewBoardList, pageable, count);
    }

    private Slice<ReviewBoard> checkLastPage(Pageable pageable, List<ReviewBoard> reviewBoards) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (reviewBoards.size() > pageable.getPageSize()) {
            hasNext = true;
            reviewBoards.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(reviewBoards, pageable, hasNext);
    }

}
