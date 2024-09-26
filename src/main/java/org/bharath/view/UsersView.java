/**
 * This is a view for user page to print the menu
 */
package org.bharath.view;

import org.bharath.Main;
import org.bharath.controller.UsersController;
import org.bharath.model.Users;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

public class UsersView {
    private final UsersController usersController_ = new UsersController();
    public void printAuthenticationOptionMenu(){
        System.out.println("Hi!.. Welcome to ABC Social Media\n" +
                "Authentication Page\n" +
                "Choose below option\n" +
                "\t1. Login\n" +
                "\t2. Sign up\n" +
                "\t3. Forget Password\n" +
                "\t0. Quit the application\n");
    }
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
            printAuthenticationOptionMenu();
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
                    System.out.println("Login page");
                    System.out.println("Enter the user_id: ");
                    String userId = Main.CUSTOM_USER_INPUT.scanner.nextLine();
                    System.out.println("Enter the password: ");
                    String password = Main.CUSTOM_USER_INPUT.scanner.nextLine();

                    if (usersController_.isAuthenticationInValid(userId, password)){
                        break;
                    }

                    if(!UsersController.getUsers_().getRoles_().equals("admin")){
                        performMainMenu();
                        break;
                    }

                    performMainMenuIfAdmin();
                    break;
                }
                case 2:
                {
                    System.out.println("Signup page");
                    System.out.println("Enter user name: ");
                    String userName = Main.CUSTOM_USER_INPUT.scanner.nextLine();
                    System.out.println("Enter password that matches below criteria: ");
                    printPasswordCriteria();
                    String password = Main.CUSTOM_USER_INPUT.scanner.nextLine();
                    while(!Pattern.matches( "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", password)){
                        Main.LOGGER.warning("Password not met the criteria\nPassword: " + password);
                        printPasswordCriteria();
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
                        break;
                    }

                    System.out.println("Enter email id: ");
                    String emailId = Main.CUSTOM_USER_INPUT.scanner.nextLine();

                    if (!usersController_.isCreatingNewAccountSuccessful(userName, password, birthDayDate, emailId)) {
                        break;
                    }

                    System.out.println("Login page");
                    System.out.println("Enter the user_id: ");
                    String userId = Main.CUSTOM_USER_INPUT.scanner.nextLine();
                    System.out.println("Enter the password: ");
                    String passwordAuth = Main.CUSTOM_USER_INPUT.scanner.nextLine();

                    if(usersController_.isAuthenticationInValid(userId, passwordAuth)){
                        break;
                    }

                    if(!UsersController.getUsers_().getRoles_().equals("admin")){
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

                    Users users = usersController_.forgetPassword(emailId, birthday);

                    if(users == null){
                        Main.LOGGER.warning("Something went wrong");
                        break;
                    }

                    System.out.println(String.format("Your user id : %d and password: %s", users.getUserId_(), users.getPassword_()));
                    Main.LOGGER.info("Successfully got the userid and password");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
            }
        }

    }
    /**
     * This method takes the user input and perform navigation part the different modules
     *                  "\t1. Posts page\n" +
     *                 "\t2. People page\n" +
     *                 "\t0. Logout"
     */
    public void performMainMenu() {
        mainPageLoop:
        while (true) {
            printMainMenu();
            int choice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, 2);
            switch (choice) {
                case 0: {
                    usersController_.logout();
                    break mainPageLoop;
                }
                case 1: {
                    PostView postView = new PostView();
                    postView.chooseOptionsMainPostPage();
                    break;
                }
                case 2: {
                    FollowerView followerView = new FollowerView();
                    followerView.chooseOptionMainFollowPage();
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
    public void performMainMenuIfAdmin() {
        mainPageLoop:
        while(true){
            printAdminUserMainMenu();
            int choice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, 3);
            switch(choice){
                case 0: {
                    usersController_.logout();
                    break mainPageLoop;
                }
                case 1:
                {
                    PostView postView = new PostView();
                    postView.chooseOptionsMainPostPageIfAdmin();
                    break;
                }
                case 2:
                {
                    FollowerView followerView = new FollowerView();
                    followerView.chooseOptionMainFollowPageIfAdmin();
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
    public void performActionOnUserAccounts(){
        mainAccountActionPageLoop:
        while(true){
            printAccountActionMainMenuPageForAdmin();
            int choice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, 6);
            switch(choice){
                case 0: {
                    Main.LOGGER.info("Back to previous menu");
                    break mainAccountActionPageLoop;
                }
                case 1:
                {
                    List<Users> allTheUserRoleAccounts = usersController_.getListOfAllUserAcconts();
                    if (allTheUserRoleAccounts == null){
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
                    boolean isUpdatedTheRoleOfTheUser = usersController_.updateUserRole(UsersController.getUsers_().getUserId_(), targetUserId, "admin");

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
                    List<Users> allTheAdminRoleAccounts = usersController_.listAllAdminAccountsWithoutMe();

                    if(allTheAdminRoleAccounts == null){
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
                    boolean isUpdatedTheRoleOfTheUser = usersController_.updateUserRole(UsersController.getUsers_().getUserId_(), targetUserId, "user");

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
                    List<Users> allUserAccounts = usersController_.listAllAccountsWithoutMe();

                    if(allUserAccounts == null){
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
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    boolean  isAccountRemoveInterrupted = usersController_.removeUserAccount(targetUserId);

                    if(isAccountRemoveInterrupted){
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
                    List<Users> allUserAccounts = usersController_.listAllAccountsWithoutMe();

                    if(allUserAccounts == null){
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

                    boolean isUserAccountBlocked = usersController_.blockUnblockUserAccount(UsersController.getUsers_().getUserId_(), targetUserId, 'y');

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
                    List<Users> blockedUsersList = usersController_.getListOfBlockedUsers();

                    if(blockedUsersList == null){
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Below are the list of all the users in the application: ");
                    blockedUsersList.forEach(e -> System.out.println("User id: " + e.getUserId_() + " :-> " + "user name: " + e.getUserName_()));
                    System.out.println("Choose the user account id to unblock the account:(press '0' to get back to previous menu): ");

                    int maxUserId = blockedUsersList.stream().map(Users::getUserId_).max(Integer::compareTo).get();
                    int targetUserId = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, maxUserId);
                    if(targetUserId == 0){
                        Main.LOGGER.info("Back to previous menu");
                        break;
                    }

                    boolean isUserAccountBlocked = usersController_.blockUnblockUserAccount(UsersController.getUsers_().getUserId_(), targetUserId, 'n');

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
                    List<Users> allUserAccounts = usersController_.listAllAccountsWithoutMe();

                    if(allUserAccounts == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    allUserAccounts.add(UsersController.getUsers_());

                    System.out.println("Below are the list of all the users in the application: ");
                    allUserAccounts.forEach(e -> System.out.println("User id: " + e.getUserId_() + " :-> " + "user name: " + e.getUserName_()));
                    System.out.println("Choose the user account id to change the account password:(press '0' to get back to previous menu): ");

                    int maxUserId = allUserAccounts.stream().map(Users::getUserId_).max(Integer::compareTo).get();
                    int targetUserId = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, maxUserId);
                    if(targetUserId == 0){
                        Main.LOGGER.info("Back to previous menu");
                        break;
                    }

                    if(usersController_.changePassword(targetUserId)){
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.LOGGER.info("Successfully changed the password");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();;
                    break;
                }
            }
        }
    }

    public void printPasswordCriteria(){
        System.out.println("Minimum length of 8 characters\n" +
                "At least one lowercase letter\n" +
                "At least one uppercase letter\n" +
                "At least one special character(Allowed special Characters: @$!%*?&)\n" +
                "At least one number");
    }

    public void printMainMenu(){
        System.out.println("Choose below option\n" +
                "\t1. Posts page\n" +
                "\t2. People page\n" +
                "\t0. Logout");
    }

    public void printAdminUserMainMenu(){
        System.out.println("Choose below options\n" +
                "\t1. Posts Page\n" +
                "\t2. People Page\n" +
                "\t3. Account Action Page\n" +
                "\t0. Logout");
    }



    public void printAccountActionMainMenuPageForAdmin(){
        System.out.println("Choose the below options: \n" +
                "\t1. Make the user as admin\n" +
                "\t2. Make the admin as user\n" +
                "\t3. Remove user account\n" +
                "\t4. Block user account\n" +
                "\t5. Un Block user account\n" +
                "\t6. Change password for user account\n" +
                "\t0. Back to previous page");
    }
}
