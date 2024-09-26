package org.bharath.dao;

import org.bharath.model.Users;

import java.time.LocalDate;
import java.util.List;

public interface UsersDao {

    List<Users> listAllUsers();
    List<Users> listAllUserRoleAccounts();
    List<Users> listAllTheAdminAccounts();
    boolean resetPassword(int userId, int targetUser, String password);
    boolean removeUser(int userId, int targetUser);
    boolean isAdminCheck(int userId);
    boolean isUserRoleCheck(int userId);
    int addNewUser(String userName, String password, LocalDate birthDate, String emailId);
    boolean updateTheRoleOfTheUser(int userId, int targetUserId, String value);
    Users getUser(int userId);
    boolean blockAndUnblock(int userId, int targetUser, char value);
    boolean isUserAccountBlocked(int userId);
    boolean isUserIdExits(int userId);
    boolean isEmailAlreadyExits(String emailId);
    int getUserIdByEmailId(String emailId);
}
