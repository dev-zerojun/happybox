package com.app.happybox.repository.order;

import com.app.happybox.domain.MemberOrderProductItemDTO;
import com.app.happybox.entity.order.MemberOrderProductItem;
import com.app.happybox.service.order.MemberOrderProductItemService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional @Rollback(false)
@Slf4j
public class MemberOrderProductItemRepositoryTests {
    @Autowired private MemberOrderProductItemRepository memberOrderProductItemRepository;
    @Autowired private MemberOrderProductItemService memberOrderProductItemService;

    @Test
    public void findCancleListByMemberIdDescWithPagingTest() {
        LocalDateTime startDate = LocalDateTime.of(2023, 2, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.now();

        memberOrderProductItemRepository
                .findCancleListByMemberIdAndSearchDateDescWithPaging_QueryDSL(PageRequest.of(0, 5), 1L, startDate, endDate)
                .stream().map(MemberOrderProductItem::toString).forEach(log::info);
    }

    @Test
    public void findSaleListByWelfareIdAndSearchDateDescWithPagingTest() {
        LocalDateTime startDate = LocalDateTime.of(2023, 2, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.now();

        memberOrderProductItemRepository
                .findSaleListByDistributorIdAndSearchDateDescWithPaging_QueryDSL(PageRequest.of(0, 5), 42L, startDate, endDate)
                .stream().map(MemberOrderProductItem::getMemberOrderProduct).forEach(v -> log.info(v.getMember().toString()));
    }

    @Test
    public void findCancleListByDistributorIdAndSearchDateDescWithPaging_QueryDSL_Test() {
        LocalDateTime startDate = LocalDateTime.of(2023, 2, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.now();

        memberOrderProductItemRepository
                .findCancleListByDistributorIdAndSearchDateDescWithPaging_QueryDSL(PageRequest.of(0, 5), 42L, startDate, endDate)
                .stream().map(MemberOrderProductItem::getMemberOrderProduct).forEach(v -> log.info(v.getMember().toString()));
    }

    @Test
    public void findSaleCountByDistributorAndPurchaseStatus_QueryDSL_Test() {
        log.info("saleCount : " + memberOrderProductItemRepository.findSaleCountByDistributorAndPurchaseStatus_QueryDSL(42L));
    }

    @Test
    public void findCancleCountByDistributorAndPurchaseStatus_QueryDSL_Test() {
        log.info("cancleCount : " + memberOrderProductItemRepository.findCancleCountByDistributorAndPurchaseStatus_QueryDSL(42L));
    }

    @Test
    public void memberOrderServiceTest() {
        memberOrderProductItemService.getListByIdAndSearchDate(PageRequest.of(0, 5), 1L)
                .stream().map(MemberOrderProductItemDTO::toString).forEach(log::info);
    }
}