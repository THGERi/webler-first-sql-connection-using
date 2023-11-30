package hu.webler.service;

import hu.webler.entity.User;
import hu.webler.model.UserModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserService {

    UserModel saveUser(User user, Connection connection) throws SQLException;
    User findUserById(Long id, Connection connection) throws SQLException;
    User findUserByEmail(String email, Connection connection) throws SQLException;
    Long findUserByEmailReturningId(String email, Connection connection) throws SQLException;
    UserModel updateUser(User user, Connection connection) throws SQLException;
    void deleteUser(User user, Connection connection) throws SQLException;
    List<UserModel> findAllUsers(Connection connection) throws SQLException;
}