package com.keypass.server.token;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import com.keypass.server.account.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Lob
  private String token;

  @Column(nullable = false)
  private LocalDateTime expiresIn;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", referencedColumnName = "id")
  private Account account;

  @Column(columnDefinition = "boolean default false")
  private boolean revoked;

  @Column(columnDefinition = "boolean default false")
  private boolean expired;

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
