package com.example.Ott.Recommendation.Program.service;

import com.example.Ott.Recommendation.Program.Dto.SignRequest;
import com.example.Ott.Recommendation.Program.domain.Member;
import com.example.Ott.Recommendation.Program.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;


  public boolean join(SignRequest request) {

    if (request.getEmail().isEmpty() || request.getPassword().isEmpty() || request.getNickname()
        .isEmpty() || request.getPhone().isEmpty()) {
      throw new IllegalArgumentException("빈칸 없이 입력해주세요.");
    }
    if (memberRepository.findByEmail(request.getEmail()).isPresent()){
      throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
    }
    try{
      Member member = Member.builder()
          .email(request.getEmail())
          .nickname(request.getNickname())
          .password(request.getPassword())
          .phone(request.getPhone())
          .build();
      memberRepository.save(member);
    }catch (DataIntegrityViolationException e) {
      System.out.println(e.getMessage());
      throw new IllegalArgumentException("잘못된 요청입니다.");
    }
    return true;
  }
}