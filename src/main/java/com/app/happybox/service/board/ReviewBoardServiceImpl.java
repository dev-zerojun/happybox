package com.app.happybox.service.board;

import com.app.happybox.entity.board.ReviewBoard;
import com.app.happybox.entity.board.ReviewBoardDTO;
import com.app.happybox.repository.board.ReviewBoardRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("reviewBoard")
public class ReviewBoardServiceImpl implements ReviewBoardService {
    private final ReviewBoardRepository reviewBoardRepository;

    @Override
    public Slice<ReviewBoardDTO> getReviewBoards(Pageable pageable) {
        Slice<ReviewBoard> reviewBoards =
                reviewBoardRepository.findAllByIdDescWithPaging_QueryDSL(PageRequest.of(0, 10));
        List<ReviewBoardDTO> collect = reviewBoards.get().map(board -> reviewBoardToDTO(board)).collect(Collectors.toList());

        return new SliceImpl<>(collect, pageable, reviewBoards.hasNext());
    }

    @Override
    public Slice<ReviewBoardDTO> getPopularReviewBoards(Pageable pageable) {
        Slice<ReviewBoard> reviewBoards =
                reviewBoardRepository.findAllByLikeCountDescWithPaging_QueryDSL(PageRequest.of(0, 10));
        List<ReviewBoardDTO> collect = reviewBoards.get().map(board -> reviewBoardToDTO(board)).collect(Collectors.toList());
        return new SliceImpl<>(collect, pageable, reviewBoards.hasNext());
    }

    @Override
    public Page<ReviewBoardDTO> findAllByMemberIdDescWithPaging_QueryDSL(Pageable pageable, Long memberId) {
        return null;
    }
}