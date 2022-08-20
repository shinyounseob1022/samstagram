package com.sparta.samstagram.domain;

import com.sparta.samstagram.dto.request.PostRequestDto;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column
    private String imgUrl;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikes;

    public void update(PostRequestDto postRequestDto, String imgUrl) {
        this.content = postRequestDto.getContent();
        this.imgUrl = imgUrl;
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

    @Builder
    public Post(String content, String imgUrl, Member member) {
        this.content = content;
        this.imgUrl = imgUrl;
        this.member = member;
    }

    public Post() {}
}
