package com.sujai.test.dto;

import com.sujai.test.model.Device;
import com.sujai.test.model.DeviceState;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DeviceDto {

    private Long id;
    private String name;
    private String brand;
    private DeviceState state;
    private LocalDateTime creationTime;
    private LocalDateTime updatedAt;

    public static DeviceDto from(Device device) {
        DeviceDto dto = new DeviceDto();
        dto.setId(device.getId());
        dto.setName(device.getName());
        dto.setBrand(device.getBrand());
        dto.setState(device.getState());
        dto.setCreationTime(device.getCreationTime());
        dto.setUpdatedAt(device.getUpdatedAt());
        return dto;
    }

    public Device toEntity() {
        return new Device(name, brand, state);
    }
}
