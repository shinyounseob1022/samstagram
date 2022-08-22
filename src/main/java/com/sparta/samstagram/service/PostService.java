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
import java.util.ArrayList;
import java.util.List;
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

        String imgUrl = null;
        if (!multipartFile.isEmpty()) {
            imgUrl = s3UploadService.upload(multipartFile, "samstagram/postImg");
            System.out.println("imgUrl: "+imgUrl);
        }

        Post post = Post.builder()
                .content(requestDto.getContent())
                .imgUrl(imgUrl)
                .member(member)
                .build();
        postRepository.save(post);

        return ResponseDto.success(
                PostResponseDto.builder()
                        .postId(post.getId())
                        .author(post.getMember().getNickname())
                        .authorImgUrl(post.getMember().getMemberImgUrl())
                        .content(post.getContent())
                        .imgUrl(post.getImgUrl())
                        .postLikeCnt(postLikeRepository.countByPost(post))
                        .commentCnt(commentRepository.countByPost(post))
                        .isModalMode(false)
                        .createdAt(post.getMember().getCreatedAt())
                        .modifiedAt(post.getMember().getModifiedAt())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPost() {
        List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post post : postList) {

            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .postId(post.getId())
                            .author(post.getMember().getNickname())
                            .authorImgUrl(post.getMember().getMemberImgUrl())
                            .content(post.getContent())
                            .imgUrl(post.getImgUrl())
                            .postLikeCnt(postLikeRepository.countByPost(post))
                            .commentCnt(commentRepository.countByPost(post))
                            .isModalMode(false)
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }

        return ResponseDto.success(postResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> getPost(Long postId) {
        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        return ResponseDto.success(
                PostResponseDto.builder()
                        .postId(post.getId())
                        .author(post.getMember().getMemberId())
                        .authorImgUrl((post.getMember().getMemberImgUrl()))
                        .content(post.getContent())
                        .imgUrl(post.getImgUrl())
                        .postLikeCnt(postLikeRepository.countByPost(post))
                        .commentCnt(commentRepository.countByPost(post))
                        .isEditMode(false)
                        .isModalMode(false)
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> updatePost(Long postId, PostRequestDto requestDto,
                                             MultipartFile multipartFile, HttpServletRequest request) throws IOException {
//        if (null == request.getHeader("Refresh-Token")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember();
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        String imgUrl = post.getImgUrl();
        if (!multipartFile.isEmpty()) {
            imgUrl = s3UploadService.upload(multipartFile, "postImg");
            System.out.println("imgUrl: "+imgUrl);
        } else {
            imgUrl = null;
        }

        post.update(requestDto, imgUrl);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .postId(post.getId())
                        .author(post.getMember().getNickname())
                        .authorImgUrl(post.getMember().getMemberImgUrl())
                        .content(post.getContent())
                        .imgUrl(imgUrl)
                        .postLikeCnt(postLikeRepository.countByPost(post))
                        .commentCnt(commentRepository.countByPost(post))
                        .isModalMode(false)
                        .createdAt(post.getMember().getCreatedAt())
                        .modifiedAt(post.getMember().getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deletePost(Long postId, HttpServletRequest request) {
//        if (null == request.getHeader("Refresh-Token")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember();
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);

        return ResponseDto.success(null);
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
