package com.app.happybox.repository.board;

import com.app.happybox.entity.board.RecipeBoard;
import com.app.happybox.entity.board.ReviewBoard;
import com.app.happybox.entity.type.FileRepresent;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static com.app.happybox.entity.board.QRecipeBoard.recipeBoard;

@RequiredArgsConstructor
public class RecipeBoardQueryDslImpl implements RecipeBoardQueryDsl {
    private final JPAQueryFactory query;

    @Override
    public Slice<RecipeBoard> findAllByIdDescWithPaging_QueryDSL(Pageable pageable) {
        List<RecipeBoard> recipeBoards = query.select(recipeBoard)
                .from(recipeBoard)
                .join(recipeBoard.member).fetchJoin()
                .join(recipeBoard.boardFiles).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return checkLastPage(pageable, recipeBoards);
    }

    @Override
    public Slice<RecipeBoard> findAllByLikeCountDescWithPaging_QueryDSL(Pageable pageable) {
        List<RecipeBoard> recipeBoards = query.select(recipeBoard)
                .from(recipeBoard)
                .join(recipeBoard.member).fetchJoin()
                .join(recipeBoard.boardFiles).fetchJoin()
                .orderBy(recipeBoard.recipeLikeCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return checkLastPage(pageable, recipeBoards);
    }

    @Override
    public Page<RecipeBoard> findRecipeBoardListByMemberIdWithPaging_QueryDSL(Pageable pageable, Long memberId) {
        List<RecipeBoard> recipeBoardList = query.select(recipeBoard)
                .from(recipeBoard)
                .join(recipeBoard.member).fetchJoin()
                .join(recipeBoard.boardFiles).fetchJoin()
//                .join(recipeBoard.recipeBoardReplies).fetchJoin()
                .where(recipeBoard.member.id.eq(memberId))
                .orderBy(recipeBoard.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query.select(recipeBoard.id.count()).from(recipeBoard).where(recipeBoard.member.id.eq(memberId)).fetchOne();

        return new PageImpl<>(recipeBoardList, pageable, count);
    }

    //    댓글 수
    @Override
    public List<RecipeBoard> findRecipeBoardReplyCountByMemberId_QueryDSL(Long memberId) {
        List<RecipeBoard> recipeBoardList = query.select(recipeBoard)
                .from(recipeBoard)
                .join(recipeBoard.recipeBoardReplies).fetchJoin()
                .where(recipeBoard.member.id.eq(memberId))
                .fetch();

        return recipeBoardList;
    }

    @Override
    public List<RecipeBoard> findTop5ByLikeCountWithRepresentFileOrderByLikeCount_QueryDSL() {
        List<RecipeBoard> recipeBoards = query.select(recipeBoard)
                .from(recipeBoard)
                .join(recipeBoard.boardFiles)
                .fetchJoin()
                .where(recipeBoard.boardFiles.any().fileRepresent.eq(FileRepresent.REPRESENT))
                .orderBy(recipeBoard.recipeLikeCount.desc())
                .limit(5L)
                .fetch();

        return recipeBoards;
    }

    //    hasNext true인지 false인지 체크하는 메소드(마지막 페이지 체크)
    private Slice<RecipeBoard> checkLastPage(Pageable pageable, List<RecipeBoard> recipeBoards) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (recipeBoards.size() > pageable.getPageSize()) {
            hasNext = true;
            recipeBoards.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(recipeBoards, pageable, hasNext);
    }
}
