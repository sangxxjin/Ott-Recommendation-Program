package com.example.Ott.Recommendation.Program.service;

import com.example.Ott.Recommendation.Program.domain.Member;
import com.example.Ott.Recommendation.Program.repository.MemberRepository;
import com.example.Ott.Recommendation.Program.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member user = memberRepository.findByEmail(email).orElseThrow(
        () -> new UsernameNotFoundException("사용자가 존재하지 않습니다.")
    );
    return new CustomUserDetails(user);
  }
}