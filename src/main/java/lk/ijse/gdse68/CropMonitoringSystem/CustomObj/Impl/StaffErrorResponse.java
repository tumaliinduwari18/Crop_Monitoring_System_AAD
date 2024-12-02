package lk.ijse.gdse68.CropMonitoringSystem.CustomObj.Impl;

import lk.ijse.gdse68.CropMonitoringSystem.CustomObj.StaffResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffErrorResponse implements StaffResponse, Serializable {
    private int errorCode;
    private String errorMessage;
}