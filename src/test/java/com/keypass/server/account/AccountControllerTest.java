package com.keypass.server.account;

import com.keypass.server.utils.JsonConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Account Controller Integration Test")
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Account mockedAccount;

    @BeforeEach
    public void setUp(){
        mockedAccount =  Account.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe123")
                .email("johndoe@test.com")
                .password("123123")
                .build();
    }
    @Test
    @DisplayName("Test controller that successfully creates a new account [POST]")
    void testControllerThatSuccessfullyCreatesNewAccount() throws Exception {
        String jsonRequestBody = JsonConverter.convertToJson(mockedAccount);

        this.mockMvc
                .perform(
                        post("/accounts/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequestBody))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
    }
}