package hu.webler.service;

import hu.webler.entity.User;
import hu.webler.exception.UserNotFoundException;
import hu.webler.model.UserModel;
import hu.webler.util.UserMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    private static final Logger logger = Logger.getLogger(String.valueOf(UserServiceImpl.class));

    @Override
    public UserModel saveUser(User user, Connection connection) throws SQLException {

        // ? -> in prepared statement are placeholders or parameter markers
        String sql = "INSERT INTO user (username, email, password, first_name, last_name) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully."); // You can replace this with your own logging or error handling
                return UserMapper.mapUserToUserModel(user);
            } else {
                System.out.println("Data insertion failed."); // You can replace this with your own logging or error handling
            }
        }
        return null;
    }

    @Override
    public User findUserById(Long id, Connection connection) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            } else {
                // Handle the case where no user with the given ID is found
                String message = "User not found with ID: " + id;
                logger.warning(message);
                throw new UserNotFoundException(message);
            }
        }
    }

    @Override
    public User findUserByEmail(String email, Connection connection) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            } else {
                // No user with the given email is found, return null
                logger.warning("User not found with email: " + email);
                return null;
            }
        }
    }

    @Override
    public Long findUserByEmailReturningId(String email, Connection connection) throws SQLException {
        String sql = "SELECT id FROM user WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public UserModel updateUser(User user, Connection connection) throws SQLException {
        Long userId = findUserByEmailReturningId(user.getEmail(), connection);
        if (userId != null) {
            // Step 2: Use the retrieved userId to update the user's information in the database
            String sql = "UPDATE user SET username = ?, password = ?, email = ?, first_name = ?, last_name = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.hashPassword(user.getPassword()));
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getFirstName());
                statement.setString(5, user.getLastName());
                statement.setLong(6, userId); // Use the retrieved userId to update the correct user

                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    String message = "User updated successfully.";
                    logger.info(message);
                    System.out.println(message); // You can replace this with your own logging or error handling
                    // Now, use the mapper to convert the User to a UserModel
                    return UserMapper.mapUserToUserModel(user);
                } else {
                    String message = "User update failed.";
                    logger.warning(message);
                    System.out.println(message);
                }
            }
        } else {
            String message = "User not found for the provided email/username.";
            logger.info(message);
            System.out.println(message);
        }
        return null; // Return null if the update function has been failed or handle the failure case accordingly.
    }

    @Override
    public void deleteUser(User user, Connection connection) throws SQLException {
        User userToDelete = findUserByEmail(user.getEmail(), connection);
        if (userToDelete != null) {
            String sql = "DELETE FROM user WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, user.getEmail());

                int rowsDeleted = statement.executeUpdate();

                if (rowsDeleted > 0) {
                    String message = "User deleted successfully.";
                    logger.info(message);
                    System.out.println(message); // You can replace this with your own logging or error handling
                } else {
                    System.out.println("User deletion failed.");
                }
            }
        } else {
            String message = "User not found for the provided email/username.";
            logger.info(message);
            System.out.println(message);
        }
    }

    @Override
    public List<UserModel> findAllUsers(Connection connection) throws SQLException {
        String sql = "SELECT * FROM user";
        List<UserModel> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User currentUser = mapResultSetToUser(resultSet);
                users.add(UserMapper.mapUserToUserModel(currentUser));
            }
        }
        return users;
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        return user;
    }
}