/**
 * This is the centralized controller which is the stating point for calling views and performing business logic
 */

package org.bharath.controller;

import org.bharath.Main;
import org.bharath.dao.*;
import org.bharath.model.Post;
import org.bharath.model.Users;
import org.bharath.utils.DBConnection;
import org.bharath.view.UsersView;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UsersController {
    private static final Connection CONNECTION = DBConnection.getConnection();
    private static final UsersView USERS_VIEW = new UsersView();
    private static final UsersDaoImpl USERS_DAO_IMP = UsersDaoImpl.getInstance(CONNECTION);
    private static PostController postController_ = null;
    private static FollowerController followerController_ = null;
    private static Users users_ = null;
    private static final LikeDaoImpl LIKE_DAO = LikeDaoImpl.getInstance(CONNECTION);
    private static final CommentDaoImpl COMMENT_DAO = CommentDaoImpl.getInstance(CONNECTION);
    private static final PostDaoImpl POST_DAO = PostDaoImpl.getInstance(CONNECTION);
    private static final FollowerDaoImpl FOLLOWER_DAO = FollowerDaoImpl.getInstance(CONNECTION);


    public Users forgetPassword(String emailId, String birthday) {
        int userId = USERS_DAO_IMP.getUserIdByEmailId(emailId);

        if(userId == -1){
            Main.LOGGER.warning("Entered email is not valid!");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return null;
        }

        Users users = USERS_DAO_IMP.getUser(userId);

        if(users == null){
            Main.LOGGER.warning("Something went wrong");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return null;
        }

        if(!users.getBirthday_().equals(LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd")))){
            Main.LOGGER.warning("Entered date of birth is invalid");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return null;
        }

        Main.LOGGER.info("Successfully got the userid and password");
        return users;
    }

    /**
     * This method takes the user input for registering the user account
     * @return -> returns boolean value based on the successful registration of the account
     */
    public boolean isCreatingNewAccountSuccessful(String userName, String password, LocalDate birthDayDate, String emailId ) {

        if(USERS_DAO_IMP.isEmailAlreadyExits(emailId)){
            Main.LOGGER.warning(String.format("Email id %s already exists", emailId));
            return false;
        }

        int userId = USERS_DAO_IMP.addNewUser(userName, password, birthDayDate, emailId);

        if(userId == -1){
            Main.LOGGER.warning("Something went wrong in creating user");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return false;
        }

        System.out.println("User id: " + userId);
        Main.CUSTOM_USER_INPUT.printSeparatorLine();
        return true;
    }

    /**
     * This method takes the login credentials from the user, validate it and returns true if the credential is valid and false if the credential is not valid
     * @return -> returns the boolean value based on the credentials entered by the user
     */
    public boolean isAuthenticationInValid(String userId, String password) {
        int userIdInteger = -1;
        try{
            userIdInteger = Integer.parseInt(userId);
        }catch(NumberFormatException e){
            Main.LOGGER.warning("User id incorrect\nuserid: " + userId);
            Main.LOGGER.severe(e.toString());
            return true;
        }


        boolean isExits = USERS_DAO_IMP.isUserIdExits(userIdInteger);
        if(!isExits){
            Main.LOGGER.warning("User id incorrect\nuserid: " + userId);
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return true;
        }

        Users users = USERS_DAO_IMP.getUser(userIdInteger);
        boolean isPasswordCorrect = users.getPassword_().equals(password);

        if(!isPasswordCorrect){
            Main.LOGGER.warning("Password incorrect!..");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return true;
        }

        Main.CUSTOM_USER_INPUT.printSeparatorLine();
        UsersController.users_ = users;

        if(users_.isBlocked() == 'y'){
            Main.LOGGER.warning("Your account is blocked cannot login");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return true;
        }

        Main.LOGGER.info("Welcome " + users.getUserName_());
        return false;
    }

    public void logout() {
        Main.LOGGER.info("Logging out");
        UsersController.users_ = null;
        UsersController.postController_ = null;
        Main.LOGGER.info("Thanks for using the application");
    }

    public boolean changePassword(int targetUserId) {
        Users targetUser = USERS_DAO_IMP.getUser(targetUserId);
        if(targetUser == null){
            Main.LOGGER.warning("Something went wrong!");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return true;
        }
        System.out.println("Enter password that matches below criteria:(Do not enter the old password) ");
        USERS_VIEW.printPasswordCriteria();
        String password = Main.CUSTOM_USER_INPUT.scanner.nextLine();
        while((!Pattern.matches( "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", password)) || targetUser.getPassword_().equals(password)){
            Main.LOGGER.warning("Password not met the criteria\nPassword: " + password);
            USERS_VIEW.printPasswordCriteria();
            System.out.println("Enter the password: ");
            password = Main.CUSTOM_USER_INPUT.scanner.nextLine();
        }

        boolean isPasswordChangeSuccessful = USERS_DAO_IMP.resetPassword(users_.getUserId_(), targetUserId, password);
        if(!isPasswordChangeSuccessful){
            Main.LOGGER.warning("Something went wrong");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return true;
        }

        users_.setPassword_(password);
        return false;
    }


    public boolean blockUnblockUserAccount(int userId, int targetUserId, char value){
        return USERS_DAO_IMP.blockAndUnblock(userId, targetUserId, value);
    }

    public boolean removeUserAccount(int targetUserId) {
        List<Post> allPostOfUser = POST_DAO.getAllMyPost(targetUserId);

        if(allPostOfUser != null){
            boolean isCommentInterrupted = false;
            boolean isLikeInterrupted = false;
            boolean isPostInterrupted = false;

            for(Post currentPost : allPostOfUser){
                boolean isCommentsDeleted = COMMENT_DAO.deleteAllCommentsForThePost(currentPost.getPostId());
                if(!isCommentsDeleted){
                    Main.LOGGER.warning("Something went wrong in deleting the comments!");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    isCommentInterrupted = true;
                    break;
                }

                boolean isLikesDeleted = LIKE_DAO.removeAllLikeForSpecificPost(currentPost.getPostId());
                if(!isLikesDeleted){
                    Main.LOGGER.warning("Something went wrong in deleting all the likes of the user!");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    isLikeInterrupted = true;
                    break;
                }

                boolean isPostDeleted = POST_DAO.removePost(currentPost.getPostId());

                if(!isPostDeleted){
                    Main.LOGGER.warning("Something went wrong");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    isPostInterrupted = true;
                    break;
                }
            }
            if(isCommentInterrupted || isLikeInterrupted || isPostInterrupted){
                Main.LOGGER.warning("Something went wrong");
                Main.CUSTOM_USER_INPUT.printSeparatorLine();
                return true;
            }
        }

        boolean isCommentsForTheUsersDeleted = COMMENT_DAO.deleteAllCommentsForTheUser(targetUserId);

        if(!isCommentsForTheUsersDeleted){
            Main.LOGGER.warning("Something went wrong");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return true;
        }

        boolean isLikesDeletedUsingUserId = LIKE_DAO.removeAllLikesByUserId(targetUserId);

        if(!isLikesDeletedUsingUserId){
            Main.LOGGER.warning("Something went wrong");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return true;
        }

        List<Users> listOfAllFollowers = FOLLOWER_DAO.listOfAllUsersFollowingYou(targetUserId);

        if(listOfAllFollowers != null){
            boolean isFollowingUserUnFollowInterrupted = false;
            for(Users users : listOfAllFollowers){
                if(!FOLLOWER_DAO.unFollowUser(users.getUserId_(), targetUserId)){
                    isFollowingUserUnFollowInterrupted = true;
                    break;
                }
            }
            if(isFollowingUserUnFollowInterrupted){
                Main.LOGGER.warning("Something went wrong");
                Main.CUSTOM_USER_INPUT.printSeparatorLine();
                return true;
            }
        }

        List<Users> listOfAllUsersFollowing = FOLLOWER_DAO.listAllFollowedUsers(targetUserId);

        if(listOfAllUsersFollowing != null){
            boolean isFollowingUserUnFollowInterrupted = false;
            for(Users users : listOfAllUsersFollowing){
                if(!FOLLOWER_DAO.unFollowUser(targetUserId, users.getUserId_())){
                    isFollowingUserUnFollowInterrupted = true;
                    break;
                }
            }
            if(isFollowingUserUnFollowInterrupted){
                Main.LOGGER.warning("Something went wrong");
                Main.CUSTOM_USER_INPUT.printSeparatorLine();
                return true;
            }
        }

        boolean  isAccountSuccessfullyRemoved = USERS_DAO_IMP.removeUser(users_.getUserId_(), targetUserId);

        if(!isAccountSuccessfullyRemoved){
            Main.LOGGER.warning("Something went wrong");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return true;
        }
        return false;
    }


    public List<Users> listAllAccountsWithoutMe(){
        List<Users> allUserAccounts = USERS_DAO_IMP.listAllUsers();
        if(allUserAccounts == null){
            Main.LOGGER.warning("Something went wrong");
            return  null;
        }

        allUserAccounts.removeIf(e -> e.getUserId_() == users_.getUserId_());

        if(allUserAccounts.isEmpty()){
            Main.LOGGER.warning("You are the only user in this application");
            return null;
        }
        return allUserAccounts;
    }

    public List<Users> listAllAdminAccountsWithoutMe() {
        List<Users> allTheAdminRoleAccounts = USERS_DAO_IMP.listAllTheAdminAccounts();

        if (allTheAdminRoleAccounts == null) {
            Main.LOGGER.warning("Something went wrong");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return null;
        }

        allTheAdminRoleAccounts.removeIf(e -> e.getUserId_() == users_.getUserId_());

        if (allTheAdminRoleAccounts.isEmpty()) {
            Main.LOGGER.warning("There is no another admin account");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return null;
        }
        return allTheAdminRoleAccounts;
    }

    public boolean updateUserRole(int userId, int targetId, String value){
        return USERS_DAO_IMP.updateTheRoleOfTheUser(userId, targetId, value);
    }

    public List<Users> getListOfAllUserAcconts() {
        List<Users> allTheUserRoleAccounts = USERS_DAO_IMP.listAllUserRoleAccounts();

        if(allTheUserRoleAccounts == null){
            Main.LOGGER.warning("Something went wrong");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return null;
        }
        return allTheUserRoleAccounts;
    }

    public static Users getUsers_() {
        return users_;
    }

    public List<Users> getListOfBlockedUsers(){
        return listAllAccountsWithoutMe().stream().filter( e -> USERS_DAO_IMP.isUserAccountBlocked(e.getUserId_())).collect(Collectors.toList());
    }
}
