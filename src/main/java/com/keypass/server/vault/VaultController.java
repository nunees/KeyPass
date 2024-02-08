package com.keypass.server.vault;

import com.keypass.server.account.Account;
import com.keypass.server.account.impl.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vaults")
@RequiredArgsConstructor
public class VaultController {
    private final VaultService vaultService;
    private final AccountServiceImpl accountServiceImpl;

    @PostMapping("/new/{userId}")
    public ResponseEntity<Object> createNewVault(@RequestBody VaultRequestDto vaultRequestDto, @PathVariable String userId) {
        Account account = accountServiceImpl.getAccountById(userId).orElseThrow();

        if (!vaultRequestDto.password().equals(vaultRequestDto.confirmPassword())) {
            return ResponseEntity.status(400).body("Password mismatch");
        }

        if (vaultRequestDto.password().length() < 8 || vaultRequestDto.password().length() > 64) {
            return ResponseEntity.status(400).body("The password must have at least 8 characters and a maximum of 64");
        }

        Vault newVault = Vault.builder()
                .name(vaultRequestDto.name())
                .description(vaultRequestDto.description())
                .account(account)
                .password(new BCryptPasswordEncoder().encode(vaultRequestDto.password()))
                .build();


        return ResponseEntity.ok().body(vaultService.save(newVault));
    }
}
