package com.example.Ott.Recommendation.Program.repository;

import com.example.Ott.Recommendation.Program.domain.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
}