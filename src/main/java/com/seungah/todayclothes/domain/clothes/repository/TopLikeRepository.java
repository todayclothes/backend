package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.clothes.entity.TopLike;
import com.seungah.todayclothes.domain.clothes.repository.queryDsl.TopLikeQueryRepository;
import com.seungah.todayclothes.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopLikeRepository extends JpaRepository<TopLike, Long>,
    TopLikeQueryRepository {

    boolean existsByMemberAndTop(Member member, Top top);

    @Modifying
    @Query("DELETE FROM TopLike t WHERE t.member.id = :userId AND t.top.id = :topId")
    void deleteByMemberAndTop(@Param("userId") Long userId, @Param("topId") Long topId);
}
