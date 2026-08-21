package com.sujai.test.controller;

import com.sujai.test.dto.DeviceDto;
import com.sujai.test.model.DeviceState;
import com.sujai.test.service.DeviceService;
import java.util.List;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceService deviceService;

    @Test
    void shouldCreateDevice() throws Exception {
        DeviceDto device = new DeviceDto();
        device.setId(1L);
        device.setName("iPhone 15");
        device.setBrand("Apple");
        device.setState(DeviceState.AVAILABLE);

        when(deviceService.createDevice(any(com.sujai.test.model.Device.class))).thenReturn(device);

        mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"iPhone 15\",\"brand\":\"Apple\",\"state\":\"AVAILABLE\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("iPhone 15"))
                .andExpect(jsonPath("$.data.brand").value("Apple"))
                .andExpect(jsonPath("$.data.state").value("AVAILABLE"))
                .andExpect(jsonPath("$.message").value("Device created successfully"));
    }

    @Test
    void shouldGetDeviceById() throws Exception {
        DeviceDto device = new DeviceDto();
        device.setId(1L);
        device.setName("iPhone 15");
        device.setBrand("Apple");
        device.setState(DeviceState.AVAILABLE);

        when(deviceService.getDeviceById(1L)).thenReturn(device);

        mockMvc.perform(get("/api/devices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("iPhone 15"));
    }

    @Test
    void shouldGetAllDevices() throws Exception {
        DeviceDto device1 = new DeviceDto();
        device1.setId(1L);
        device1.setName("iPhone 15");
        device1.setBrand("Apple");
        device1.setState(DeviceState.AVAILABLE);
        DeviceDto device2 = new DeviceDto();
        device2.setId(2L);
        device2.setName("Galaxy S24");
        device2.setBrand("Samsung");
        device2.setState(DeviceState.IN_USE);

        org.springframework.data.domain.Page<DeviceDto> page = new org.springframework.data.domain.PageImpl<>(List.of(device1, device2));
        when(deviceService.getAllDevices(any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].name").value("iPhone 15"))
                .andExpect(jsonPath("$.data.content[1].name").value("Galaxy S24"))
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.totalPages").value(1));
    }

    @Test
    void shouldGetDevicesByBrand() throws Exception {
        DeviceDto device = new DeviceDto();
        device.setId(1L);
        device.setName("iPhone 15");
        device.setBrand("Apple");
        device.setState(DeviceState.AVAILABLE);

        org.springframework.data.domain.Page<DeviceDto> page = new org.springframework.data.domain.PageImpl<>(List.of(device));
        when(deviceService.getDevicesByBrand(eq("Apple"), any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/devices/brand/Apple"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].brand").value("Apple"))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1));
    }

    @Test
    void shouldGetDevicesByState() throws Exception {
        DeviceDto device = new DeviceDto();
        device.setId(1L);
        device.setName("iPhone 15");
        device.setBrand("Apple");
        device.setState(DeviceState.AVAILABLE);

        org.springframework.data.domain.Page<DeviceDto> page = new org.springframework.data.domain.PageImpl<>(List.of(device));
        when(deviceService.getDevicesByState(eq(DeviceState.AVAILABLE), any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/devices/state/AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].state").value("AVAILABLE"))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1));
    }

//    @Test
//    void shouldSearchDevicesByBrand() throws Exception {
//        Device device = new Device("iPhone 15", "Apple", DeviceState.AVAILABLE);
//        device.setId(1L);
//
//        when(deviceService.searchDevices("Apple", null)).thenReturn(List.of(device));
//
//        mockMvc.perform(get("/api/devices/search").param("brand", "Apple"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data[0].brand").value("Apple"));
//    }
//
//    @Test
//    void shouldSearchDevicesByState() throws Exception {
//        Device device = new Device("iPhone 15", "Apple", DeviceState.AVAILABLE);
//        device.setId(1L);
//
//        when(deviceService.searchDevices(null, DeviceState.AVAILABLE)).thenReturn(List.of(device));
//
//        mockMvc.perform(get("/api/devices/search").param("state", "AVAILABLE"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data[0].state").value("AVAILABLE"));
//    }
//
//    @Test
//    void shouldSearchDevicesByBrandAndState() throws Exception {
//        Device device = new Device("iPhone 15", "Apple", DeviceState.AVAILABLE);
//        device.setId(1L);
//
//        when(deviceService.searchDevices("Apple", DeviceState.AVAILABLE)).thenReturn(List.of(device));
//
//        mockMvc.perform(get("/api/devices/search").param("brand", "Apple").param("state", "AVAILABLE"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data[0].brand").value("Apple"))
//                .andExpect(jsonPath("$.data[0].state").value("AVAILABLE"));
//    }

    @Test
    void shouldGetAllDistinctBrands() throws Exception {
        when(deviceService.getAllDistinctBrands()).thenReturn(List.of("Apple", "Samsung", "Google"));

        mockMvc.perform(get("/api/devices/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0]").value("Apple"))
                .andExpect(jsonPath("$.data[1]").value("Samsung"))
                .andExpect(jsonPath("$.data[2]").value("Google"));
    }

    @Test
    void shouldUpdateDevice() throws Exception {
        DeviceDto updatedDevice = new DeviceDto();
        updatedDevice.setId(1L);
        updatedDevice.setName("iPhone 15 Pro");
        updatedDevice.setBrand("Apple");
        updatedDevice.setState(DeviceState.IN_USE);

        when(deviceService.updateDevice(eq(1L), any(com.sujai.test.model.Device.class))).thenReturn(updatedDevice);

        mockMvc.perform(patch("/api/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"iPhone 15 Pro\",\"brand\":\"Apple\",\"state\":\"IN_USE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("iPhone 15 Pro"))
                .andExpect(jsonPath("$.data.state").value("IN_USE"));
    }

    @Test
    void shouldDeleteDevice() throws Exception {
        doNothing().when(deviceService).deleteDevice(1L);

        mockMvc.perform(delete("/api/devices/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeviceDoesNotExist() throws Exception {
        when(deviceService.getDeviceById(999L))
                .thenThrow(new DeviceService.DeviceNotFoundException("Device not found with id: 999"));

        mockMvc.perform(get("/api/devices/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Device not found with id: 999"));
    }

    @Test
    void shouldReturnConflictWhenUpdatingNameOfInUseDevice() throws Exception {
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
    void shouldReturnConflictWhenUpdatingBrandOfInUseDevice() throws Exception {
        DeviceDto inUseDevice = new DeviceDto();
        inUseDevice.setId(2L);
        inUseDevice.setName("Galaxy S24");
        inUseDevice.setBrand("Samsung");
        inUseDevice.setState(DeviceState.IN_USE);

        when(deviceService.getDeviceById(2L)).thenReturn(inUseDevice);
        doThrow(new DeviceService.DeviceUpdateException("Cannot update brand of a device that is in use"))
                .when(deviceService).updateDevice(eq(2L), any(com.sujai.test.model.Device.class));

        mockMvc.perform(patch("/api/devices/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"NewBrand\",\"state\":\"IN_USE\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Cannot update brand of a device that is in use"));
    }

    @Test
    void shouldReturnConflictWhenDeletingInUseDevice() throws Exception {
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
}
