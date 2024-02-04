package com.keypass.server.account;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keypass.server.utils.JsonConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Account Controller Integration Test")
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    private Account mockedAccount;

    private AccountRequestDto accountRequestDto;

    // TODO: Need to find a way to delete the current user every time a test runs
    @BeforeEach
    public void setUp() {
        mockedAccount = Account.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe123")
                .email("johndoe@test.com")
                .password("123123")
                .build();

        accountRequestDto = AccountRequestDto.builder()
                .firstName(mockedAccount.getFirstName())
                .lastName(mockedAccount.getLastName())
                .username(mockedAccount.getUsername())
                .email(mockedAccount.getEmail())
                .password(mockedAccount.getPassword())
                .build();

        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("Test controller that successfully creates a new account [POST]")
    void testControllerThatSuccessfullyCreatesNewAccount() throws Exception {
        this.mockMvc
                .perform(
                        post("/accounts/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonConverter.convertToJson(accountRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").exists());
    }

    @Test
    @DisplayName("Test controller that fails to create a new account with an existing username or email [POST]")
    void testControllerThatFailsToCreateNewAccountWithExistingUsername() throws Exception {
        this.mockMvc
                .perform(
                        post("/accounts/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonConverter.convertToJson(accountRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").exists());


        this.mockMvc
                .perform(
                        post("/accounts/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonConverter.convertToJson(accountRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test controller that get an account by Id [GET]")
    public void testControllerThatGetAnAccountById() throws Exception {
        MvcResult insertedData = this.mockMvc
                .perform(
                        post("/accounts/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonConverter.convertToJson(accountRequestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = insertedData.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        String userCreatedUUID = jsonNode.path("id").asText();

        this.mockMvc
                .perform(
                        get("/accounts/" + userCreatedUUID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonConverter.convertToJson(accountRequestDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userCreatedUUID));

    }

    @Test
    @DisplayName("Test controller that failed to get an account By Id [GET]")
    public void testControllerThatFailedToGetAnAccountById() throws Exception{
        this.mockMvc
                .perform(
                        post("/accounts/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonConverter.convertToJson(accountRequestDto)))
                .andExpect(status().isCreated());

        this.mockMvc
                .perform(
                        get("/accounts/" + UUID.randomUUID())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonConverter.convertToJson(accountRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test controller that update a user by id [UPDATE]")
    public void testControllerThatUpdateAUserById(){

    }


}