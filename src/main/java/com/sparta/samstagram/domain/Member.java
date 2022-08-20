package com.sparta.samstagram.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
public class Member extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String memberId;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  @JsonIgnore
  private String password;

  @Column
  private String memberImgUrl;

  @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<Post> postList;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Member member = (Member) o;
    return id != null && Objects.equals(id, member.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Builder
  public Member(Long id, String memberId, String nickname, String password, String memberImgUrl) {
    this.id = id;
    this.memberId = memberId;
    this.nickname = nickname;
    this.password = password;
    this.memberImgUrl = memberImgUrl;
  }

  public Member() {}

}
