package com.example.Ott.Recommendation.Program.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {

  private String email;

  private String nickname;

  private String phone;

  private TokenDto token;
}