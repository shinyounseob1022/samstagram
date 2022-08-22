package com.sparta.samstagram.service;

import com.sparta.samstagram.domain.*;
import com.sparta.samstagram.dto.response.CommentResponseDto;
import com.sparta.samstagram.dto.response.LikeCommentResponseDto;
import com.sparta.samstagram.dto.response.LikePostResponseDto;
import com.sparta.samstagram.dto.response.ResponseDto;
import com.sparta.samstagram.jwt.TokenProvider;
import com.sparta.samstagram.repository.CommentLikeRepository;
import com.sparta.samstagram.repository.CommentRepository;
import com.sparta.samstagram.repository.PostLikeRepository;
import com.sparta.samstagram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final TokenProvider tokenProvider;
    private final PostService postService;
    private final CommentService commentService;

    @Transactional
    public ResponseDto<?> likePost(Long postId, HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "refresh token is invalid");
        }

        Post post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "post id is not exist");
        }

        PostLike postLike = isPresentPostLike(member, post);
        if (null == postLike) {
            postLikeRepository.save(
                    PostLike.builder()
                            .member(member)
                            .post(post)
                            .build()
            );
            return ResponseDto.success(
                    LikePostResponseDto.builder()
                            .postId(post.getId())
                            .isLike(true)
                            .postLikeCnt(countLikesPost(post))
                            .build()
            );
        } else {
            postLikeRepository.delete(postLike);
            return ResponseDto.success(
                    LikePostResponseDto.builder()
                            .postId(post.getId())
                            .isLike(false)
                            .postLikeCnt(countLikesPost(post))
                            .build()
            );
        }
    }

    @Transactional
    public ResponseDto<?> likeComment(Long postId, Long commentId, HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "refresh token is invalid");
        }

        Post post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "post id is not exist");
        }

        Comment comment = commentService.isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "comment id is not exist");
        }

        CommentLike commentLike = isPresentCommentLike(member, comment);
        if (null == commentLike) {
            commentLikeRepository.save(
                    CommentLike.builder()
                            .member(member)
                            .comment(comment)
                            .build()
            );
            return ResponseDto.success(
                    LikeCommentResponseDto.builder()
                            .commentId(comment.getId())
                            .isLike(true)
                            .commentLikeCnt(countLikesComment(comment))
                            .build()
            );
        } else {
            commentLikeRepository.delete(commentLike);
            return ResponseDto.success(
                    LikeCommentResponseDto.builder()
                            .commentId(comment.getId())
                            .isLike(false)
                            .commentLikeCnt(countLikesComment(comment))
                            .build()
            );
        }
    }



    @Transactional(readOnly = true)
    public int countLikesPost(Post post) {
        List<PostLike> postLikeList = postLikeRepository.findAllByPost(post);
        return postLikeList.size();
    }
    @Transactional(readOnly = true)
    public int countLikesComment(Comment comment) {
        List<CommentLike> commentLikeList = commentLikeRepository.findAllByComment(comment);
        return commentLikeList.size();
    }
    @Transactional(readOnly = true)
    public PostLike isPresentPostLike(Member member, Post post) {
        Optional<PostLike> optionalPostLike = postLikeRepository.findByMemberAndPost(member, post);
        return optionalPostLike.orElse(null);
    }
    @Transactional(readOnly = true)
    public CommentLike isPresentCommentLike(Member member, Comment comment) {
        Optional<CommentLike> optionalCommentLike = commentLikeRepository.findByMemberAndComment(member, comment);
        return optionalCommentLike.orElse(null);
    }


    @Transactional
    public Member validateMember(HttpServletRequest request) {
//        if (!tokenProvider.validateToken(request.getHeader("Authorization"))) {
//            return null;
//        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
