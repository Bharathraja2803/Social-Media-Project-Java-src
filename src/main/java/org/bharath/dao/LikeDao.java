package org.bharath.dao;

import org.bharath.Main;
import org.bharath.model.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LikeDao {
    private static LikeDao likeDao_ = null;
    private static Connection connection_ = null;

    private LikeDao(Connection connection){
        connection_ = connection;
    }

    public boolean likeThePost(int userId, int postId){
        Like like = getLike(userId, postId);
        if(like != null){
            Main.LOGGER.warning("You have already liked the post");
            return false;
        }

        try {
            PreparedStatement addLikeEntryToPost = connection_.prepareStatement("INSERT INTO likes (like_id, user_id, post_id, like_date, like_time) VALUES (nextVal('like_id_sequence'), ?, ?, CURRENT_DATE, CURRENT_TIME)");
            addLikeEntryToPost.setInt(1, userId);
            addLikeEntryToPost.setInt(2, postId);
            addLikeEntryToPost.execute();
            Main.LOGGER.info("You have liked the record");
            return true;
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return false;
        }

    }

    public List<Like> getAllLikesForThePost(int postId){
        List<Like> likeList = new ArrayList<>();
        try {
            PreparedStatement selectQueryToFetchAllLikesForThePost = connection_.prepareStatement("select * from likes where post_id = ?");
            selectQueryToFetchAllLikesForThePost.setInt(1, postId);
            ResultSet resultSet = selectQueryToFetchAllLikesForThePost.executeQuery();
            while(resultSet.next()){
                Like like = new Like();
                like.setLikeId(resultSet.getInt("like_id"));
                like.setUserId(resultSet.getInt("user_id"));
                like.setPostId(resultSet.getInt("post_id"));
                like.setLikeDate(resultSet.getDate("like_date").toLocalDate());
                like.setLikeTime(resultSet.getTime("like_time").toLocalTime());
                likeList.add(like);
            }

            if(likeList.isEmpty()){
                Main.LOGGER.warning("No likes received for the post with post id: " + postId);
                Main.CUSTOM_USER_INPUT.printSeparatorLine();
                return null;
            }
            Main.LOGGER.info("Fetched all likes for the post with post id: " + postId);
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return likeList;
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return null;
        }
    }

    public boolean removeLike(int likeId){
        try {
            PreparedStatement removeLikeQuery = connection_.prepareStatement("delete from likes where like_id = ?");
            removeLikeQuery.setInt(1, likeId);
            removeLikeQuery.execute();
            Main.LOGGER.info(String.format("The like id %d is removed for the post: ", likeId));
            return true;
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return false;
        }

    }

    public boolean removeAllLikesByUserId(int userId){
        UsersDaoImp usersDaoImp = UsersDaoImp.getInstance(connection_);
        boolean isValidUser = usersDaoImp.isUserIdExits(userId);
        if(!isValidUser){
            Main.LOGGER.warning("User is invalid");
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            return false;
        }

        try {
            PreparedStatement deleteQueryToRemoveLikesByUser = connection_.prepareStatement("delete from likes where user_id = ?");
            deleteQueryToRemoveLikesByUser.setInt(1, userId);
            deleteQueryToRemoveLikesByUser.execute();
            Main.LOGGER.info("Successfully deleted the likes using userid");
            return true;
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return false;
        }

    }
    public boolean removeAllLikeForSpecificPost(int postId){
        List<Like> likeList = getAllLikesForThePost(postId);

        if(likeList == null){
            Main.LOGGER.info("No likes to delete for deleting the entire post");
            return true;
        }

        try {
            PreparedStatement removeAllLikedByPostId = connection_.prepareStatement("delete from likes where post_id = ?");
            removeAllLikedByPostId.setInt(1, postId);
            removeAllLikedByPostId.execute();
            Main.LOGGER.info("All liked were removed for the post");
            return true;
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return false;
        }

    }

    public Like getLike(int userId, int postId){
        try {
            PreparedStatement selectQueryForLikeId = connection_.prepareStatement("select * from likes where user_id = ? and post_id = ?");
            selectQueryForLikeId.setInt(1, userId);
            selectQueryForLikeId.setInt(2, postId);
            ResultSet resultSet = selectQueryForLikeId.executeQuery();
            if(!resultSet.next()){
                return null;
            }
            Like like = new Like();
            like.setUserId(userId);
            like.setPostId(postId);
            like.setLikeDate(resultSet.getDate("like_date").toLocalDate());
            like.setLikeTime(resultSet.getTime("like_time").toLocalTime());
            like.setLikeId(resultSet.getInt("like_id"));
            return like;
        } catch (SQLException e) {
            Main.LOGGER.severe(e.toString());
            return null;
        }
    }

    public static LikeDao getInstance(Connection connection){
        if(likeDao_ == null){
            likeDao_ = new LikeDao(connection);
        }
        return likeDao_;
    }
}
