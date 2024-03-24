package com.example.Ott.Recommendation.Program.controller;

import com.example.Ott.Recommendation.Program.Dto.SignRequest;
import com.example.Ott.Recommendation.Program.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
  private final MemberService memberService;

  @PostMapping("/join")
  public ResponseEntity<Boolean> join(@RequestBody SignRequest request) {
    return new ResponseEntity<>(memberService.join(request), HttpStatus.OK);
  }

}
