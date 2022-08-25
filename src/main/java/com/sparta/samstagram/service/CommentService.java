package com.sparta.samstagram.service;

import com.sparta.samstagram.domain.*;
import com.sparta.samstagram.dto.request.CommentRequestDto;
import com.sparta.samstagram.dto.response.CommentResponseDto;
import com.sparta.samstagram.dto.response.ResponseDto;
import com.sparta.samstagram.jwt.TokenProvider;
import com.sparta.samstagram.repository.CommentLikeRepository;
import com.sparta.samstagram.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;

    private final TokenProvider tokenProvider;

    private final PostService postService;

    private final MemberService memberService;

    @Transactional
    public ResponseDto<?> createComment(Long postId,CommentRequestDto requestDto, HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Authorization is invalid");
        }

        Post post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "post id is not exist");
        }

        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);

        return ResponseDto.success(
                CommentResponseDto.builder()
                        .postId(post.getId())
                        .commentId(comment.getId())
                        .author(comment.getMember().getNickname())
                        .authorImgUrl(comment.getMember().getMemberImgUrl())
                        .content(comment.getContent())
                        .commentLikeCnt(commentLikeRepository.countByComment(comment))
                        .isEditMode(false)
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllComment(Long postId, String nickname) {
        Post post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "post id is not exist");
        }

        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {

            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .postId(post.getId())
                            .commentId(comment.getId())
                            .author(comment.getMember().getNickname())
                            .authorImgUrl(comment.getMember().getMemberImgUrl())
                            .content(comment.getContent())
                            .commentLikeCnt(commentLikeRepository.countByComment(comment))
                            .isEditMode(false)
                            .isLike(isLikeMethod(comment, nickname))
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(commentResponseDtoList);
    }

    @Transactional
    public boolean isLikeMethod(Comment comment, String nickname) {
        List<CommentLike> commentLikeList = comment.getCommentLikes();
        Member member = memberService.isPresentMemberByNickname(nickname);
        boolean isLike = false;
        for (CommentLike commentLike : commentLikeList) {
            if (commentLike.getMember().getId() == member.getId()) {
                isLike = true;
            }
        }
        return isLike;
    }

    @Transactional
    public ResponseDto<?> updateComment(Long postId,Long commentId, CommentRequestDto requestDto, HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Authorization is invalid");
        }

        Post post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "post id is not exist");
        }

        Comment comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "comment id is not exist");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "only author can update");
        }

        return ResponseDto.success(
                CommentResponseDto.builder()
                        .commentId(comment.getId())
                        .author(comment.getMember().getNickname())
                        .authorImgUrl(post.getMember().getMemberImgUrl())
                        .content(comment.getContent())
                        .commentLikeCnt(commentLikeRepository.countByComment(comment))
                        .isEditMode(false)
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deleteComment(Long postId,Long commentId, HttpServletRequest request) {
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Authorization is invalid");
        }

        Post post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "post id is not exist");
        }

        Comment comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "comment id is not exist");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "only author can update");
        }

        commentRepository.delete(comment);
        return ResponseDto.success("success");
    }


    @Transactional(readOnly = true)
    public Comment isPresentComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        return tokenProvider.getMemberFromAuthentication();
    }
}
