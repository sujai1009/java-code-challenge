package com.sujai.test.controller;

import com.sujai.test.dto.ApiResponseWrapper;
import com.sujai.test.dto.DeviceDto;
import com.sujai.test.model.Device;
import com.sujai.test.model.DeviceState;
import com.sujai.test.dto.PagedResponse;
import com.sujai.test.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/devices")
@Validated
@Tag(name = "Devices", description = "Device management API")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new device", description = "Creates a new device with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ApiResponseWrapper<DeviceDto> createDevice(
            @Parameter(description = "Device creation request", required = true) @Valid @RequestBody DeviceRequest request) {
        Device device = new Device(request.name(), request.brand(), request.state());
        return ApiResponseWrapper.success(deviceService.createDevice(device), "Device created successfully");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get device by ID", description = "Retrieves a device by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Device not found")
    })
    public ResponseEntity<ApiResponseWrapper<DeviceDto>> getDeviceById(
            @Parameter(description = "Device ID", example = "1", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseWrapper.success(deviceService.getDeviceById(id)));
    }

    @GetMapping
    @Operation(summary = "Get all devices", description = "Retrieves all devices with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devices retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponseWrapper<PagedResponse<DeviceDto>> getAllDevices(
            @Parameter(description = "Pagination and sorting parameters") @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ApiResponseWrapper.success(PagedResponse.of(deviceService.getAllDevices(pageable)));
    }

    @GetMapping("/brand/{brand}")
    @Operation(summary = "Get devices by brand", description = "Retrieves all devices matching the specified brand with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devices retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponseWrapper<PagedResponse<DeviceDto>> getDevicesByBrand(
            @Parameter(description = "Brand name", example = "Apple", required = true) @PathVariable @NotBlank String brand,
            @Parameter(description = "Pagination and sorting parameters") @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ApiResponseWrapper.success(PagedResponse.of(deviceService.getDevicesByBrand(brand, pageable)));
    }

    @GetMapping("/state/{state}")
    @Operation(summary = "Get devices by state", description = "Retrieves all devices matching the specified state with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devices retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponseWrapper<PagedResponse<DeviceDto>> getDevicesByState(
            @Parameter(description = "Device state (AVAILABLE, IN_USE, INACTIVE)", example = "AVAILABLE", required = true) @PathVariable DeviceState state,
            @Parameter(description = "Pagination and sorting parameters") @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ApiResponseWrapper.success(PagedResponse.of(deviceService.getDevicesByState(state, pageable)));
    }

    @GetMapping("/brands")
    @Operation(summary = "Get all distinct brands", description = "Retrieves all distinct device brands")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brands retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponseWrapper<List<String>> getAllDistinctBrands() {
        return ApiResponseWrapper.success(deviceService.getAllDistinctBrands());
    }

    // This is an additional feature to have one api for searching wit
//    @GetMapping("/search")
//    @Operation(summary = "Search devices", description = "Search devices by brand and/or state")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Devices retrieved successfully",
//                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
//    })
//    public ApiResponseWrapper<List<Device>> searchDevices(
//            @Parameter(description = "Brand name", example = "Apple", in = ParameterIn.QUERY) @RequestParam(required = false) String brand,
//            @Parameter(description = "Device state (AVAILABLE, IN_USE, INACTIVE)", example = "AVAILABLE", in = ParameterIn.QUERY) @RequestParam(required = false) DeviceState state) {
//        return ApiResponseWrapper.success(deviceService.searchDevices(brand, state));
//    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a device", description = "Fully or partially updates an existing device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - cannot update device in use")
    })
    public ApiResponseWrapper<DeviceDto> updateDevice(
            @Parameter(description = "Device ID", example = "1", required = true) @PathVariable Long id,
            @Parameter(description = "Device update request", required = true) @Valid @RequestBody DeviceUpdateRequest request) {
        Device device = new Device(request.name(), request.brand(), request.state());
        return ApiResponseWrapper.success(deviceService.updateDevice(id, device));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a device", description = "Deletes a device by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Device deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - cannot delete device in use")
    })
    public void deleteDevice(
            @Parameter(description = "Device ID", example = "1", required = true) @PathVariable Long id) {
        deviceService.deleteDevice(id);
    }

    public record DeviceRequest(
            @Parameter(description = "Device name", example = "iPhone 15", required = true) @NotBlank(message = "Name is required") String name,
            @Parameter(description = "Device brand", example = "Apple", required = true) @NotBlank(message = "Brand is required") String brand,
            @Parameter(description = "Device state", example = "AVAILABLE") @NotNull(message = "State is required") DeviceState state
    ) {
    }

    public record DeviceUpdateRequest(
            @Parameter(description = "Device name", example = "iPhone 15") String name,
            @Parameter(description = "Device brand", example = "Apple") String brand,
            @Parameter(description = "Device state (AVAILABLE, IN_USE, INACTIVE)", example = "AVAILABLE") DeviceState state
    ) {
    }
}
