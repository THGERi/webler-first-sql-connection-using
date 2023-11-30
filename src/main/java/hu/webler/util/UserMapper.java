package hu.webler.util;

import hu.webler.entity.User;
import hu.webler.model.UserModel;
import org.modelmapper.ModelMapper;

public class UserMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserModel mapUserToUserModel(User user) {
        return modelMapper.map(user, UserModel.class);
    }

    private UserMapper() {

    }
}