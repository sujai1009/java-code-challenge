package com.sujai.test.service;

import com.sujai.test.dto.DeviceDto;
import com.sujai.test.model.Device;
import com.sujai.test.model.DeviceState;
import com.sujai.test.repository.DeviceRepository;
import com.sujai.test.validation.DeviceValidator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private DeviceValidator deviceValidator;

    @InjectMocks
    private DeviceService deviceService;

    @Test
    void shouldCreateDevice() {
        Device device = new Device("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device.setId(1L);

        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        DeviceDto created = deviceService.createDevice(device);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getName()).isEqualTo("iPhone 15");
        assertThat(created.getBrand()).isEqualTo("Apple");
        assertThat(created.getState()).isEqualTo(DeviceState.AVAILABLE);
    }

    @Test
    void shouldGetDeviceById() {
        Device device = new Device("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device.setId(1L);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        DeviceDto found = deviceService.getDeviceById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("iPhone 15");
    }

    @Test
    void shouldThrowWhenDeviceNotFound() {
        when(deviceRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.getDeviceById(999L))
                .isInstanceOf(DeviceService.DeviceNotFoundException.class)
                .hasMessageContaining("Device not found with id: 999");
    }

    @Test
    void shouldGetAllDevices() {
        Device device1 = new Device("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device1.setId(1L);
        Device device2 = new Device("Galaxy S24", "Samsung", DeviceState.IN_USE);
        device2.setId(2L);

        Page<Device> page = new PageImpl<>(List.of(device1, device2), PageRequest.of(0, 20), 2);
        when(deviceRepository.findAll(PageRequest.of(0, 20))).thenReturn(page);

        Page<DeviceDto> result = deviceService.getAllDevices(PageRequest.of(0, 20));

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getName()).isEqualTo("iPhone 15");
        assertThat(result.getContent().get(1).getName()).isEqualTo("Galaxy S24");
    }

    @Test
    void shouldGetAllDevicesWithPagination() {
        Device device1 = new Device("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device1.setId(1L);
        Device device2 = new Device("Galaxy S24", "Samsung", DeviceState.IN_USE);
        device2.setId(2L);

        Page<Device> page = new PageImpl<>(List.of(device1, device2), PageRequest.of(0, 20), 2);
        when(deviceRepository.findAll(PageRequest.of(0, 20))).thenReturn(page);

        Page<DeviceDto> result = deviceService.getAllDevices(PageRequest.of(0, 20));

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    void shouldGetDevicesByBrand() {
        Device device = new Device("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device.setId(1L);

        Page<Device> page = new PageImpl<>(List.of(device), PageRequest.of(0, 20), 1);
        when(deviceRepository.findByBrand(eq("Apple"), any(Pageable.class))).thenReturn(page);

        Page<DeviceDto> result = deviceService.getDevicesByBrand("Apple", PageRequest.of(0, 20));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getBrand()).isEqualTo("Apple");
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    void shouldGetDevicesByState() {
        Device device = new Device("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device.setId(1L);

        Page<Device> page = new PageImpl<>(List.of(device), PageRequest.of(0, 20), 1);
        when(deviceRepository.findByState(eq(DeviceState.AVAILABLE), any(Pageable.class))).thenReturn(page);

        Page<DeviceDto> result = deviceService.getDevicesByState(DeviceState.AVAILABLE, PageRequest.of(0, 20));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getState()).isEqualTo(DeviceState.AVAILABLE);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    void shouldUpdateDevice() {
        Device existingDevice = new Device("iPhone 15", "Apple", DeviceState.AVAILABLE);
        existingDevice.setId(1L);

        Device updatedDetails = new Device("iPhone 15 Pro", "Apple", DeviceState.IN_USE);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(existingDevice));
        when(deviceRepository.save(any(Device.class))).thenReturn(existingDevice);

        DeviceDto updated = deviceService.updateDevice(1L, updatedDetails);

        assertThat(updated.getName()).isEqualTo("iPhone 15 Pro");
        assertThat(updated.getState()).isEqualTo(DeviceState.IN_USE);
    }

    @Test
    void shouldNotUpdateNameOrBrandWhenInUse() {
        Device inUseDevice = new Device("iPhone 15", "Apple", DeviceState.IN_USE);
        inUseDevice.setId(1L);

        Device updateDetails = new Device("iPhone 15 Pro", "Samsung", DeviceState.IN_USE);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(inUseDevice));
        doThrow(new DeviceService.DeviceUpdateException("Cannot update name of a device that is in use"))
                .when(deviceValidator).validateUpdate(inUseDevice, updateDetails);

        assertThatThrownBy(() -> deviceService.updateDevice(1L, updateDetails))
                .isInstanceOf(DeviceService.DeviceUpdateException.class)
                .hasMessageContaining("Cannot update name of a device that is in use");
    }

    @Test
    void shouldDeleteDevice() {
        Device device = new Device("iPhone 15", "Apple", DeviceState.AVAILABLE);
        device.setId(1L);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        doNothing().when(deviceRepository).delete(device);

        deviceService.deleteDevice(1L);
    }

    @Test
    void shouldNotDeleteInUseDevice() {
        Device inUseDevice = new Device("iPhone 15", "Apple", DeviceState.IN_USE);
        inUseDevice.setId(1L);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(inUseDevice));
        doThrow(new DeviceService.DeviceDeleteException("Cannot delete a device that is in use"))
                .when(deviceValidator).validateDelete(inUseDevice);

        assertThatThrownBy(() -> deviceService.deleteDevice(1L))
                .isInstanceOf(DeviceService.DeviceDeleteException.class)
                .hasMessageContaining("Cannot delete a device that is in use");
    }

    @Test
    void shouldGetAllDistinctBrands() {
        when(deviceRepository.findDistinctBrands()).thenReturn(List.of("Apple", "Samsung", "Google"));

        List<String> brands = deviceService.getAllDistinctBrands();

        assertThat(brands).hasSize(3);
        assertThat(brands).containsExactly("Apple", "Samsung", "Google");
        verify(deviceRepository).findDistinctBrands();
    }
}
