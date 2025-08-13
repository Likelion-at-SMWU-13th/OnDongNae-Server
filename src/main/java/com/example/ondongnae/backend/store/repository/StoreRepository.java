package com.example.ondongnae.backend.store.repository;

import com.example.ondongnae.backend.store.model.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    // 이미지/시장까지만 가져오기 (메뉴 제외)
    @EntityGraph(attributePaths = {
            "storeImages",
            "market",
            "member"
    })
    Optional<Store> findById(Long id);

    Optional<Store> findByMemberId(Long member_Id);

    @Query(value = "SELECT * FROM store ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Store> pickRandom();

    @Query("""
        select s
        from Store s
        join s.storeSubCategories ssc
        where (:marketId is null or s.market.id = :marketId)
            and (:mainCategoryId is null or s.mainCategory.id = :mainCategoryId)
            and ssc.subCategory.id in :subCategoryIds
        group by s.id
        having count(distinct ssc.subCategory.id) = :subCategoryCount
    """)
    List<Store> findByMarketIdAndMainCategoryIdAndSubCategoryIds(@Param("marketId") Long marketId,
                                                                      @Param("mainCategoryId") Long mainCategoryId,
                                                                      @Param("subCategoryIds") List<Long> subCategoryIds,
                                                                      @Param("subCategoryCount") Long subCategoryCount);

    @Query("""
        select s
        from Store s
        where (:marketId is null or s.market.id = :marketId)
        and (:mainCategoryId is null or s.mainCategory.id = :mainCategoryId)
""")
    List<Store> findByMarketIdAndMainCategoryId(@Param("marketId") Long marketId,
                                                  @Param("mainCategoryId") Long mainCategoryId);

    @Query("""
    select s
    from Store s
    where
        lower(s.nameEn) like lower(concat('%', :keyword, '%')) or
        lower(s.nameKo) like lower(concat('%', :keyword, '%')) or
        lower(s.nameZh) like lower(concat('%', :keyword, '%')) or
        lower(s.nameJa) like lower(concat('%', :keyword, '%'))
""")
    List<Store> findByKeyword(@Param("keyword") String keyword);

}
