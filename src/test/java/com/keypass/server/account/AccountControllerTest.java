package com.keypass.server.account;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keypass.server.common.utils.JsonConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private AccountRequestDTO accountRequestDto;

    private Account mockedAccount;

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

        accountRequestDto = AccountRequestDTO.builder()
                .firstName(mockedAccount.getFirstName())
                .lastName(mockedAccount.getLastName())
                .username(mockedAccount.getUsername())
                .email(mockedAccount.getEmail())
                .password(mockedAccount.getPassword())
                .build();

        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("[POST] Should be able to create a new account through controller")
    void shouldBeAbleToCreateANewAccountThroughController() throws Exception {
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
    @DisplayName("[POST] Should failed if try to create an account with username or password already in use")
    void shouldFailedIfTryToCreateAnAccountWithUsernameOrPasswordAlreadyInUse() throws Exception {
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
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[GET] Should get an account by id")
    public void shouldGetAnAccountById() throws Exception {
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
    @DisplayName("[GET] Should fail get an account with non-existent account")
    public void shouldFailGetAnAccountWithNonExistentAccount() throws Exception {
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
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[PUT] Should update an account")
    public void shouldUpdateAnAccount() throws Exception{
        MvcResult insertedData = this.mockMvc
                .perform(
                        post("/accounts/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonConverter.convertToJson(accountRequestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        mockedAccount.setUsername("otherusername@gmail.com");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(insertedData.getResponse().getContentAsString());
        String userCreatedUUID = jsonNode.path("id").asText();

        this.mockMvc
                .perform(
                        put("/accounts/" + userCreatedUUID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonConverter.convertToJson(accountRequestDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Operation succeeded"));

    }

    @Test
    @DisplayName("[PUT] Should not update an account with different id")
    public void shouldNotUpdateAnAccountWithDifferentId() throws Exception{
        MvcResult insertedData = this.mockMvc
                .perform(
                        post("/accounts/register")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonConverter.convertToJson(accountRequestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        mockedAccount.setUsername("otherusername@gmail.com");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(insertedData.getResponse().getContentAsString());
        String userCreatedUUID = jsonNode.path("id").asText();

        this.mockMvc
                .perform(
                        put("/accounts/" + UUID.randomUUID())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonConverter.convertToJson(accountRequestDto)))
                .andExpect(status().isUnauthorized());

    }


}