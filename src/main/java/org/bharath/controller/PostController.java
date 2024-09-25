/**
 * This is the controller used to handle the post page requests
 */
package org.bharath.controller;

import org.bharath.Main;
import org.bharath.dao.*;
import org.bharath.model.Comment;
import org.bharath.model.Like;
import org.bharath.model.Post;
import org.bharath.model.Users;
import org.bharath.utils.DBConnection;
import org.bharath.utils.MyIterator;
import org.bharath.view.PostView;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class PostController {
    private static final Connection CONNECTION = DBConnection.getConnection();
    private static final PostDao POST_DAO = PostDao.getInstance(CONNECTION);
    private static final PostView POST_VIEW = new PostView();
    private static Users users_ = null;
    private static LikeDao likeDao_ = LikeDao.getInstance(CONNECTION);
    private static CommentDao commentDao_ = CommentDao.getInstance(CONNECTION);
    private static FollowerDao followerDao_ = FollowerDao.getInstance(CONNECTION);
    private static UsersDaoImp usersDaoImp_ = UsersDaoImp.getInstance(CONNECTION);

    public PostController(Users users){
        PostController.users_ = users;
    }



    /**
     * This method performs the action related to post by the normal user
     *                 "\t1. Create Post\n" +
     *                 "\t2. View all My Post one by one\n" +
     *                 "\t3. View all Friends Post one by one\n" +
     *                 "\t0. Back to previous page
     */
    public void chooseOptionsMainPostPage(){

        postMainMethodLoop:
        while(true){
            POST_VIEW.printPostMainPageMenu();
            int choice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0,3);
            switch(choice){
                case 0:
                {
                    Main.LOGGER.info("Back to previous menu");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break postMainMethodLoop;
                }
                case 1:
                {
                    System.out.println("Enter the post content:(enter 'q' to stop drafting content) ");
                    StringBuilder postContent = new StringBuilder();
                    while(true){
                        String line = Main.CUSTOM_USER_INPUT.scanner.nextLine();
                        if(line.equals("q")){
                            break;
                        }
                        postContent.append(line);
                    }
                    if(postContent.toString().isEmpty()){
                        Main.LOGGER.warning("Post content is empty!");
                        break;
                    }
                    POST_DAO.createPost(postContent.toString(), users_.getUserId_());
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 2:
                {
                    List<Post> allMyPost = POST_DAO.getAllMyPost(users_.getUserId_());
                    if(allMyPost == null){
                        Main.LOGGER.warning("Something went wrong in fetching our own post");
                        break;
                    }
                    MyIterator<Post> myPostIterator = new MyIterator<>(allMyPost.listIterator());
                    Post currentPost = myPostIterator.next();

                    postIteratorAndActioner(currentPost, myPostIterator, true);
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 3:
                {
                    List<Users> listOfAllFollowedUsers = followerDao_.listAllFollowedUsers(users_.getUserId_());

                    if(listOfAllFollowedUsers == null){
                        Main.LOGGER.warning("Something went wrong!");
                        break;
                    }

                    List<Post> allFollowersPost = new ArrayList<>();
                    for(Users users : listOfAllFollowedUsers){
                        List<Post> allMyPostPerUser = POST_DAO.getAllMyPost(users.getUserId_());
                        if(allMyPostPerUser == null){
                            Main.LOGGER.warning("Something went wrong in fetching our own post");
                            continue;
                        }
                        allFollowersPost.addAll(allMyPostPerUser);
                    }

                    if(allFollowersPost.isEmpty()){
                        Main.LOGGER.warning("No post posted by our followers");
                        break;
                    }

                    allFollowersPost.sort(new Comparator<Post>() {
                        @Override
                        public int compare(Post post, Post t1) {
                            return LocalDateTime.of(post.getPostedDate(), post.getPostedTime()).compareTo(LocalDateTime.of(t1.getPostedDate(), t1.getPostedTime()));
                        }
                    });

                    MyIterator<Post> myPostIterator = new MyIterator<>(allFollowersPost.listIterator());
                    Post currentPost = myPostIterator.next();

                    postIteratorAndActioner(currentPost, myPostIterator, false);
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
            }
        }
    }



    /**
     * This method performs the action related to post by the admin user
     *                  "\t1. Create Post\n" +
     *                 "\t2. View all My Post one by one\n" +
     *                 "\t3. View all others Post one by one\n" +
     *                 "\t0. Back to previous page"
     */
    public void chooseOptionsMainPostPageIfAdmin(){

        postMainMethodLoop:
        while(true){
            POST_VIEW.printPostMainPageMenuForAdmin();
            int choice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0,3);
            switch(choice){
                case 0:
                {
                    Main.LOGGER.info("Back to previous menu");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break postMainMethodLoop;
                }
                case 1:
                {
                    System.out.println("Enter the post content:(enter 'q' to stop drafting content) ");
                    StringBuilder postContent = new StringBuilder();
                    while(true){
                        String line = Main.CUSTOM_USER_INPUT.scanner.nextLine();
                        if(line.equals("q")){
                            break;
                        }
                        postContent.append(line);
                    }
                    if(postContent.toString().isEmpty()){
                        Main.LOGGER.warning("Post content is empty!");
                        break;
                    }
                    POST_DAO.createPost(postContent.toString(), users_.getUserId_());
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 2:
                {
                    List<Post> allMyPost = POST_DAO.getAllMyPost(users_.getUserId_());
                    if(allMyPost == null){
                        Main.LOGGER.warning("Something went wrong in fetching our own post");
                        break;
                    }
                    MyIterator<Post> myPostIterator = new MyIterator<>(allMyPost.listIterator());
                    Post currentPost = myPostIterator.next();

                    postIteratorAndActioner(currentPost, myPostIterator, true);
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 3:
                {

                    List<Users> listOfAllUsers = usersDaoImp_.listAllUsers();

                    if(listOfAllUsers == null){
                        Main.LOGGER.warning("Something went wrong!");
                        break;
                    }

                    List<Post> allFollowersPost = new ArrayList<>();
                    for(Users users : listOfAllUsers){
                        List<Post> allMyPostPerUser = POST_DAO.getAllMyPost(users.getUserId_());
                        if(allMyPostPerUser == null){
                            Main.LOGGER.warning("Something went wrong in fetching all users post");
                            continue;
                        }
                        allFollowersPost.addAll(allMyPostPerUser);
                    }

                    if(allFollowersPost.isEmpty()){
                        Main.LOGGER.warning("No post posted by any user");
                        break;
                    }

                    allFollowersPost.sort(new Comparator<Post>() {
                        @Override
                        public int compare(Post post, Post t1) {
                            return LocalDateTime.of(post.getPostedDate(), post.getPostedTime()).compareTo(LocalDateTime.of(t1.getPostedDate(), t1.getPostedTime()));
                        }
                    }.reversed());

                    MyIterator<Post> myPostIterator = new MyIterator<>(allFollowersPost.listIterator());
                    Post currentPost = myPostIterator.next();

                    postIteratorAndActioner(currentPost, myPostIterator, true);
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
            }
        }
    }


    /**
     * This method performs iteration action and like comment action on the post
     *                 "\t1. Like the post\n" +
     *                 "\t2. Comment the post\n" +
     *                 "\t3. View Likes for the post\n" +
     *                 "\t4. View Comments for the post\n" +
     *                 "\t5. Delete this post\n" +
     *                 "\t6. Next Post\n" +
     *                 "\t7. Previous Post\n" +
     *                 "\t0. Back to previous menu");
     * @param currentPost -> current post in iterator
     * @param myPostIterator -> iterator for iterating
     * @param isMySelf -> checks true if the post belongs to me
     */
    private static void postIteratorAndActioner(Post currentPost, MyIterator<Post> myPostIterator, boolean isMySelf) {
        myPostIteratorLoop:
        while(true){
            System.out.println("Current Post: ");
            System.out.println(currentPost);
            Main.CUSTOM_USER_INPUT.printSeparatorLine();
            POST_VIEW.printPostIteratorMenu();
            int postIteratorChoice = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0,7);
            switch (postIteratorChoice){
                case 0:
                {
                    Main.LOGGER.info("Back to previous ");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break myPostIteratorLoop;
                }
                case 1:
                {
                    Like like = likeDao_.getLike(users_.getUserId_(), currentPost.getPostId());
                    if(like == null){
                        boolean isLikeActionDone = likeDao_.likeThePost(users_.getUserId_(), currentPost.getPostId());
                        if(isLikeActionDone){
                            Main.LOGGER.info("Like action completed successfully");
                        }else{
                            Main.LOGGER.warning("Something went wrong in liking the post!");
                        }
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("You have already liked the post");
                    System.out.println("Choose below options\n" +
                            "\tpress 1 -> to unlike the post\n" +
                            "\tpress 0 -> Do not take any action");
                    int choiceOfAction = Main.CUSTOM_USER_INPUT.validateAndGetIntegerInput(0,1);
                    switch (choiceOfAction){
                        case 0:{
                            Main.LOGGER.info("Action not performed on the post");
                            Main.CUSTOM_USER_INPUT.printSeparatorLine();
                            break;
                        }
                        case 1:
                        {
                            boolean isUnlikeDone = likeDao_.removeLike(like.getLikeId());
                            if(isUnlikeDone){
                                Main.LOGGER.info("Unlike done");
                                Main.CUSTOM_USER_INPUT.printSeparatorLine();
                                break;
                            }
                            Main.LOGGER.warning("Something went wrong in unliking the post");
                            Main.CUSTOM_USER_INPUT.printSeparatorLine();
                            break;
                        }
                    }
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 2:
                {
                    System.out.println("Enter the comment for the post: (press 'q' to quit from comment drafting)");
                    StringBuilder commentText = new StringBuilder();
                    while(true){
                        String line = Main.CUSTOM_USER_INPUT.scanner.nextLine();
                        if(line.equals("q")){
                            break;
                        }
                        commentText.append(line);
                    }

                    if(commentText.toString().isEmpty()){
                        Main.LOGGER.warning("Comments entered is empty");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    boolean isCommentedSuccessfully = commentDao_.commentThePost(users_.getUserId_(), currentPost.getPostId(), commentText.toString());
                    if(!isCommentedSuccessfully){
                        Main.LOGGER.warning("Something went wrong in commenting the post with post id: " + currentPost.getPostId());
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.LOGGER.info("Added comment successfully");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 3:
                {
                    List<Like> likeList = likeDao_.getAllLikesForThePost(currentPost.getPostId());

                    if(likeList == null){
                        Main.LOGGER.warning("Something went wrong in fetching all the liked for the post");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Total number of likes for the post is: " + likeList.size());
                    System.out.println("Below are the peoples who liked the post: ");
                    likeList.forEach(e -> System.out.println("User id: "+e.getUserId() + " :-> liked the post at " + e.getLikeDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy") ) +" "+ e.getLikeTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 4:
                {
                    List<Comment> commentList = commentDao_.getAllCommentsForThePost(currentPost.getPostId());

                    if(commentList == null){
                        Main.LOGGER.warning("Something went wrong in fetching all the comments for the post");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    System.out.println("Total number of comments for the post is: " + commentList.size());
                    System.out.println("Below are the peoples who commented the post: ");
                    commentList.forEach(e -> System.out.println("User id: "+e.getCommentUserId() + " :-> commented the post at " + e.getCommentDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy") ) +" "+ e.getCommentTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) +"\n Comment: " + e.getCommentText()));
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 5:
                {
                    if(!isMySelf){
                        Main.LOGGER.warning("You are not supposed to delete someone else's post");
                        break;
                    }
                    boolean isCommentsDeleted = commentDao_.deleteAllCommentsForThePost(currentPost.getPostId());
                    if(!isCommentsDeleted){
                        Main.LOGGER.warning("Something went wrong in deleting the comments!");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    boolean isLikesDeleted = likeDao_.removeAllLikeForSpecificPost(currentPost.getPostId());
                    if(!isLikesDeleted){
                        Main.LOGGER.warning("Something went wrong in deleting all the likes of the user!");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    boolean isPostDeleted = POST_DAO.removePost(currentPost.getPostId());
                    boolean isPostRemovedFromIterator = myPostIterator.remove();

                    if(isPostDeleted && isPostRemovedFromIterator){
                        Main.LOGGER.info("Removing post is successful");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.LOGGER.warning("Something went wrong in removing the post");
                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 6:
                {
                    try{
                        currentPost = myPostIterator.next();
                        Main.LOGGER.info("Moved to next post");
                    }catch(RuntimeException e){
                        Main.LOGGER.severe(e.toString());
                        Main.LOGGER.warning("Reached end of the post");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
                case 7:
                {
                    try{
                        currentPost = myPostIterator.previous();
                        Main.LOGGER.info("Moved to previous post");
                    }catch(RuntimeException e){
                        Main.LOGGER.severe(e.toString());
                        Main.LOGGER.warning("Reached first of the post");
                        Main.CUSTOM_USER_INPUT.printSeparatorLine();
                        break;
                    }

                    Main.CUSTOM_USER_INPUT.printSeparatorLine();
                    break;
                }
            }
        }
    }


}
