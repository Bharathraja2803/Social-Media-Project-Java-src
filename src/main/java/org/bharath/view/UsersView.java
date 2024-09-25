/**
 * This is a view for user page to print the menu
 */
package org.bharath.view;

public class UsersView {
    public void printAuthenticationOptionMenu(){
        System.out.println("Hi!.. Welcome to ABC Social Media\n" +
                "Authentication Page\n" +
                "Choose below option\n" +
                "\t1. Login\n" +
                "\t2. Sign up\n" +
                "\t3. Forget Password\n" +
                "\t0. Quit the application\n");
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
