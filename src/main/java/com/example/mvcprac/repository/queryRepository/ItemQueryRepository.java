package com.example.mvcprac.repository.queryRepository;

import com.example.mvcprac.dto.item.ItemDetailDto;
import com.example.mvcprac.dto.item.ItemListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.mvcprac.model.QItem.item;

@Repository
public class ItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ItemQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 전체 아이템 조회
     */
    public Page<ItemListDto> findItemList(Pageable pageable) {
        List<ItemListDto> itemList = queryFactory
                .select(Projections.constructor(ItemListDto.class,
                        item.id,
                        item.itemName,
                        item.itemSellStatus,
                        item.deliveryChoice,
                        item.price))
                .from(item)
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(item.count())
                .from(item);
        return PageableExecutionUtils.getPage(itemList, pageable, countQuery :: fetchOne);
    }

    /**
     * 아이템 하나 조회
     */
    public ItemDetailDto findOneItem(Long id) {
        queryFactory
                .select(Projections.constructor(ItemDetailDto.class,
                        item.id,
                        item.itemName,
                        item.itemBody,
                        item.itemSellStatus,
                        item.deliveryChoice,
                        item.price))
                .from(item);

    }
}
