package com.keypass.server.account;

import com.keypass.server.account.dto.AccountRequestDto;
import com.keypass.server.account.dto.AccountResponseDto;
import com.keypass.server.account.dto.AccountUpdateRequestDto;
import com.keypass.server.account.exception.AccountAlreadyExistException;
import com.keypass.server.account.exception.AccountNotFoundException;
import com.keypass.server.account.exception.AccountOperationNotPermitedException;
import com.keypass.server.exception.GeneralException;
import com.keypass.server.exception.GeneralResponseDTO;
import com.keypass.server.exception.MissingFieldsException;
import com.nimbusds.jose.proc.SecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping(value = "/accounts")
@Tag(name = "Accounts", description = "Create and manage user accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final ModelMapper modelMapper;

    @Operation(summary = "Create account controller",
            description = "Create a new user account",
            tags = {"account", "create"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Account created", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
                            @Schema(implementation = AccountResponseDto.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "User Already Exists", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
                            @Schema(implementation = GeneralResponseDTO.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Blank fields on form", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
                            @Schema(implementation = GeneralResponseDTO.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Bad Request", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
                            @Schema(implementation = GeneralResponseDTO.class))
                    })
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/register")
    public ResponseEntity<AccountResponseDto> create(@RequestBody AccountRequestDto accountRequestDto) {

        if (accountRequestDto.firstName() == null
                || accountRequestDto.lastName() == null
                || accountRequestDto.username() == null
                || accountRequestDto.email() == null
                || accountRequestDto.password() == null
        ) {
            throw new MissingFieldsException("Check for blank fields on the form");
        }

        String hashedPassword = new BCryptPasswordEncoder().encode(accountRequestDto.password());
        Account newAccount = Account.builder()
                .firstName(accountRequestDto.firstName())
                .lastName(accountRequestDto.lastName())
                .username(accountRequestDto.username())
                .password(hashedPassword)
                .email(accountRequestDto.email())
                .build();

        Account userCreated = accountService.create(newAccount);

        AccountResponseDto response = new AccountResponseDto(
                userCreated.getId(),
                userCreated.getFirstName(),
                userCreated.getLastName(),
                userCreated.getUsername(),
                userCreated.getEmail()
        );

        return ResponseEntity.status(201).body(response);

    }


    @Operation(summary = "Get account by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "400", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Bad Request")
    })
    @SecurityRequirement(name = "bearer-key")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAccountById(@PathVariable("id") String userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account userAccount = accountService.getAccountByUsername(authentication.getName());

        if(userAccount.getId().equals(UUID.fromString(userId))){
            return ResponseEntity.ok().body(new AccountResponseDto(
                    userAccount.getId(),
                    userAccount.getFirstName(),
                    userAccount.getLastName(),
                    userAccount.getUsername(),
                    userAccount.getEmail()
            ));
        }

        return ResponseEntity.notFound().build();
    }


    @Operation(summary = "Delete account controller",
            description = "Delete an existing user account",
            tags = {"account", "delete"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Deleted", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
                            @Schema(implementation = AccountResponseDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Blank fields on form", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
                            @Schema(implementation = GeneralResponseDTO.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Bad Request", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
                            @Schema(implementation = GeneralResponseDTO.class))
                    })
            }
    )
    @SecurityRequirement(name = "bearer-key")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteAccountById(@PathVariable("id") String id) {

        Account account = accountService.getAccountById(id).orElseThrow(() -> new AccountAlreadyExistException("Account not found"));

        accountService.deleteAccountById(account.getId().toString());

        return ResponseEntity.ok(account);
    }


    @Operation(summary = "Update account controller",
            description = "Update an existing user account",
            tags = {"account", "update"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
                            @Schema(implementation = AccountResponseDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Blank fields on form", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
                            @Schema(implementation = GeneralResponseDTO.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "User not allowed", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
                            @Schema(implementation = GeneralResponseDTO.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Bad Request", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema =
                            @Schema(implementation = GeneralResponseDTO.class))
                    })
            }
    )
    @SecurityRequirement(name = "bearer-key")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUserAccount(
            @PathVariable("id") UUID userId,
            @RequestBody AccountUpdateRequestDto accountUpdateRequestDto) {

        if (accountUpdateRequestDto.firstName() == null
                || accountUpdateRequestDto.lastName() == null
                || accountUpdateRequestDto.username() == null
                || accountUpdateRequestDto.email() == null
        ) {
            throw new MissingFieldsException("Check for blank fields on the form");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account userAccount = accountService.getAccountByUsername(authentication.getName());

        if (userAccount == null || !userAccount.getId().equals(userId)) {
            throw new AccountOperationNotPermitedException("Operation not allowed");
        }

        int rowsAffected = accountService.updateAccount(userId, accountUpdateRequestDto);

        if (rowsAffected == 0) {
            throw new GeneralException("Failed to update account");
        }
        return ResponseEntity.ok().body(
                new GeneralResponseDTO(200, "Operation succeeded", ZonedDateTime.now(ZoneId.of("Z")))
        );
    }
}
