package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.entity.BottomLike;
import com.seungah.todayclothes.domain.clothes.repository.queryDsl.BottomLikeQueryRepository;
import com.seungah.todayclothes.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BottomLikeRepository extends JpaRepository<BottomLike, Long>,
    BottomLikeQueryRepository {

    boolean existsByMemberAndBottom(Member member, Bottom bottom);

    @Modifying
    @Query("DELETE FROM BottomLike b WHERE b.member.id = :userId AND b.bottom.id = :bottomId")
    void deleteByMemberAndBottom(@Param("userId") Long userId, @Param("bottomId") Long bottomId);

}
