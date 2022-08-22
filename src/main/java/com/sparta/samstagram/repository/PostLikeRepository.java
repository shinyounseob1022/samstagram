package com.sparta.samstagram.repository;

import com.sparta.samstagram.domain.Member;
import com.sparta.samstagram.domain.Post;
import com.sparta.samstagram.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByMemberAndPost(Member member, Post post);
    Long countByPost(Post post);

    List<PostLike> findAllByPost(Post post);
    List<PostLike> findAllByMember(Member member);
}
