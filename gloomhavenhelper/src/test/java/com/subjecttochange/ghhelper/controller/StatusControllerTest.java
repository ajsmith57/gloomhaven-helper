package com.subjecttochange.ghhelper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subjecttochange.ghhelper.Application;
import com.subjecttochange.ghhelper.persistence.model.orm.Stat;
import com.subjecttochange.ghhelper.persistence.model.orm.monster.Status;
import com.subjecttochange.ghhelper.persistence.service.StatusService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("ALL")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@ActiveProfiles("test")
public class StatusControllerTest {


    @Autowired
    private MockMvc mvc;
    @Autowired
    private StatusService statusService;
    private Status status;

    @Before
    public void setUp() throws Exception {
        status = status.create("Bleed", "Take damage");
        status = statusService.createStatus(status);
    }

    @After
    public void tearDown() throws Exception {
        statusService.deleteStatus(status.getId());
    }

    @Test
    public void getStatus() throws Exception {
        mvc.perform(
                get("/statuses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id", notNullValue()))
                .andExpect(jsonPath("$.content[0].name", is(status.getName())))
                .andExpect(jsonPath("$.content[0].tooltip", is(status.getTooltip())));
    }

    @Test
    public void createStatus() throws Exception {
        String jsonBody = new ObjectMapper().writeValueAsString(status);

        MvcResult result = mvc.perform(
                post("/statuses")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(status.getName())))
                .andExpect(jsonPath("$.tooltip", is(status.getTooltip())))
                .andReturn();

        String resultString = result.getResponse().getContentAsString();
        Status createdStaus = new ObjectMapper().readValue(resultString, Status.class);
        statusService.deleteStatus(createdStaus.getId());
    }

    @Test
    public void updateStatus() throws Exception {
        Status request = Status.create("Stunned", "Cannot move");
        String jsonBody = new ObjectMapper().writeValueAsString(request);

        mvc.perform(
                put("/statuses")
                        .param("id", status.getId().toString())
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(request.getName())))
                .andExpect(jsonPath("$.tooltip", is(request.getTooltip())));
    }

    @Test
    public void deleteStatus() throws Exception {
        mvc.perform(
                delete("/statuses")
                        .param("id", status.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        status = statusService.createStatus(status);
    }
}