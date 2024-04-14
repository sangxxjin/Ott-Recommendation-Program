package com.example.Ott.Recommendation.Program.service;

import com.example.Ott.Recommendation.Program.dto.SignRequest;
import com.example.Ott.Recommendation.Program.dto.SignResponse;
import com.example.Ott.Recommendation.Program.dto.TokenDto;
import com.example.Ott.Recommendation.Program.domain.Member;
import com.example.Ott.Recommendation.Program.domain.Token;
import com.example.Ott.Recommendation.Program.repository.MemberRepository;
import com.example.Ott.Recommendation.Program.repository.TokenRepository;
import com.example.Ott.Recommendation.Program.security.JwtProvider;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenRepository tokenRepository;
  private final JwtProvider jwtProvider;

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
          .password(passwordEncoder.encode(request.getPassword()))
          .phone(request.getPhone())
          .build();
      memberRepository.save(member);
    }catch (DataIntegrityViolationException e) {
      System.out.println(e.getMessage());
      throw new IllegalArgumentException("잘못된 요청입니다.");
    }
    return true;
  }
  public SignResponse login(SignRequest request) {
    if (request.getEmail().isEmpty() || request.getPassword().isEmpty()) {
      throw new IllegalArgumentException("빈칸 없이 입력해주세요.");
    }

    Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(() ->
        new IllegalArgumentException("존재하지 않는 이메일입니다."));

    if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
      throw new IllegalArgumentException("로그인 정보가 올바르지 않습니다.");
    }

    member.setRefreshToken(createRefreshToken(member));

    return SignResponse.builder()
        .email(member.getEmail())
        .nickname(member.getNickname())
        .phone(member.getPhone())
        .token(TokenDto.builder()
            .accessToken(jwtProvider.createToken(member.getEmail()))
            .refreshToken(member.getRefreshToken())
            .build())
        .build();
  }

  public String createRefreshToken(Member member) {
    Token token = tokenRepository.save(
        Token.builder()
            .email(member.getEmail())
            .refresh_token(UUID.randomUUID().toString())
            .expiration(300)
            .build()
    );
    return token.getRefresh_token();
  }
}