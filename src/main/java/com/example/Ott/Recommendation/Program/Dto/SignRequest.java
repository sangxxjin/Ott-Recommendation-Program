package com.example.Ott.Recommendation.Program.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignRequest {

  private String email;
  private String nickname;
  private String password;
  private String phone;

}
