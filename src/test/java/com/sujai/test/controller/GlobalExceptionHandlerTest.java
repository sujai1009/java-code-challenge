package com.sujai.test.controller;

import com.sujai.test.dto.DeviceDto;
import com.sujai.test.model.DeviceState;
import com.sujai.test.service.DeviceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceService deviceService;

    @Test
    void shouldReturn404WhenDeviceNotFound() throws Exception {
        when(deviceService.getDeviceById(999L))
                .thenThrow(new DeviceService.DeviceNotFoundException("Device not found with id: 999"));

        mockMvc.perform(get("/api/devices/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Device not found with id: 999"));
    }

    @Test
    void shouldReturn409WhenUpdatingInUseDeviceName() throws Exception {
        DeviceDto inUseDevice = new DeviceDto();
        inUseDevice.setId(2L);
        inUseDevice.setName("Galaxy S24");
        inUseDevice.setBrand("Samsung");
        inUseDevice.setState(DeviceState.IN_USE);

        when(deviceService.getDeviceById(2L)).thenReturn(inUseDevice);
        doThrow(new DeviceService.DeviceUpdateException("Cannot update name of a device that is in use"))
                .when(deviceService).updateDevice(eq(2L), any(com.sujai.test.model.Device.class));

        mockMvc.perform(patch("/api/devices/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Name\",\"state\":\"IN_USE\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Cannot update name of a device that is in use"));
    }

    @Test
    void shouldReturn409WhenDeletingInUseDevice() throws Exception {
        DeviceDto inUseDevice = new DeviceDto();
        inUseDevice.setId(2L);
        inUseDevice.setName("Galaxy S24");
        inUseDevice.setBrand("Samsung");
        inUseDevice.setState(DeviceState.IN_USE);

        when(deviceService.getDeviceById(2L)).thenReturn(inUseDevice);
        doThrow(new DeviceService.DeviceDeleteException("Cannot delete a device that is in use"))
                .when(deviceService).deleteDevice(2L);

        mockMvc.perform(delete("/api/devices/2"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Cannot delete a device that is in use"));
    }

    @Test
    void shouldReturn400WhenRequestBodyValidationFails() throws Exception {
        mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"Apple\",\"state\":\"AVAILABLE\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("name: Name is required"));
    }
}
