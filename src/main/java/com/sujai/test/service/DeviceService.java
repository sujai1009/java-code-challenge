package com.sujai.test.service;

import com.sujai.test.dto.DeviceDto;
import com.sujai.test.model.Device;
import com.sujai.test.model.DeviceState;
import com.sujai.test.repository.DeviceRepository;
import com.sujai.test.validation.DeviceValidator;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceValidator deviceValidator;

    public DeviceService(DeviceRepository deviceRepository, DeviceValidator deviceValidator) {
        this.deviceRepository = deviceRepository;
        this.deviceValidator = deviceValidator;
    }

    public DeviceDto createDevice(Device device) {
        Device saved = deviceRepository.save(device);
        return DeviceDto.from(saved);
    }

    public Page<DeviceDto> getAllDevices(Pageable pageable) {
        return deviceRepository.findAll(pageable).map(DeviceDto::from);
    }

    public List<DeviceDto> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(DeviceDto::from)
                .collect(Collectors.toList());
    }

    public DeviceDto getDeviceById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));
        return DeviceDto.from(device);
    }

    public Page<DeviceDto> getDevicesByBrand(String brand, Pageable pageable) {
        return deviceRepository.findByBrand(brand, pageable).map(DeviceDto::from);
    }

    public Page<DeviceDto> getDevicesByState(DeviceState state, Pageable pageable) {
        return deviceRepository.findByState(state, pageable).map(DeviceDto::from);
    }

    public List<String> getAllDistinctBrands() {
        return deviceRepository.findDistinctBrands();
    }

//    public List<Device> searchDevices(String brand, DeviceState state) {
//        if (brand != null && state != null) {
//            return deviceRepository.findByBrandAndState(brand, state);
//        } else if (brand != null) {
//            return deviceRepository.findByBrand(brand);
//        } else if (state != null) {
//            return deviceRepository.findByState(state);
//        }
//        return Collections.emptyList();
//    }

    public DeviceDto updateDevice(Long id, Device deviceDetails) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));

        deviceValidator.validateUpdate(device, deviceDetails);

        if (StringUtils.hasText(deviceDetails.getName())) {
            device.setName(deviceDetails.getName());
        }

        if (StringUtils.hasText(deviceDetails.getBrand())) {
            device.setBrand(deviceDetails.getBrand());
        }

        if (deviceDetails.getState() != null) {
            device.setState(deviceDetails.getState());
        }

        Device saved = deviceRepository.save(device);
        return DeviceDto.from(saved);
    }

    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));
        deviceValidator.validateDelete(device);
        deviceRepository.delete(device);
    }

    public static class DeviceNotFoundException extends RuntimeException {
        public DeviceNotFoundException(String message) {
            super(message);
        }
    }

    public static class DeviceUpdateException extends RuntimeException {
        public DeviceUpdateException(String message) {
            super(message);
        }
    }

    public static class DeviceDeleteException extends RuntimeException {
        public DeviceDeleteException(String message) {
            super(message);
        }
    }
}
