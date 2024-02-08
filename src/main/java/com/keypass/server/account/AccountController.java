package com.keypass.server.account;

import com.keypass.server.account.dto.AccountRequestDto;
import com.keypass.server.account.dto.AccountResponseDto;
import com.keypass.server.account.dto.AccountExceptionResponseDTO;
import com.keypass.server.account.exception.AccountAlreadyExistException;
import com.keypass.server.exception.GeneralException;
import com.keypass.server.exception.MissingFieldsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/accounts")
@Tag(name = "Accounts", description = "Create and manage user accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Create account controller",
    description = "Create a new user account",
            tags = {"account", "create"},
            responses ={
                    @ApiResponse(responseCode = "201", description = "Account created", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema =
                            @Schema(implementation = AccountResponseDto.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "User Already Exists", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema =
                            @Schema(implementation = AccountExceptionResponseDTO.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Blank fields on form", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema =
                            @Schema(implementation = MissingFieldsException.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Bad Request", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema =
                            @Schema(implementation = GeneralException.class))
                    })
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/register")
    public ResponseEntity<AccountResponseDto> create(@RequestBody AccountRequestDto accountRequestDto) throws GeneralException {

               if(accountRequestDto.firstName() == null
                       || accountRequestDto.lastName() == null
                       || accountRequestDto.username() == null
                       || accountRequestDto.email() == null
                       || accountRequestDto.password() == null
               ){
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
    public ResponseEntity<Object> getAccountById(@PathVariable("id") String id) {

            Account account = accountService.getAccountById(id).orElseThrow(() -> new AccountAlreadyExistException("Account not found"));
            return ResponseEntity.ok(account);
    }


    @Operation(summary = "Delete account by Id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Delete an account"),
            @ApiResponse(responseCode = "400", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Bad Request")

    })
    @SecurityRequirement(name = "bearer-key")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteAccountById(@PathVariable("id") String id) {

            Account account = accountService.getAccountById(id).orElseThrow(() -> new AccountAlreadyExistException("Account not found"));

            accountService.deleteAccountById(account.getId().toString());

            return ResponseEntity.ok(account);
    }

}
