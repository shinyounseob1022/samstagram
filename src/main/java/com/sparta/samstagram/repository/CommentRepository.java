package com.sparta.samstagram.repository;

import com.sparta.samstagram.domain.Comment;
import com.sparta.samstagram.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByOrderByModifiedAtDesc();
    List<Comment> findAllByPost(Post post);
    Long countByPost(Post post);
}
