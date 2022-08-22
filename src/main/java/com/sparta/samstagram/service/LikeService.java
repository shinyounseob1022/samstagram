package com.sparta.samstagram.service;

import com.sparta.samstagram.domain.Member;
import com.sparta.samstagram.domain.Post;
import com.sparta.samstagram.domain.PostLike;
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
    public ResponseDto<?> likePost(Long id, HttpServletRequest request) {
        Member member = validateMember(request);
//        if (null == member) {
//            return ResponseDto.fail("INVALID_TOKEN", "refresh token is invalid");
//        }

        Post post = postService.isPresentPost(id);
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
            return ResponseDto.success("like success");
        } else {
            postLikeRepository.delete(postLike);
            return ResponseDto.success("cancel like success");
        }
    }





    @Transactional(readOnly = true)
    public PostLike isPresentPostLike(Member member, Post post) {
        Optional<PostLike> optionalPostLike = postLikeRepository.findByMemberAndPost(member, post);
        return optionalPostLike.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
