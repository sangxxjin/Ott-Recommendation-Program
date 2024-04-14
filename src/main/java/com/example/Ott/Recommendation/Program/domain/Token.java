package com.example.Ott.Recommendation.Program.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
  @Id
  @JsonIgnore
  @Column(unique = true)
  @NotNull
  private String email;

  private String refresh_token;

  @TimeToLive(unit = TimeUnit.SECONDS)
  private Integer expiration;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRefresh_token() {
    return refresh_token;
  }

  public void setRefresh_token(String refresh_token) {
    this.refresh_token = refresh_token;
  }

  public Integer getExpiration() {
    return expiration;
  }

  public void setExpiration(Integer expiration) {
    this.expiration = expiration;
  }
}