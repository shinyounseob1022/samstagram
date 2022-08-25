package com.sparta.samstagram.repository;

import com.sparta.samstagram.domain.Comment;
import com.sparta.samstagram.domain.CommentLike;
import com.sparta.samstagram.domain.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByMemberAndComment(Member member, Comment comment);

    List<CommentLike> findAllByComment(Comment comment);

    List<CommentLike> findAllByMember(Member member);

    Long countByComment(Comment comment);
}
