package org.bharath.dao;

import org.bharath.Main;
import org.bharath.model.Users;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsersDaoImpl implements UsersDao{


    private static UsersDaoImpl usersDaoImp_ = null;
    private static Connection connection_;


    private UsersDaoImpl(Connection connection){
        connection_ = connection;
    }

    /**
     * This method returns the instance of the UserDeo class
     * @param connection
     * @return
     */
    public static UsersDaoImpl getInstance(Connection connection){
        if(usersDaoImp_ == null){
            usersDaoImp_ = new UsersDaoImpl(connection);
            return usersDaoImp_;
        }else{
            return usersDaoImp_;
        }
    }



    /**
     * This method fetches all the users data for this application
     * @return
     */
    @Override
    public List<Users> listAllUsers() {
        List<Users> allUsersData = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection_.prepareStatement("Select * from users");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Users users = new Users();
                users.setUserId_(resultSet.getInt("user_id"));
                users.setUserName_(resultSet.getString("user_name"));
                users.setPassword_(resultSet.getString("password"));
                users.setBirthday_(resultSet.getDate("birthdate").toLocalDate());
                users.setEmailId_(resultSet.getString("email_id"));
                users.setSignupDate_(resultSet.getDate("signup_date").toLocalDate());
                users.setSignupTime_(resultSet.getTime("signup_time").toLocalTime());
                users.setRoles_(resultSet.getString("roles"));
                users.setBlocked(resultSet.getString("is_blocked").charAt(0));
                allUsersData.add(users);
            }

            if(allUsersData.isEmpty()){
                Main.LOGGER.warning("No user records to fetch!");
                return null;
            }

            Main.LOGGER.info("User records fetched successfully");
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return null;
        }
        return allUsersData;
    }


    /**
     * This method fetches all the users data having the user role
     * @return
     */
    @Override
    public List<Users> listAllUserRoleAccounts(){
        List<Users> allUserRoleAccounts = new ArrayList<>();

        try {
            PreparedStatement selectQueryToFetchAllUsersHavingRoleAsUser = connection_.prepareStatement("select * from users where roles = 'user'");
            ResultSet resultSet = selectQueryToFetchAllUsersHavingRoleAsUser.executeQuery();
            while(resultSet.next()){
                Users users = new Users();
                users.setUserId_(resultSet.getInt("user_id"));
                users.setUserName_(resultSet.getString("user_name"));
                users.setPassword_(resultSet.getString("password"));
                users.setBirthday_(resultSet.getDate("birthdate").toLocalDate());
                users.setEmailId_(resultSet.getString("email_id"));
                users.setSignupDate_(resultSet.getDate("signup_date").toLocalDate());
                users.setSignupTime_(resultSet.getTime("signup_time").toLocalTime());
                users.setRoles_(resultSet.getString("roles"));
                users.setBlocked(resultSet.getString("is_blocked").charAt(0));
                allUserRoleAccounts.add(users);
            }
            if(allUserRoleAccounts.isEmpty()){
                Main.LOGGER.warning("There is no user role account");
                return null;
            }

            Main.LOGGER.info("Successfully fetched all the user role accounts");
            return allUserRoleAccounts;
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return null;
        }
    }

    /**
     * This method fetches all the users data having the user role
     * @return
     */
    @Override
    public List<Users> listAllTheAdminAccounts(){
        List<Users> allUserRoleAccounts = new ArrayList<>();

        try {
            PreparedStatement selectQueryToFetchAllUsersHavingRoleAsUser = connection_.prepareStatement("select * from users where roles = 'admin'");
            ResultSet resultSet = selectQueryToFetchAllUsersHavingRoleAsUser.executeQuery();
            while(resultSet.next()){
                Users users = new Users();
                users.setUserId_(resultSet.getInt("user_id"));
                users.setUserName_(resultSet.getString("user_name"));
                users.setPassword_(resultSet.getString("password"));
                users.setBirthday_(resultSet.getDate("birthdate").toLocalDate());
                users.setEmailId_(resultSet.getString("email_id"));
                users.setSignupDate_(resultSet.getDate("signup_date").toLocalDate());
                users.setSignupTime_(resultSet.getTime("signup_time").toLocalTime());
                users.setRoles_(resultSet.getString("roles"));
                users.setBlocked(resultSet.getString("is_blocked").charAt(0));
                allUserRoleAccounts.add(users);
            }
            if(allUserRoleAccounts.isEmpty()){
                Main.LOGGER.warning("There is no admin role account");
                return null;
            }

            Main.LOGGER.info("Successfully fetched all the admin role accounts");
            return allUserRoleAccounts;
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return null;
        }
    }

    /**
     * This method reset the password of the user
     * @param userId
     * @param targetUser
     * @param password
     * @return
     */
    @Override
    public boolean resetPassword(int userId, int targetUser, String password) {
        if(userId != targetUser){
            boolean isValidAdminUser = isAdminCheck(userId);
            if(!isValidAdminUser){
                Main.LOGGER.warning("You are not admin to change someone else password");
                return false;
            }
        }
        try {
            PreparedStatement updatePasswordStatement = connection_.prepareStatement("UPDATE users SET password = ? WHERE user_id = ?");
            updatePasswordStatement.setString(1, password);
            updatePasswordStatement.setInt(2, targetUser);
            updatePasswordStatement.execute();
            Main.LOGGER.info("Successfully Update the password for user id " + targetUser);
            return true;
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return false;
        }
    }

    /**
     * This method removes the user and related information from the db
     * @param userId
     * @param targetUser
     * @return
     */
    @Override
    public boolean removeUser(int userId, int targetUser) {

        boolean isTheUserAdmin = isAdminCheck(userId);

        if(!isTheUserAdmin){
            Main.LOGGER.warning("You are not the admin user to perform this operation");
            return false;
        }

        boolean isValidUser = isUserIdExits(targetUser);

        if(!isValidUser){
            Main.LOGGER.warning("Entered user id is not valid");
            return false;
        }

        if(userId == targetUser){
            Main.LOGGER.warning("You are not supposed to remove you own account");
            return false;
        }

        try {
            PreparedStatement deleteUserStatement = connection_.prepareStatement("DELETE FROM users WHERE user_id = ?");
            deleteUserStatement.setInt(1, targetUser);
            deleteUserStatement.execute();
            Main.LOGGER.info("Successfully deleted user_id: " + targetUser);
            return true;
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return false;
        }
    }

    /**
     * This method is used to check whether the user is admin user
     * @param userId
     * @return
     */
    @Override
    public boolean isAdminCheck(int userId){
        boolean isAValidUserId = isUserIdExits(userId);
        if(!isAValidUserId){
            Main.LOGGER.warning("User id is invalid!");
            return false;
        }

        try {
            PreparedStatement selectQueryToCheckForRoleAdmin = connection_.prepareStatement("select user_id from users where roles = 'admin' and user_id = ?");
            selectQueryToCheckForRoleAdmin.setInt(1, userId);
            ResultSet resultSet = selectQueryToCheckForRoleAdmin.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return false;
        }

    }

    /**
     * This method check the user has user role
     * @param userId
     * @return
     */
    @Override
    public boolean isUserRoleCheck(int userId){
        boolean isAValidUserId = isUserIdExits(userId);
        if(!isAValidUserId){
            Main.LOGGER.warning("User id is invalid!");
            return false;
        }

        try {
            PreparedStatement selectQueryToCheckForRoleAdmin = connection_.prepareStatement("select user_id from users where roles = 'user' and user_id = ?");
            selectQueryToCheckForRoleAdmin.setInt(1, userId);
            ResultSet resultSet = selectQueryToCheckForRoleAdmin.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return false;
        }

    }

    /**
     * This method adds new user to the database
     * @param userName
     * @param password
     * @param birthDate
     * @param emailId
     * @return
     */
    @Override
    public int addNewUser(String userName, String password, LocalDate birthDate, String emailId) {
        try {
            PreparedStatement addingUserStatement = connection_.prepareStatement("INSERT INTO users(\n" +
                    "\tuser_id, user_name, password, birthdate, email_id, signup_date, signup_time, roles)\n" +
                    "VALUES \n" +
                    "\t(nextval('user_id_sequence'), ?, ?, ?, ?, CURRENT_DATE, CURRENT_TIME, 'user')");
            addingUserStatement.setString(1, userName);
            addingUserStatement.setString(2, password);
            addingUserStatement.setDate(3, Date.valueOf(birthDate));
            addingUserStatement.setString(4, emailId);
            addingUserStatement.execute();
            int userId = getUserIdByEmailId(emailId);
            if(userId == -1){
                Main.LOGGER.warning("unable to create the user!");
                return -1;
            }
            Main.LOGGER.info("User created successfully with user id: " + userId);
            return userId;
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return -1;
        }
    }

    /**
     * This method is used to update the role of the user
     * @param userId
     * @param targetUserId
     * @param value
     * @return
     */
    @Override
    public boolean updateTheRoleOfTheUser(int userId, int targetUserId, String value) {

        boolean isTheUserAdmin = isAdminCheck(userId);
        if(!isTheUserAdmin){
            Main.LOGGER.warning("You are not the admin user to perform this operation");
            return false;
        }

        if(value.equals("admin")){
            boolean isUserRoleId = isUserRoleCheck(targetUserId);

            if(!isUserRoleId){
                Main.LOGGER.warning("Entered id is not the user role id");
                return false;
            }

            try {
                PreparedStatement updateFieldStatement = connection_.prepareStatement("UPDATE users SET roles = 'admin' WHERE user_id = ?");
                updateFieldStatement.setInt(1, targetUserId);
                updateFieldStatement.execute();
                Main.LOGGER.info(String.format("The field roles of user id %d is updated to %s", targetUserId, value));
                return true;
            } catch (SQLException e) {
                Main.LOGGER.severe(e.toString());
                return false;
            }
        }else if(value.equals("user")){
            if(userId == targetUserId){
                Main.LOGGER.warning("You cannot change your role by yourself");
                return false;
            }

            boolean isTheAdminId = isAdminCheck(targetUserId);
            if(!isTheAdminId){
                Main.LOGGER.warning("Entered target id is not the admin id");
                return false;
            }

            try {
                PreparedStatement updateFieldStatement = connection_.prepareStatement("UPDATE users SET roles = 'user' WHERE user_id = ?");
                updateFieldStatement.setInt(1, targetUserId);
                updateFieldStatement.execute();
                Main.LOGGER.info(String.format("The field roles of user id %d is updated to %s", targetUserId, value));
                return true;
            } catch (SQLException e) {
                Main.LOGGER.severe(e.toString());
                return false;
            }
        }else{
            Main.LOGGER.warning("Entered value is invalid");
            return false;
        }
    }

    /**
     * This method is used to retrieve the user data from DB by user id
     * @param userId
     * @return
     */
    @Override
    public Users getUser(int userId) {
        Users users = null;
        try {
            PreparedStatement selectQuery = connection_.prepareStatement("select * from users where user_id = ?");
            selectQuery.setInt(1, userId);
            ResultSet resultSet = selectQuery.executeQuery();

            if(!resultSet.next()){
                Main.LOGGER.warning("Unable to fetch the user details");
                return null;
            }

            users = new Users();
            users.setUserId_(resultSet.getInt("user_id"));
            users.setUserName_(resultSet.getString("user_name"));
            users.setPassword_(resultSet.getString("password"));
            users.setBirthday_(resultSet.getDate("birthdate").toLocalDate());
            users.setEmailId_(resultSet.getString("email_id"));
            users.setSignupDate_(resultSet.getDate("signup_date").toLocalDate());
            users.setSignupTime_(resultSet.getTime("signup_time").toLocalTime());
            users.setRoles_(resultSet.getString("roles"));
            users.setBlocked(resultSet.getString("is_blocked").charAt(0));
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return null;
        }
        return users;
    }

    /**
     * This method is used to block and unblock the user
     * @param userId
     * @param targetUser
     * @param value
     * @return
     */
    @Override
    public boolean blockAndUnblock(int userId, int targetUser, char value){
        boolean isTheUserAdmin = isAdminCheck(userId);
        if(!isTheUserAdmin){
            Main.LOGGER.warning("You are not the admin user to perform this operation");
            return false;
        }

        boolean isValidTargetUser = isUserIdExits(targetUser);

        if(!isValidTargetUser){
            Main.LOGGER.warning("Entered User is invalid");
            return false;
        }

        if(userId == targetUser){
            Main.LOGGER.warning("You are not supposed to change the your own blocking state");
            return false;
        }

        if(value == 'y'){
            boolean isUserAccountBlocked = isUserAccountBlocked(targetUser);
            if(isUserAccountBlocked){
                Main.LOGGER.warning("User account already blocked");
                return false;
            }

            try {
                PreparedStatement updateQueryToChangeTheBlockStatusOfTheUser = connection_.prepareStatement("update users set is_blocked = 'y' where user_id = ?");
                updateQueryToChangeTheBlockStatusOfTheUser.setInt(1, targetUser);
                updateQueryToChangeTheBlockStatusOfTheUser.execute();
                Main.LOGGER.info("Successfully changed the blockState of user");
                return true;
            } catch (SQLException e) {
                Main.LOGGER.severe(e.toString());
                return false;
            }
        }else if(value == 'n'){
            boolean isUserAccountBlocked = isUserAccountBlocked(targetUser);
            if(!isUserAccountBlocked){
                Main.LOGGER.warning("User account already unblocked");
                return false;
            }

            try {
                PreparedStatement updateQueryToChangeTheBlockStatusOfTheUser = connection_.prepareStatement("update users set is_blocked = 'n' where user_id = ?");
                updateQueryToChangeTheBlockStatusOfTheUser.setInt(1, targetUser);
                updateQueryToChangeTheBlockStatusOfTheUser.execute();
                Main.LOGGER.info("Successfully changed the blockState of user");
                return true;
            } catch (SQLException e) {
                Main.LOGGER.severe(e.toString());
                return false;
            }
        }else{
            Main.LOGGER.warning("Entered value is invalid");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return false;
        }
    }

    /**
     * This method is used to check whether the user account is blocked
     * @param userId
     * @return
     */
    @Override
    public boolean isUserAccountBlocked(int userId){
        boolean isValidUserId = isUserIdExits(userId);

        if(!isValidUserId){
            Main.LOGGER.warning("Entered user id is invalid");
            return false;
        }

        try {
            PreparedStatement selectQueryToCheckIfTheUserAccountBlocked = connection_.prepareStatement("select user_id from users where user_id = ? and is_blocked = 'y'");
            selectQueryToCheckIfTheUserAccountBlocked.setInt(1, userId);
            ResultSet resultSet = selectQueryToCheckIfTheUserAccountBlocked.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return false;
        }

    }

    /**
     * This method is used to check whether the userid is present in the database
     * @param userId
     * @return
     */
    @Override
    public boolean isUserIdExits(int userId) {
        try {
            PreparedStatement selectUserIdByFilteringUserId = connection_.prepareStatement("select user_id from users where user_id = ?");
            selectUserIdByFilteringUserId.setInt(1, userId);
            ResultSet resultSet = selectUserIdByFilteringUserId.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return false;
        }
    }

    /**
     * This method is used to check if the email is already exists
     * @param emailId
     * @return
     */
    @Override
    public boolean isEmailAlreadyExits(String emailId){
        try {
            PreparedStatement checkIsEmailPresentStatement = connection_.prepareStatement("select user_id from users where email_id = ?");
            checkIsEmailPresentStatement.setString(1, emailId);
            ResultSet resultSet = checkIsEmailPresentStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return false;
        }
    }

    /**
     * This method is used to get the user id by email_id
     * @param emailId
     * @return
     */
    @Override
    public int getUserIdByEmailId(String emailId){
        try{
            PreparedStatement selectStatementToFetchIdByEmailId = connection_.prepareStatement("select user_id from users where email_id = ?");
            selectStatementToFetchIdByEmailId.setString(1, emailId);
            ResultSet resultSet = selectStatementToFetchIdByEmailId.executeQuery();
            if(!resultSet.next()){
                return -1;
            }
            return resultSet.getInt("user_id");
        }catch(SQLException e){
            Main.LOGGER.severe(e.toString());
            return -1;
        }

    }

}
