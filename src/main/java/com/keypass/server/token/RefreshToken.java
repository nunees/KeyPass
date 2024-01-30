package com.keypass.server.token;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.keypass.server.account.Account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UUID id;

  @Column(nullable = false, unique = true, columnDefinition = "TEXT")
  private String refresh_token;

  @Column(nullable = false)
  private Long expiresIn;

  @ManyToOne
  @JoinColumn(name = "account_id", referencedColumnName = "id")
  private Account account;

  @Column(columnDefinition = "boolean default false")
  private Boolean revoked;

  @Column(columnDefinition = "boolean default false")
  private Boolean expired;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @CreationTimestamp
  private LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    this.revoked = false;
    this.expired = false;
  }

}
