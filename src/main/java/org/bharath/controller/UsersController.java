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

public class UsersController {
    private static final Connection CONNECTION = DBConnection.getConnection();
    private static final UsersView USERS_VIEW = new UsersView();
    private static final UsersDaoImp USERS_DAO_IMP = UsersDaoImp.getInstance(CONNECTION);
    private static PostController postController_ = null;
    private static FollowerController followerController_ = null;
    private static Users users_ = null;
    private static final LikeDao LIKE_DAO = LikeDao.getInstance(CONNECTION);
    private static final CommentDao COMMENT_DAO = CommentDao.getInstance(CONNECTION);
    private static final PostDao POST_DAO = PostDao.getInstance(CONNECTION);
    private static final FollowerDao FOLLOWER_DAO = FollowerDao.getInstance(CONNECTION);




    /**
     * This method takes care of Welcome page and user choice and perform navigation accordingly
     *                  "\t1. Login\n" +
     *                 "\t2. Sign up\n" +
     *                 "\t3. Forget Password\n" +
     *                 "\t0. Quit the application\n
     */
    public void welcomeMessage(){
        welcomeMessageLoop :
        while(true){


            USERS_VIEW.printAuthenticationOptionMenu();
            int choice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, 3);
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            switch(choice){
                case 0:
                {
                    Main.LOGGER.info("Thank you for using this application");
                    break welcomeMessageLoop;
                }
                case 1:
                {
                    if (isAuthenticationInValid()){
                        break;
                    }

                    if(!users_.getRoles_().equals("admin")){
                        performMainMenu();
                        break;
                    }

                    performMainMenuIfAdmin();
                    break;
                }
                case 2:
                {
                    if (!isCreatingNewAccountSuccessful()) {
                        break;
                    }

                    if(isAuthenticationInValid()){
                        break;
                    }

                    if(!users_.getRoles_().equals("admin")){
                        performMainMenu();
                        break;
                    }

                    performMainMenuIfAdmin();
                    break;
                }
                case 3:
                {
                    System.out.println("Enter email id: ");
                    String emailId = Main.CUSTOM_USER_INPUT.scanner.nextLine();
                    System.out.println("Enter date of birth in yyyy-MM-dd format eg. 2000-12-18:");
                    String birthday = Main.CUSTOM_USER_INPUT.scanner.nextLine();
                    while(!Pattern.matches( "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", birthday)){
                        Main.LOGGER.warning("Entered birthday date is invalid\nbirthday: " + birthday);
                        System.out.println("Enter date of birth in yyyy-MM-dd format eg. 2000-12-18: ");
                        birthday = Main.CUSTOM_USER_INPUT.scanner.nextLine();
                    }

                    int userId = USERS_DAO_IMP.getUserIdByEmailId(emailId);

                    if(userId == -1){
                        Main.LOGGER.warning("Entered email is not valid!");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Users users = USERS_DAO_IMP.getUser(userId);

                    if(users == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    if(!users.getBirthday_().equals(LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd")))){
                        Main.LOGGER.warning("Entered date of birth is invalid");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println(String.format("Your user id : %d and password: %s", userId, users.getPassword_()));
                    Main.LOGGER.info("Successfully got the userid and password");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
            }
        }

    }



    /**
     * This method takes the user input for registering the user account
     * @return -> returns boolean value based on the successful registration of the account
     */
    private boolean isCreatingNewAccountSuccessful() {
        System.out.println("Signup page");
        System.out.println("Enter user name: ");
        String userName = Main.CUSTOM_USER_INPUT.scanner.nextLine();
        System.out.println("Enter password that matches below criteria: ");
        USERS_VIEW.printPasswordCriteria();
        String password = Main.CUSTOM_USER_INPUT.scanner.nextLine();
        while(!Pattern.matches( "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", password)){
            Main.LOGGER.warning("Password not met the criteria\nPassword: " + password);
            USERS_VIEW.printPasswordCriteria();
            System.out.println("Enter the password: ");
            password = Main.CUSTOM_USER_INPUT.scanner.nextLine();
        }
        System.out.println("Enter date of birth in yyyy-MM-dd format eg. 2000-12-18: \nYour age needs to be in the range of 18-110");
        String birthday = Main.CUSTOM_USER_INPUT.scanner.nextLine();
        while(!Pattern.matches( "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", birthday)){
            Main.LOGGER.warning("Entered birthday date is invalid\nbirthday: " + birthday);
            System.out.println("Enter date of birth in yyyy-MM-dd format eg. 2000-12-18: ");
            birthday = Main.CUSTOM_USER_INPUT.scanner.nextLine();
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDayDate = LocalDate.parse(birthday, dateTimeFormatter);
        if(birthDayDate.isBefore(LocalDate.now().minusYears(110)) || birthDayDate.isAfter(LocalDate.now().minusYears(18))){
            Main.LOGGER.warning("Entered date is invalid");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return false;
        }

        System.out.println("Enter email id: ");
        String emailId = Main.CUSTOM_USER_INPUT.scanner.nextLine();
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
    private boolean isAuthenticationInValid() {
        System.out.println("Login page");
        System.out.println("Enter the user_id: ");
        int userId = -1;
        try{
            userId = Integer.parseInt(Main.CUSTOM_USER_INPUT.scanner.nextLine());
        }catch(NumberFormatException e){
            Main.LOGGER.warning("User id incorrect\nuserid: " + userId);
            Main.LOGGER.severe(e.toString());
            return true;
        }

        System.out.println("Enter the password: ");
        String password = Main.CUSTOM_USER_INPUT.scanner.nextLine();

        boolean isExits = USERS_DAO_IMP.isUserIdExits(userId);
        if(!isExits){
            Main.LOGGER.warning("User id incorrect\nuserid: " + userId);
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return true;
        }

        Users users = USERS_DAO_IMP.getUser(userId);
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


    /**
     * This method takes the user input and perform navigation part the different modules
     *                  "\t1. Posts page\n" +
     *                 "\t2. People page\n" +
     *                 "\t0. Logout"
     */
    private void performMainMenu() {
        mainPageLoop:
        while(true){
            USERS_VIEW.printMainMenu();
            int choice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, 2);
            switch(choice){
                case 0: {
                    Main.LOGGER.info("Logging out");
                    UsersController.users_ = null;
                    UsersController.postController_ = null;
                    Main.LOGGER.info("Thanks for using the application");
                    break mainPageLoop;
                }
                case 1:
                {
                    postController_ = new PostController(UsersController.users_);
                    postController_.chooseOptionsMainPostPage();
                    break;
                }
                case 2:
                {
                    followerController_ = new FollowerController(UsersController.users_);
                    followerController_.chooseOptionMainFollowPage();
                    break;
                }
            }
        }
    }


    /**
     * This method performs the action entered by the admin user
     *                  "\t1. Posts Page\n" +
     *                 "\t2. People Page\n" +
     *                 "\t3. Account Action Page\n" +
     *                 "\t0. Logout"
     */
    private void performMainMenuIfAdmin() {
        mainPageLoop:
        while(true){
            USERS_VIEW.printAdminUserMainMenu();
            int choice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, 3);
            switch(choice){
                case 0: {
                    Main.LOGGER.info("Logging out");
                    UsersController.users_ = null;
                    UsersController.postController_ = null;
                    Main.LOGGER.info("Thanks for using the application");
                    break mainPageLoop;
                }
                case 1:
                {
                    postController_ = new PostController(UsersController.users_);
                    postController_.chooseOptionsMainPostPageIfAdmin();
                    break;
                }
                case 2:
                {
                    followerController_ = new FollowerController(UsersController.users_);
                    followerController_.chooseOptionMainFollowPageIfAdmin();
                    break;
                }
                case 3:
                {
                    performActionOnUserAccounts();
                    break;
                }
            }
        }
    }



    /**
     * This method performs account related actions entered by the admin user
     *                 "\t1. Make the user as admin\n" +
     *                 "\t2. Make the admin as user\n" +
     *                 "\t3. Remove user account\n" +
     *                 "\t4. Block user account\n" +
     *                 "\t5. Unblock user account\n" +
     *                 "\t6. Change password for user account\n" +
     *                 "\t0. Back to previous page"
     */
    private void performActionOnUserAccounts(){
        mainAccountActionPageLoop:
        while(true){
            USERS_VIEW.printAccountActionMainMenuPageForAdmin();
            int choice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, 6);
            switch(choice){
                case 0: {
                    Main.LOGGER.info("Back to previous menu");
                    break mainAccountActionPageLoop;
                }
                case 1:
                {
                    List<Users> allTheUserRoleAccounts = USERS_DAO_IMP.listAllUserRoleAccounts();

                    if(allTheUserRoleAccounts == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Below are the list of users in the application under user role: ");
                    allTheUserRoleAccounts.forEach(e -> System.out.println("User id: " + e.getUserId_() + " :-> " + "user name: " + e.getUserName_()));
                    System.out.println("Choose the user account id to switch the role to admin:(press '0' to get back to previous menu): ");

                    int maxUserId = allTheUserRoleAccounts.stream().map(Users::getUserId_).max(Integer::compareTo).get();
                    int targetUserId = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, maxUserId);
                    if(targetUserId == 0){
                        Main.LOGGER.info("Back to previous menu");
                        break;
                    }
                    boolean isUpdatedTheRoleOfTheUser = USERS_DAO_IMP.updateTheRoleOfTheUser(users_.getUserId_(), targetUserId, "admin");

                    if(!isUpdatedTheRoleOfTheUser){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.LOGGER.info("Successfully updated the role of the user");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 2:
                {
                    List<Users> allTheAdminRoleAccounts = USERS_DAO_IMP.listAllTheAdminAccounts();

                    if(allTheAdminRoleAccounts == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    allTheAdminRoleAccounts.removeIf(e -> e.getUserId_() ==  users_.getUserId_());

                    if(allTheAdminRoleAccounts.isEmpty()){
                        Main.LOGGER.warning("There is no another admin account");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Below are the list of users in the application under admin role: ");
                    allTheAdminRoleAccounts.forEach(e -> System.out.println("User id: " + e.getUserId_() + " :-> " + "user name: " + e.getUserName_()));
                    System.out.println("Choose the user account id to switch the admin to user:(press '0' to get back to previous menu): ");

                    int maxUserId = allTheAdminRoleAccounts.stream().map(Users::getUserId_).max(Integer::compareTo).get();
                    int targetUserId = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, maxUserId);
                    if(targetUserId == 0){
                        Main.LOGGER.info("Back to previous menu");
                        break;
                    }
                    boolean isUpdatedTheRoleOfTheUser = USERS_DAO_IMP.updateTheRoleOfTheUser(users_.getUserId_(), targetUserId, "user");

                    if(!isUpdatedTheRoleOfTheUser){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.LOGGER.info("Successfully updated the role of the user");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 3:
                {
                    List<Users> allUserAccounts = USERS_DAO_IMP.listAllUsers();

                    if(allUserAccounts == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    allUserAccounts.removeIf(e -> e.getUserId_() == users_.getUserId_());

                    if(allUserAccounts.isEmpty()){
                        Main.LOGGER.warning("You are the only user in the account");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Below are the list of all the users in the application: ");
                    allUserAccounts.forEach(e -> System.out.println("User id: " + e.getUserId_() + " :-> " + "user name: " + e.getUserName_()));
                    System.out.println("Choose the user account id to delete the account:(press '0' to get back to previous menu): ");

                    int maxUserId = allUserAccounts.stream().map(Users::getUserId_).max(Integer::compareTo).get();
                    int targetUserId = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, maxUserId);
                    if(targetUserId == 0){
                        Main.LOGGER.info("Back to previous menu");
                        break;
                    }

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
                            break;
                        }
                    }

                    boolean isCommentsForTheUsersDeleted = COMMENT_DAO.deleteAllCommentsForTheUser(targetUserId);

                    if(!isCommentsForTheUsersDeleted){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    boolean isLikesDeletedUsingUserId = LIKE_DAO.removeAllLikesByUserId(targetUserId);

                    if(!isLikesDeletedUsingUserId){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
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
                            break;
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
                            break;
                        }
                    }

                    boolean  isAccountSuccessfullyRemoved = USERS_DAO_IMP.removeUser(users_.getUserId_(), targetUserId);

                    if(!isAccountSuccessfullyRemoved){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.LOGGER.info("Removed the user account successfully");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 4:
                {
                    List<Users> allUserAccounts = USERS_DAO_IMP.listAllUsers();

                    if(allUserAccounts == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    allUserAccounts.removeIf(e -> e.getUserId_() == users_.getUserId_());

                    if(allUserAccounts.isEmpty()){
                        Main.LOGGER.warning("You are the only user in the account");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Below are the list of all the users in the application: ");
                    allUserAccounts.forEach(e -> System.out.println("User id: " + e.getUserId_() + " :-> " + "user name: " + e.getUserName_()));
                    System.out.println("Choose the user account id to block the account:(press '0' to get back to previous menu): ");

                    int maxUserId = allUserAccounts.stream().map(Users::getUserId_).max(Integer::compareTo).get();
                    int targetUserId = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, maxUserId);
                    if(targetUserId == 0){
                        Main.LOGGER.info("Back to previous menu");
                        break;
                    }

                    boolean isUserAccountBlocked = USERS_DAO_IMP.blockAndUnblock(users_.getUserId_(), targetUserId, 'y');

                    if(!isUserAccountBlocked){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.LOGGER.info("Blocking the user is successful");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 5:
                {
                    List<Users> allUserAccounts = USERS_DAO_IMP.listAllUsers();

                    if(allUserAccounts == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    allUserAccounts.removeIf(e -> e.getUserId_() == users_.getUserId_());

                    if(allUserAccounts.isEmpty()){
                        Main.LOGGER.warning("You are the only user in the account");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Below are the list of all the users in the application: ");
                    allUserAccounts.forEach(e -> System.out.println("User id: " + e.getUserId_() + " :-> " + "user name: " + e.getUserName_()));
                    System.out.println("Choose the user account id to unblock the account:(press '0' to get back to previous menu): ");

                    int maxUserId = allUserAccounts.stream().map(Users::getUserId_).max(Integer::compareTo).get();
                    int targetUserId = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, maxUserId);
                    if(targetUserId == 0){
                        Main.LOGGER.info("Back to previous menu");
                        break;
                    }

                    boolean isUserAccountBlocked = USERS_DAO_IMP.blockAndUnblock(users_.getUserId_(), targetUserId, 'n');

                    if(!isUserAccountBlocked){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.LOGGER.info("Unblocking the user is successful");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 6:
                {
                    List<Users> allUserAccounts = USERS_DAO_IMP.listAllUsers();

                    if(allUserAccounts == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    if(allUserAccounts.isEmpty()){
                        Main.LOGGER.warning("You are the only user in the account");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Below are the list of all the users in the application: ");
                    allUserAccounts.forEach(e -> System.out.println("User id: " + e.getUserId_() + " :-> " + "user name: " + e.getUserName_()));
                    System.out.println("Choose the user account id to change the account password:(press '0' to get back to previous menu): ");

                    int maxUserId = allUserAccounts.stream().map(Users::getUserId_).max(Integer::compareTo).get();
                    int targetUserId = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, maxUserId);
                    if(targetUserId == 0){
                        Main.LOGGER.info("Back to previous menu");
                        break;
                    }

                    Users targetUser = USERS_DAO_IMP.getUser(targetUserId);
                    if(targetUser == null){
                        Main.LOGGER.warning("Something went wrong!");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
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
                        break;
                    }

                    users_.setPassword_(password);
                    Main.LOGGER.info("Successfully changed the password");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();;
                    break;
                }
            }
        }
    }
}
