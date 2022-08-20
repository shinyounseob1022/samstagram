package com.sparta.samstagram.service;

import com.sparta.samstagram.domain.Member;
import com.sparta.samstagram.domain.Post;
import com.sparta.samstagram.dto.request.PostRequestDto;
import com.sparta.samstagram.dto.response.PostResponseDto;
import com.sparta.samstagram.dto.response.ResponseDto;
import com.sparta.samstagram.jwt.TokenProvider;
import com.sparta.samstagram.repository.CommentRepository;
import com.sparta.samstagram.repository.PostLikeRepository;
import com.sparta.samstagram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TokenProvider tokenProvider;
    private final S3UploadService s3UploadService;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

    public ResponseDto<?> createPost(PostRequestDto requestDto,
                                     MultipartFile multipartFile,
                                     HttpServletRequest request) throws IOException {

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember();
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        String imgUrl = "";
        if (!multipartFile.isEmpty()) {
            imgUrl = s3UploadService.upload(multipartFile, "samstagram");
            System.out.println("imgUrl: "+imgUrl);
        }

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imgUrl(imgUrl)
                .member(member)
                .build();
        postRepository.save(post);

        return ResponseDto.success(
                PostResponseDto.builder()
                        .author(post.getMember().getNickname())
                        .authorImgUrl(post.getMember().getAuthorImgUrl())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .imgUrl(post.getImgUrl())
                        .postLikeCnt(postLikeRepository.countByPost(post))
                        .commentCnt(commentRepository.countByPost(post))
                        .createdAt(post.getMember().getCreatedAt())
                        .modifiedAt(post.getMember().getModifiedAt())
                        .build()
        );
    }



    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    @Transactional
    public Member validateMember() {
//        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
//            return null;
//        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
