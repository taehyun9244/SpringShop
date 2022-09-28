package com.example.mvcprac.repository;

import com.example.mvcprac.dto.item.ItemDetailDto;
import com.example.mvcprac.dto.item.ItemImageDto;
import com.example.mvcprac.dto.item.ItemListDto;
import com.example.mvcprac.dto.item.ItemSearchDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.mvcprac.model.QImage.image;
import static com.example.mvcprac.model.QItem.item;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
public class ItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ItemQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<ItemListDto> findAllItem(ItemSearchDto itemSearchDto, Pageable pageable){
        List<ItemListDto> itemList = queryFactory
                .select(Projections.constructor(
                        ItemListDto.class,
                        image.item.id,
                        image.item.itemName,
                        image.item.itemSellStatus,
                        image.item.deliveryChoice,
                        image.item.price,
                        image.storeFileName
                ))
                .from(image)
                .join(image.item, item)
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(image.count())
                .from(image);

        return PageableExecutionUtils.getPage(itemList, pageable, countQuery :: fetchOne);

    }

    /**
     * findByItemId Not Use
     */
    public List<ItemDetailDto> findByIdItem(Long id) {
        return queryFactory
                .from(image)
                .join(image.item, item)
                .on(image.item.id.eq(item.id))
                .where(itemId(id))
                .orderBy(image.id.desc())
                .transform(
                        groupBy(item.id).list(Projections.constructor(
                        ItemDetailDto.class,
                        image.item.id,
                        image.item.itemName,
                        image.item.itemBody,
                        image.item.itemSellStatus,
                        image.item.deliveryChoice,
                        image.item.price,
                                list(Projections.constructor(
                                ItemImageDto.class,
                                image.id,
                                image.storeFileName
                        )
                ))));

    }
    private BooleanExpression itemNameLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : item.itemName.like("%" + searchQuery + "%");
    }

    private BooleanExpression itemId(Long id) {
        return id != null ? image.item.id.eq(id) : image.item.isNull();
    }

}
