package com.keypass.server.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface AccountController {
    @Operation(summary = "Create account controller", description = "Create a new user account", tags = {"account", "create"}, responses = {@ApiResponse(responseCode = "201", description = "Account created", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AccountResponseDTO.class))}), @ApiResponse(responseCode = "401", description = "User Already Exists", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.keypass.server.common.exception.GeneralResponseDTO.class))}), @ApiResponse(responseCode = "400", description = "Blank fields on form", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.keypass.server.common.exception.GeneralResponseDTO.class))}), @ApiResponse(responseCode = "500", description = "Bad Request", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.keypass.server.common.exception.GeneralResponseDTO.class))})})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/register")
    public ResponseEntity<AccountResponseDTO> create(@RequestBody AccountRequestDTO accountRequestDto);

    @Operation(summary = "Get account by Id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Account found"), @ApiResponse(responseCode = "400", description = "Account not found"), @ApiResponse(responseCode = "500", description = "Bad Request")})
    @SecurityRequirement(name = "bearer-key")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAccountById(@PathVariable("id") String userId);

    @Operation(summary = "Update account controller", description = "Update an existing user account", tags = {"account", "update"}, responses = {@ApiResponse(responseCode = "200", description = "Updated", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AccountResponseDTO.class))}), @ApiResponse(responseCode = "400", description = "Blank fields on form", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.keypass.server.common.exception.GeneralResponseDTO.class))}), @ApiResponse(responseCode = "400", description = "User not allowed", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.keypass.server.common.exception.GeneralResponseDTO.class))}), @ApiResponse(responseCode = "500", description = "Bad Request", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = com.keypass.server.common.exception.GeneralResponseDTO.class))})})
    @SecurityRequirement(name = "bearer-key")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUserAccount(@PathVariable("id") UUID userId, @RequestBody AccountUpdateDTO accountUpdateDTO);

}

