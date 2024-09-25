package org.bharath.view;

public class FollowerView {

    public void printFollowerMainMenuPage(){
        System.out.println("Choose the below options: \n" +
                "\t1. View List of all users you are following\n" +
                "\t2. View List of all users you are not following\n" +
                "\t3. View List of all users who are following you\n" +
                "\t4. Follow user\n" +
                "\t5. Unfollow user\n" +
                "\t0. Back to previous page");
    }

    public void printFollowerMainMenuPageIfAdmin(){
        System.out.println("Choose the below options: \n" +
                "\t1. View all the followers of a particular user\n" +
                "\t2. View all the following of a particular user\n" +
                "\t0. Back to previous page");
    }

}
