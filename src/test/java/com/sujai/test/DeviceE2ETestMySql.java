package com.sujai.test;

import com.sujai.test.dto.DeviceDto;
import com.sujai.test.model.DeviceState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class DeviceE2ETestMySql {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("devices_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQLDialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndRetrieveDevice() throws Exception {
        String deviceJson = objectMapper.writeValueAsString(
                new DeviceRequest("iPhone 15", "Apple", DeviceState.AVAILABLE));

        mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.name", is("iPhone 15")))
                .andExpect(jsonPath("$.data.brand", is("Apple")))
                .andExpect(jsonPath("$.data.state", is("AVAILABLE")));
    }

    @Test
    void shouldGetAllDevices() throws Exception {
        mockMvc.perform(get("/api/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.totalElements").isNumber())
                .andExpect(jsonPath("$.data.totalPages").isNumber());
    }

    @Test
    void shouldGetDevicesByBrand() throws Exception {
        mockMvc.perform(get("/api/devices/brand/Apple"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetDevicesByState() throws Exception {
        mockMvc.perform(get("/api/devices/state/AVAILABLE"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllDistinctBrands() throws Exception {
        mockMvc.perform(get("/api/devices/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void shouldUpdateDevice() throws Exception {
        String deviceJson = objectMapper.writeValueAsString(
                new DeviceRequest("iPhone 15", "Apple", DeviceState.AVAILABLE));

        String response = mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DeviceDto device = objectMapper.readValue(response, DeviceDto.class);

        String updateJson = objectMapper.writeValueAsString(
                new DeviceRequest("iPhone 15 Pro", "Apple", DeviceState.IN_USE));

        mockMvc.perform(patch("/api/devices/" + device.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name", is("iPhone 15 Pro")))
                .andExpect(jsonPath("$.data.state", is("IN_USE")));
    }

    @Test
    void shouldDeleteDevice() throws Exception {
        String deviceJson = objectMapper.writeValueAsString(
                new DeviceRequest("iPhone 15", "Apple", DeviceState.AVAILABLE));

        String response = mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DeviceDto device = objectMapper.readValue(response, DeviceDto.class);

        mockMvc.perform(delete("/api/devices/" + device.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDeleteInUseDevice() throws Exception {
        String deviceJson = objectMapper.writeValueAsString(
                new DeviceRequest("iPhone 15", "Apple", DeviceState.IN_USE));

        String response = mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DeviceDto device = objectMapper.readValue(response, DeviceDto.class);

        mockMvc.perform(delete("/api/devices/" + device.getId()))
                .andExpect(status().isConflict());
    }

    private record DeviceRequest(String name, String brand, DeviceState state) {
    }
}
