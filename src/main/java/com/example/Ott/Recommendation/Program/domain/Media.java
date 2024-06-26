package com.example.Ott.Recommendation.Program.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "media")
public class Media {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String content;
  private String category;
  private String creator;
  private String cast;

  @Column(name = "main_poster")
  private String mainPoster;

  @Column(name = "title_poster")
  private String titlePoster;

  @Column(name = "release_date")
  private LocalDate releaseDate;

  @Column(name = "age_rate")
  private String ageRate;

  private boolean recent;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "last_modified_at", nullable = false)
  private LocalDateTime lastModifiedAt;

}
