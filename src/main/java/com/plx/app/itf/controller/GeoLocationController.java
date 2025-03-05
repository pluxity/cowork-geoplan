package com.plx.app.itf.controller;

import com.plx.app.itf.service.GeoLocationService;
import com.plx.app.itf.vo.ApiResponseBody;
import com.plx.app.itf.vo.GeoLocationRequestDTO;
import com.plx.app.itf.vo.InplabRequestDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class GeoLocationController {

    protected Log logger = LogFactory.getLog(getClass());

    private final GeoLocationService geoLocationService;

    @PostMapping(value = "/api/objects")
    public ResponseEntity<ApiResponseBody> postObjectInfo(
            @RequestBody GeoLocationRequestDTO dto
    ) {
        ApiResponseBody result;
        try {
             result = geoLocationService.postGeoLocation(dto);
        } catch (RuntimeException e) {
            result = ApiResponseBody.builder()
                    .result(HttpStatus.BAD_REQUEST)
                    .message(e.getMessage())
                    .build();
        } catch (IOException ie) {
            logger.error(ie.getMessage());

            result = ApiResponseBody.builder()
                    .result(HttpStatus.OK)
                    .message("SUCCESS")
                    .build();
        }

        return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = "/api/inplab/objects")
    public ResponseEntity<ApiResponseBody> postInplabObjectInfo(
            @RequestBody InplabRequestDTO dto
    ) {
        ApiResponseBody result;
        try {
            result = geoLocationService.postInplabGeoLocation(dto);
        } catch (RuntimeException e) {
            result = ApiResponseBody.builder()
                    .result(HttpStatus.BAD_REQUEST)
                    .message(e.getMessage())
                    .build();
        } catch (IOException ie) {
            logger.error(ie.getMessage());

            result = ApiResponseBody.builder()
                    .result(HttpStatus.OK)
                    .message("SUCCESS")
                    .build();
        }

        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/api/objects")
    public ResponseEntity<String> getObjectInfo() {
        String result = geoLocationService.getLocations();
        return ResponseEntity.ok().body(result);
    }

}
