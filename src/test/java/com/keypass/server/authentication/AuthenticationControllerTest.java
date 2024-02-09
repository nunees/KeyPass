package com.keypass.server.authentication;

import com.keypass.server.account.AccountRequestDTO;
import com.keypass.server.common.utils.JsonConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Authentication Controller integration Test")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should be able to authenticate a user")
    void shouldBeAbleToAuthenticateAUser() throws Exception {

        AccountRequestDTO accountRequestDTO = AccountRequestDTO.builder()
                .firstName("John")
                .lastName("Joe")
                .username("teste")
                .email("teste@teste.com")
                .password("teste")
                .build();

        this.mockMvc
                .perform(
                        post("/accounts/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonConverter.convertToJson(accountRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(accountRequestDTO.username(), accountRequestDTO.password());

        MvcResult mvcResult = this.mockMvc.perform(
                        post("/sessions/new")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonConverter.convertToJson(authenticationDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").exists())
                .andReturn();


    }
}