package com.sujai.test.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Possible states of a device")
public enum DeviceState {

    @Schema(description = "Device is available for use", example = "AVAILABLE")
    AVAILABLE,

    @Schema(description = "Device is currently in use", example = "IN_USE")
    IN_USE,

    @Schema(description = "Device is inactive", example = "INACTIVE")
    INACTIVE
}
