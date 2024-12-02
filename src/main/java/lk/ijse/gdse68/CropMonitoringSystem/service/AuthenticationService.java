package lk.ijse.gdse68.CropMonitoringSystem.service;

import lk.ijse.gdse68.CropMonitoringSystem.dto.UserDTO;
import lk.ijse.gdse68.CropMonitoringSystem.jwtModles.JWTResponse;
import lk.ijse.gdse68.CropMonitoringSystem.jwtModles.SignIn;

public interface AuthenticationService {
    JWTResponse signIn(SignIn signIn);
    JWTResponse signUp(UserDTO userDTO);
    JWTResponse refreshToken(String accessToken);
}
