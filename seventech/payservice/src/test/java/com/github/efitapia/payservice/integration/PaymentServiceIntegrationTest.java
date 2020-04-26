package com.github.efitapia.payservice.integration;

import com.github.efitapia.payservice.repo.Account;
import com.github.efitapia.payservice.repo.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class PaymentServiceIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AccountRepository repo;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Transactional
    @Sql(statements = "insert into accounts values (1, 100.0), (2, 50.0)")
    void transferSuccessfully() throws Exception {
        mockMvc.perform(post("/transfer")
            .content("{\n"
                + "  \"senderAccountId\": 1,\n"
                + "  \"recipientAccountId\": 2,\n"
                + "  \"amount\": 20.55\n"
                + "}"
            )
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        Account sender = repo.get(1L);
        Account recipient = repo.get(2L);

        assertThat(sender).isEqualTo(
            new Account()
                .setId(1L)
                .setBalance(BigDecimal.valueOf(79.45))
        );

        assertThat(recipient).isEqualTo(
            new Account()
                .setId(2L)
                .setBalance(BigDecimal.valueOf(70.55))
        );
    }

    @Test
    @Transactional
    @Sql(statements = "insert into accounts values (1, -100.0), (2, 50.0)")
    void transfer_returnErrorWhenBalanceIsNegative() throws Exception {
        mockMvc.perform(post("/transfer")
            .content("{\n"
                + "  \"senderAccountId\": 1,\n"
                + "  \"recipientAccountId\": 2,\n"
                + "  \"amount\": 20.55\n"
                + "}"
            )
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json("{\n"
                + "  \"code\": \"2\",\n"
                + "  \"message\": \"Not enough money. Balance: -100.00\"\n"
                + "}"));
    }

    @Test
    void transfer_returnErrorWhenAccountNotFound() throws Exception {
        mockMvc.perform(post("/transfer")
            .content("{\n"
                + "  \"senderAccountId\": 1,\n"
                + "  \"recipientAccountId\": 2,\n"
                + "  \"amount\": 20.55\n"
                + "}"
            )
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json("{\n"
                + "  \"code\": \"1\",\n"
                + "  \"message\": \"No such account 1\"\n"
                + "}"));
    }

    @Test
    void topUp_returnErrorWhenAccountNotFound() throws Exception {
        mockMvc.perform(post("/topUp")
            .content("{\n"
                + "  \"accountId\": 1,\n"
                + "  \"amount\": 20.00\n"
                + "}"
            )
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json("{\n"
                + "  \"code\": \"1\",\n"
                + "  \"message\": \"No such account 1\"\n"
                + "}"));
    }


    @Test
    @Transactional
    @Sql(statements = "insert into accounts values (1, 100.0), (2, 50.0)")
    void topUpSuccessfully() throws Exception {
        mockMvc.perform(post("/topUp")
            .content("{\n"
                + "  \"accountId\": 1,\n"
                + "  \"amount\": 20.00\n"
                + "}"
            )
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        Account account = repo.get(1L);
        assertThat(account).isEqualTo(
            new Account()
                .setId(1L)
                .setBalance(new BigDecimal("120.00"))
        );

    }


    @Test
    @Transactional
    @Sql(statements = "insert into accounts values (1, 100.0), (2, 50.0)")
    void withdrawSuccessfully() throws Exception {
        mockMvc.perform(post("/withdraw")
            .content("{\n"
                + "  \"accountId\": 1,\n"
                + "  \"amount\": 20.00\n"
                + "}"
            )
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        Account account = repo.get(1L);
        assertThat(account).isEqualTo(
            new Account()
                .setId(1L)
                .setBalance(new BigDecimal("80.00"))
        );

    }


    @Test
    void withdraw_returnErrorWhenAccountNotFound() throws Exception {
        mockMvc.perform(post("/withdraw")
            .content("{\n"
                + "  \"accountId\": 1,\n"
                + "  \"amount\": 20.00\n"
                + "}"
            )
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json("{\n"
                + "  \"code\": \"1\",\n"
                + "  \"message\": \"No such account 1\"\n"
                + "}"));
    }


    @Test
    @Transactional
    @Sql(statements = "insert into accounts(id, balance) values (1, -100.0), (2, 50.0)")
    void withdraw_returnErrorWhenBalanceIsNegative() throws Exception {
        mockMvc.perform(post("/withdraw")
            .content("{\n"
                + "  \"accountId\": 1,\n"
                + "  \"amount\": 20.00\n"
                + "}"
            )
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json("{\n"
                + "  \"code\": \"2\",\n"
                + "  \"message\": \"Not enough money. Balance: -100.00\"\n"
                + "}"));
    }

}
