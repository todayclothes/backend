package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.clothes.entity.TopLike;
import com.seungah.todayclothes.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopLikeRepository extends JpaRepository<TopLike, Long> {
    List<TopLike> findByMember(Member member);

    boolean existsByMemberAndTop(Member member, Top top);

    @Modifying
    @Query("DELETE FROM TopLike t WHERE t.member.id = :userId AND t.top.id = :topId")
    void deleteByMemberAndTop(@Param("userId") Long userId, @Param("topId") Long topId);
}
