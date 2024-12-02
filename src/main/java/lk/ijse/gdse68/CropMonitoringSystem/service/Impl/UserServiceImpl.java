package lk.ijse.gdse68.CropMonitoringSystem.service.Impl;

import lk.ijse.gdse68.CropMonitoringSystem.CustomObj.Impl.UserErrorResponse;
import lk.ijse.gdse68.CropMonitoringSystem.CustomObj.UserResponse;
import lk.ijse.gdse68.CropMonitoringSystem.dao.UserDao;
import lk.ijse.gdse68.CropMonitoringSystem.dto.UserDTO;
import lk.ijse.gdse68.CropMonitoringSystem.entity.UserServiceEntity;
import lk.ijse.gdse68.CropMonitoringSystem.enums.Role;
import lk.ijse.gdse68.CropMonitoringSystem.exception.DataPersistFailedException;
import lk.ijse.gdse68.CropMonitoringSystem.exception.UserNotFoundException;
import lk.ijse.gdse68.CropMonitoringSystem.service.UserService;
import lk.ijse.gdse68.CropMonitoringSystem.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private Mapping mapping;
    @Override
    public void saveUser(UserDTO userDTO) {
        var userEntity = mapping.convertToUserEntity(userDTO);
        var savedUser = userDao.save(userEntity);
        if (savedUser == null){
            throw new DataPersistFailedException("Cannot Save User");
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserServiceEntity> getAllUsers = userDao.findAll();
        return mapping.convertUserToDTOList(getAllUsers);
    }

    @Override
    public UserResponse getSelectedUser(String email) {
        if (userDao.existsById(email)) {
            UserServiceEntity userEntityByEmail = userDao.getReferenceById(email);
            return mapping.convertToUserDTO(userEntityByEmail);
        } else {
            return new UserErrorResponse(0, "User not Found");
        }
    }

    @Override
    public void updateUser(String email, UserDTO incomeUserDTO) {
        UserServiceEntity existingUser = userDao.findById(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        UserServiceEntity updatedUser = new UserServiceEntity();
        updatedUser.setEmail(incomeUserDTO.getEmail() != null ? incomeUserDTO.getEmail() : existingUser.getEmail());
        updatedUser.setPassword(incomeUserDTO.getPassword() != null ? incomeUserDTO.getPassword() : existingUser.getPassword());

        if (incomeUserDTO.getRole() != null) {
            updatedUser.setRole(Role.valueOf(String.valueOf(incomeUserDTO.getRole())));
        } else {
            updatedUser.setRole(existingUser.getRole());
        }

        userDao.delete(existingUser);

        userDao.save(updatedUser);
    }

    @Override
    public void deleteUser(String email) {
        Optional<UserServiceEntity> findId = userDao.findById(email);
        if (!findId.isPresent()){
            throw new UserNotFoundException("User not Found");
        }else {
            userDao.deleteById(email);
        }
    }

    @Override
    public UserDetailsService userDetailsService() {
        return email ->
                userDao.findByEmail(email)
                        .orElseThrow(()-> new UserNotFoundException("User Not found"));
    }
}
