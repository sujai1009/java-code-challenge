package com.sujai.test.dto;

import com.sujai.test.model.Device;
import com.sujai.test.model.DeviceState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DeviceDto {

    @Schema(description = "Unique identifier of the device", example = "1")
    private Long id;

    @Schema(description = "Name of the device", example = "iPhone 15")
    private String name;

    @Schema(description = "Brand of the device", example = "Apple")
    private String brand;

    @Schema(description = "Current state of the device", example = "AVAILABLE")
    private DeviceState state;

    @Schema(description = "Timestamp when the device was created", example = "2024-01-15T10:30:00")
    private LocalDateTime creationTime;

    @Schema(description = "Timestamp when the device was last updated", example = "2024-01-15T10:30:00")
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
