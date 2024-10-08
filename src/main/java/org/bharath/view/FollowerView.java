/**
 * This is a view for follower page to print the menu
 */
package org.bharath.view;

import org.bharath.Main;
import org.bharath.controller.FollowerController;
import org.bharath.controller.UsersController;
import org.bharath.model.Users;

import java.util.Comparator;
import java.util.List;

public class FollowerView {
    private final FollowerController followerController_ = new FollowerController();
    public void printFollowerMainMenuPage(){
        System.out.println("Choose the below options: \n" +
                "\t1. View List of all users you are following\n" +
                "\t2. View List of all users you are not following\n" +
                "\t3. View List of all users who are following you\n" +
                "\t4. Follow user\n" +
                "\t5. Unfollow user\n" +
                "\t0. Back to previous page");
    }
    /**
     * This method performs the action related to post by the normal user
     *                 "\t1. View List of all users you are following\n" +
     *                 "\t2. View List of all users you are not following\n" +
     *                 "\t3. View List of all users who are following you\n" +
     *                 "\t4. Follow user\n" +
     *                 "\t5. Unfollow user\n" +
     *                 "\t0. Back to previous page"
     */
    public void chooseOptionMainFollowPage(){
        followerMainMethodLoop:
        while(true) {
            printFollowerMainMenuPage();
            int choice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, 5);
            switch (choice) {
                case 0: {
                    Main.LOGGER.info("Back to previous menu");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break followerMainMethodLoop;
                }
                case 1:
                {
                    List<Users> listOfAllFollowedUsers = followerController_.getListOfAllFollowedUsers(UsersController.getUsers_().getUserId_());

                    if(listOfAllFollowedUsers == null ){
                        Main.LOGGER.warning("You haven't followed any users yet!");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println(String.format("You are following %d users in total", listOfAllFollowedUsers.size()));
                    System.out.println("Below are the list of user details that you are following!");
                    listOfAllFollowedUsers.forEach(e -> System.out.println(String.format("User id: %d :-> User name: %s", e.getUserId_(), e.getUserName_())));
                    Main.LOGGER.info("Successfully printed all the following list");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 2:
                {
                    List<Users> listOfAllUsersThatYouAreNotFollowing = followerController_.getListOfAllUsersThatWeAreNotFollowing(UsersController.getUsers_().getUserId_());

                    if(listOfAllUsersThatYouAreNotFollowing == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Below are the list of user details that you are not following!");
                    listOfAllUsersThatYouAreNotFollowing.forEach(e -> System.out.println(String.format("User id: %d :-> User name: %s", e.getUserId_(), e.getUserName_())));
                    Main.LOGGER.info("Successfully printed all the not following list");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 3:
                {
                    List<Users> listOfAllUsersWhoAreFollowingYou = followerController_.getListOfAllUsersFollowingYou(UsersController.getUsers_().getUserId_());

                    if(listOfAllUsersWhoAreFollowingYou == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println(String.format("There were %d users following you", listOfAllUsersWhoAreFollowingYou.size()));
                    System.out.println("Below are the list of users following you!");
                    listOfAllUsersWhoAreFollowingYou.forEach(e -> System.out.println(String.format("User id: %d :-> User name: %s", e.getUserId_(), e.getUserName_())));
                    Main.LOGGER.info("Successfully printed all the users following you");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;

                }
                case 4:
                {
                    List<Users> listOfAllUsersThatYouAreNotFollowing = followerController_.getListOfAllUsersThatWeAreNotFollowing(UsersController.getUsers_().getUserId_());

                    if(listOfAllUsersThatYouAreNotFollowing == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Below are the list of user details that you are not following!");
                    listOfAllUsersThatYouAreNotFollowing.forEach(e -> System.out.println(String.format("User id: %d :-> User name: %s", e.getUserId_(), e.getUserName_())));
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    System.out.println("Enter the User Id you need to follow (press 0 to go to previous menu): ");
                    int maxUserId = listOfAllUsersThatYouAreNotFollowing.stream().map(e -> e.getUserId_()).max(new Comparator<Integer>() {
                        @Override
                        public int compare(Integer integer, Integer t1) {
                            return integer.compareTo(t1);
                        }
                    }).get();

                    int userIdOfFollowing = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, maxUserId);

                    if(userIdOfFollowing == 0){
                        Main.LOGGER.info("Back to previous menu");
                        break;
                    }

                    if(userIdOfFollowing == UsersController.getUsers_().getUserId_()){
                        Main.LOGGER.warning("You cannot follow yourself");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    boolean isSuccessfullyFollowed = followerController_.followUser(UsersController.getUsers_().getUserId_(), userIdOfFollowing);
                    if(!isSuccessfullyFollowed){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.LOGGER.info("Successfully followed the user id: " + userIdOfFollowing);
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 5:
                {
                    List<Users> listOfFollowingUsers = followerController_.getListOfAllFollowedUsers(UsersController.getUsers_().getUserId_());

                    if(listOfFollowingUsers == null ){
                        Main.LOGGER.warning("You haven't followed any users yet!");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    System.out.println("Below are the list of users that you are following: ");
                    listOfFollowingUsers.forEach(e -> System.out.println(String.format("User id: %d :-> User name: %s", e.getUserId_(), e.getUserName_())));

                    int maximumUserIdThatYouAreFollowing = listOfFollowingUsers.stream().map(e -> e.getUserId_()).max(new Comparator<Integer>() {
                        @Override
                        public int compare(Integer integer, Integer t1) {
                            return integer.compareTo(t1);
                        }
                    }).get();
                    System.out.println("Enter the user id you need to unfollow: (Press '0' to go to previous menu)");
                    int userIdToUnFollow = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, maximumUserIdThatYouAreFollowing);

                    if(userIdToUnFollow == 0){
                        Main.LOGGER.info("Back to previous menu");
                        break;
                    }

                    if(userIdToUnFollow == UsersController.getUsers_().getUserId_()){
                        Main.LOGGER.warning("You cannot unfollow yourself");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    boolean isUnfollowUserSuccessFul = followerController_.unfollowUser(UsersController.getUsers_().getUserId_(), userIdToUnFollow);

                    if(!isUnfollowUserSuccessFul){
                        Main.LOGGER.warning("Something went wrong!");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.LOGGER.info("Unfollow is successful");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
            }
        }
    }

    /**
     * This method performs the action related to post by the admin user
     *                 "\t1. View all the followers of a particular user\n" +
     *                 "\t2. View all the following of a particular user\n" +
     *                 "\t0. Back to previous page"
     */
    public void chooseOptionMainFollowPageIfAdmin(){
        followerMainMethodLoop:
        while(true) {
            printFollowerMainMenuPageIfAdmin();
            int choice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0, 2);
            switch (choice) {
                case 0: {
                    Main.LOGGER.info("Back to previous menu");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break followerMainMethodLoop;
                }
                case 1:
                {
                    List<Users> allUserAccounts = followerController_.listAllUsersWithoutMe(UsersController.getUsers_().getUserId_());

                    if(allUserAccounts == null){
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

                    List<Users> listOfAllUsersFollowingByTarget = followerController_.getListOfAllFollowedUsers(targetUserId);

                    if(listOfAllUsersFollowingByTarget == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Below are the list of users followed by the user with id: " + targetUserId);
                    listOfAllUsersFollowingByTarget.forEach(e -> System.out.println("user id: " + e.getUserId_() + " :-> user name: " + e.getUserName_()));
                    Main.LOGGER.info("Successfully fetched the users followed by targeted user");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 2:
                {
                    List<Users> allUserAccounts = followerController_.listAllUsersWithoutMe(UsersController.getUsers_().getUserId_());

                    if(allUserAccounts == null){
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

                    List<Users> listOfAllUsersFollowingTheTargetUser = followerController_.getListOfAllUsersFollowingYou(targetUserId);

                    if(listOfAllUsersFollowingTheTargetUser == null){
                        Main.LOGGER.warning("Something went wrong");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Below are the list of users following the target user with id: " + targetUserId);
                    listOfAllUsersFollowingTheTargetUser.forEach(e -> System.out.println("user id: " + e.getUserId_() + " :-> user name: " + e.getUserName_()));
                    Main.LOGGER.info("Successfully fetched the users following the targeted user");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }

            }
        }
    }

    public void printFollowerMainMenuPageIfAdmin(){
        System.out.println("Choose the below options: \n" +
                "\t1. View all the followers of a particular user\n" +
                "\t2. View all the following of a particular user\n" +
                "\t0. Back to previous page");
    }

}
