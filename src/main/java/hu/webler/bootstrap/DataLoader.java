package hu.webler.bootstrap;

import hu.webler.config.DatabaseConnection;
import hu.webler.entity.User;
import hu.webler.model.UserModel;
import hu.webler.service.UserService;
import hu.webler.service.UserServiceImpl;

import java.sql.Connection;
import java.sql.SQLException;

public class DataLoader {

    private final UserService userService;

    public DataLoader() {
        userService = new UserServiceImpl();
    }

    public void loadData() {
        Connection connection = DatabaseConnection.getConnection();

        if (connection != null) {
            User admin = new User("Csaba79-coder", "password", "csabavadasz79@gmail.com", "Csaba", "Vadasz");
            User user = new User("johndoe", "hashedpassword", "johndoe@example.com", "John", "Doe");
            User userTest = new User("Teszt Elek", "testpassword", "test@test.org", "Elek", "Teszt");
            try {
                UserModel adminModel = userService.saveUser(admin, connection);
                System.out.println(adminModel);
                UserModel model = userService.saveUser(user, connection);
                System.out.println(model);
                Long id = 1L;
                User userById = userService.findUserById(id, connection);
                System.out.println(userById);
                User userByEmail = userService.findUserByEmail("csaba79@gmail.com", connection);
                System.out.println(userByEmail);

                User modifyUser = new User("John Doe", "hashedpassword", "johndoe@example.com", "John", "Doe");
                UserModel updatedUser = userService.updateUser(modifyUser, connection);
                System.out.println(updatedUser);

                User userToDelete = new User("John Doe", "hashedpassword", "johndoe@example.com", "John", "Doe");
                userService.deleteUser(userToDelete, connection);

                UserModel testUser = userService.saveUser(userTest, connection);
                System.out.println(testUser);
                System.out.println(userService.findAllUsers(connection));

                User userToUpdateNotExisting = new User("John Doe", "hashedpassword", "janedoe@example.com", "John", "Doe");
                UserModel updatedUserNotExisting = userService.updateUser(userToUpdateNotExisting, connection);
                System.out.println(updatedUserNotExisting);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                DatabaseConnection.closeConnection();
            }
        }
    }
}