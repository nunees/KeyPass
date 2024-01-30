package com.keypass.server.token;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

}
