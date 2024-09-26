/**
 * This is the controller used to handle the request for followers and following people
 */
package org.bharath.controller;

import org.bharath.Main;
import org.bharath.dao.FollowerDaoImpl;
import org.bharath.dao.UsersDaoImpl;
import org.bharath.model.Users;
import org.bharath.utils.DBConnection;

import java.sql.Connection;
import java.util.List;


public class FollowerController {
    private static final Connection CONNECTION = DBConnection.getConnection();
    private static final FollowerDaoImpl FOLLOWER_DAO = FollowerDaoImpl.getInstance(CONNECTION);
    private static final UsersDaoImpl USERS_DAO_IMP = UsersDaoImpl.getInstance(CONNECTION);

    public FollowerController(){}

    public List<Users> listAllUsersWithoutMe(int userId){
        List<Users> allUserAccounts = USERS_DAO_IMP.listAllUsers();
        if(allUserAccounts == null){
            Main.LOGGER.warning("Something went wrong");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return null;
        }

        allUserAccounts.removeIf(e -> e.getUserId_() == userId);

        if(allUserAccounts.isEmpty()){
            Main.LOGGER.warning("You are the only user in the account");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return null;
        }
        return allUserAccounts;
    }

    public boolean unfollowUser(int userId, int targetId){
        return FOLLOWER_DAO.unFollowUser(userId, targetId);
    }

    public boolean followUser(int userId, int targetId){
        return FOLLOWER_DAO.followUser(userId, targetId);
    }

    public List<Users> getListOfAllUsersFollowingYou(int userId){
        return FOLLOWER_DAO.listOfAllUsersFollowingYou(userId);
    }

    public List<Users> getListOfAllUsersThatWeAreNotFollowing(int userId){
        return FOLLOWER_DAO.listAllNotFollowedUsers(userId);
    }

    public List<Users> getListOfAllFollowedUsers(int userId){
        return FOLLOWER_DAO.listAllFollowedUsers(userId);
    }

}
