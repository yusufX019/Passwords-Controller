
/*
    This is password keeper 1.0 concole edition
    ATTENTION , this is not professional softwate and has no encryption
    Author is not responsible of your actions and for your data stored using this software

*/

package com.ilova;
import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;



public class Main {
    private static Scanner userInput        =new Scanner(System.in);                                           //scanner for any porposes
    private static String  userName         =System.getProperty("user.name");                                 // getting user name
    private static String  baseFilePath     ="c:\\Users\\"+userName+"\\password keeper1.0\\sysdat.dat";      //  path to the file ,
    private static String  passwordFilePath ="c:\\Users\\"+userName+"\\password keeper1.0\\baseFile.dat";   //   path to the password file


    public static void main(String[] args) throws IOException {
        checkIfWindows();                   //function that checks name of the operation system
        if(!isInstalled()) {
            installProgramm();
        }
        System.out.println("\tWelcome to the Password Controller 1.0 [console version] programm \n\tthis programm helpes you to store your passwords easily!!!");
        if(!isPasswordInstalled()) {
            setPassword();
        }else {
            passwordRequest();
        }
        showMainMenu();
    }

    private static void passwordRequest() throws IOException {
        BufferedReader passwordFile=new BufferedReader(new FileReader(passwordFilePath));
        String password=passwordFile.readLine();
        int attemptsAmount=0;
        System.out.println("Hello "+userName+"!");
        while (true){
            System.out.println("Type the password:");
            String userPassword=userInput.next();
            if(userPassword.equals(password)){
                break;
            }
            else {
                attemptsAmount++;
            }
            if(attemptsAmount>5)  {
                System.out.println("To much attempts!!!");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.exit(0);
            }
        }
    }


    //Main menu
    private static void showMainMenu() throws IOException {
        System.out.println("1-To create new storage"  +
                           " 2-To Find storage"       +
                           " 4-To List all storages:" +
                           " 5-To exit( ");
        byte command=0;
        //try to get user input
        try {
            command=userInput.nextByte();
        }
        //if user accidently enters a non numeric value
        catch (InputMismatchException e) {
            System.out.println("Type numeric value please!");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            showMainMenu();
        }

        switch (command){
            case 1:
                createStorage();
                break;
            case 2:
                findStorage();
                break;
            case 3:
                showAllStorages();
                break;
            default:
                showMainMenu();
                break;
        }

    }

    private static void showAllStorages() throws IOException {
        BufferedReader baseFile= null;      //creating reder from the file
        //try to open file
        try {
            baseFile = new BufferedReader(new FileReader(baseFilePath));
        }//in future I will try to store errors list with their definitions
        catch (FileNotFoundException e) {
            System.out.println("Error, #001");
        }

        //getting the names of the storages
        String line="0";
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");      //just for decoration
        while ((line=baseFile.readLine())!=null){
            String[] values=line.split(",");
            System.out.println("\t"+values[1]);
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");      //just for decoration
        showMainMenu();                                                                                     //after showing go back to the main menu again
    }


    private static void deleteStorage() {
        //currently not modifiedd
    }

    private static void findStorage() throws IOException {
        System.out.println("Type the storage name:");   String searchingName=userInput.next();

        //try to catch if file is empty , otherwise will face NullPointerException in another function
        if(!emptyStorage()) {
            if (!isExists(searchingName)) {
                System.out.println("Not found");
                findStorage();
            }
        }
        else {
            System.out.println("You dont have any data yet");
            showMainMenu();
        }


        BufferedReader baseFile=null;
        //again try to open file and catching error then printing error number so that i could now excatly where is the problem
        try {
            baseFile=new BufferedReader(new FileReader(baseFilePath));

        } catch (FileNotFoundException e) {
            System.out.println("Error, #002");
            showMainMenu();
        }

        //separating line with commas
        String line="";
        String[] separetedBycomma=null;
        while ((line=baseFile.readLine())!=null) {
            separetedBycomma=line.split(",");
        }

        //getting arrray of names,login,password and id but currently I dont need ids yet
        //separetedBycomma array consist of one big string with each value in new line
        String[] ids        =separetedBycomma[0].split("\n");
        String[] names      =separetedBycomma[1].split("\n");
        String[] logins     =separetedBycomma[2].split("\n");
        String[] passwords  =separetedBycomma[3].split("\n");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");       //just decoration
        for(int i=0;i<searchingName.length();i++){
            if(names[i].equals(searchingName)){
                System.out.println("Name of the storage  :\t"+names    [i]);
                System.out.println("Name of the login    :\t"+logins   [i]);
                System.out.println("Name of the password :\t"+passwords[i]);
                break;
            }
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");       //again decoration


        showMainMenu();                                                                         //go to main menu after finishing
    }

    private static void createStorage() throws IOException {
        //
        System.out.print("Type the site or programm name:");  String name=userInput.next();
        //checking if site exists and file is not empty , otherwise probably will face NullPointerException
        if(!emptyStorage()){
            if( isExists(name)) {
                System.out.println("Storage with this name already exits");
                showMainMenu();
            }
        }
        //get the data from user
        System.out.print("Type the login   :");               String login    =userInput.next();
        System.out.print("Type the password:");               String password =userInput.next();
        //opening file for writing in it
        BufferedWriter baseFile=new BufferedWriter(new FileWriter(baseFilePath,true));

        Site newSite=new Site(password,login,name);     //creating an object of the storage , older name was storage but class stayed unchanged
        baseFile.write(newSite.getId()+",");     baseFile.write(newSite.getName()+",");
        baseFile.write(newSite.getLogIn()+",");  baseFile.write(newSite.getPassword()+"\n");
        baseFile.close();

        System.out.println("Added succsessfully!!!");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        showMainMenu();

    }

    //this function is to check if file is empty , because if other function will place null value inside array , and will get NullPointer
    private static boolean emptyStorage() throws IOException {
        BufferedReader baseFile=null;
        try {
            baseFile=new BufferedReader(new FileReader(baseFilePath));
        }catch (Exception e){
            System.out.println("Error,#003");
            showMainMenu();
        }

        String line="";
        byte linesAmount=0;
        while ((line=baseFile.readLine())!=null){
            linesAmount++;
        }

        if(linesAmount==1)  return true;        //if file contains one line it will be considered as ampty
        else                return false;
    }

    private static boolean isExists(String name) throws IOException {
        //checking if storage exsists
        boolean         siteExist =false;
        BufferedReader  baseFile  =null;
        try {
            baseFile=new BufferedReader(new FileReader(baseFilePath));

        } catch (FileNotFoundException e) {
            System.out.println("Error,#004");
            showMainMenu();
        }

        String line="";

        if((line=baseFile.readLine())==null)    return false;       //if file is empty

        String[] separetedBycomma=null;
        while ((line=baseFile.readLine())!=null) {
            separetedBycomma=(line.split(","));
            System.out.println  (separetedBycomma[0]);
        }


        //getting arrray of names and login and etc

        String[] ids        =separetedBycomma[0].split("\n");
        String[] names      =separetedBycomma[1].split("\n");
        String[] logins     =separetedBycomma[2].split("\n");
        String[] passwords  =separetedBycomma[3].split("\n");

        for(int i=0;i<names.length;i++){
            if(names[i].equals(name)){
                siteExist=true;
                break;
            }
        }


        return siteExist;
    }


    //this function needs to be called only once if second call occures problems may happen to avoid them delete all cashe and restart the programm
    private static void setPassword() throws IOException {
        //this fuction suppose to set up the password but currently not useless
        System.out.println("Would you like to install password for this programm? [1] for yes and [0] for no:");
        short command = 2;  //default 2,same as 0
        try {
            command=userInput.nextByte();

        }catch (Exception e) {
            System.out.println("Please type numeric value");
            setPassword();
        }
        if(command!=1)   showMainMenu();        //basically exits this function

        System.out.println("Type your password");
        String password=userInput.next();

        File passwordFile=new File(passwordFilePath);
        try {
            passwordFile.createNewFile();
            BufferedWriter wPasswordFile=new BufferedWriter(new FileWriter(passwordFile));
            wPasswordFile.write(password);
            wPasswordFile.close();
        } catch (IOException e) {
            System.out.println("Error,#005 ");
            setPassword();
        }

        System.out.println("Password set successfully");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");


        showMainMenu();
    }



    private static void checkIfWindows() {
        //checks the OS
        String[] osName;
        String fullOsName=System.getProperty("os.name").toLowerCase();
        osName=fullOsName.split(" ");       //getProperty("os.name") returns full name of OS(Windows 10 pro) but I just need first word

        if(!osName[0].equals("windows")){
            System.exit(0);
        }
    }




    //this fuction should be called only once ever
    private static void installProgramm() throws IOException {
        //this function just creates all files
        File baseFolder     =new File("c:\\Users\\"+userName+"\\password keeper1.0");
        File baseFile       =new File(baseFilePath);
        BufferedWriter wBaseFile=null;   //writing to the file
        baseFolder.mkdir();         //creating folder
        try{
           baseFile.createNewFile();
           wBaseFile=new BufferedWriter(new FileWriter(baseFilePath,true));
           wBaseFile.write("id,name,login,password\n");
           wBaseFile.close();

        } catch (IOException e) {
            System.out.println("Error,#006");
            showMainMenu();
        }
    }

    private static boolean isInstalled() {
        //checks if all the files exist in this machine
        File baseFile = new File(  baseFilePath);
        if (baseFile.exists()) {
            return true;
        } else return false;
    }

    private static boolean isPasswordInstalled(){
        //just checks if password file exists
        File passwordFile=new File(passwordFilePath);
        if(passwordFile.exists()){
           return true;
        }
        else return false;
    }

}
