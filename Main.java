
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
    private static Scanner userInput =new Scanner(System.in);            //scanner for any porposes
    private static String  userName  =System.getProperty("user.name");     //getting user name


    public static void main(String[] args) throws IOException {
        checkIfWindows();
        if(!isInstalled()) {
            installProgramm();
        }
        System.out.println("Welcome to the Password Keeper1.0 programm , this programm helpes you to store your passwords easily and get access to them!!!");
        if(!isPasswordInstalled()) {
            setPassword();
        }
        showMainMenu();
    }


    //Main menu
    private static void showMainMenu() throws IOException {
        System.out.println("1-To create new storage" +
                           " 2-To Find storage" +
                           " 3-To Delete storage" +
                           " 4-To List all storages:");
        byte command=0;
        try {
            command=userInput.nextByte();
        }catch (InputMismatchException e) {
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
                deleteStorage();
                break;
            case 4:
                showAllStorages();
                break;
            default:
                showMainMenu();
                break;
        }

    }

    private static void showAllStorages() throws IOException {
        BufferedReader baseFile=new BufferedReader(new FileReader("c:\\Users\\"+userName+"\\password keeper1.0\\baseFile.dat"));
        String line="0";
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        while ((line=baseFile.readLine())!=null){
            String[] values=line.split(",");
            System.out.println(values[1]);
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        showMainMenu();
    }


    private static void deleteStorage() {
        //currently not modifiedd
    }

    private static void findStorage() throws IOException {
        //
        System.out.println("Type the storage name:");   String searchingName=userInput.next();
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
        try {
            baseFile=new BufferedReader(new FileReader("c:\\Users\\"+userName+"\\password keeper1.0\\baseFile.dat"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //separating line by commas
        String line="";
        String[] separetedBycomma=null;
        while ((line=baseFile.readLine())!=null) {
            separetedBycomma=line.split(",");
        }

        //getting arrray of names and login and etc
        String[] ids        =separetedBycomma[0].split("\n");
        String[] names      =separetedBycomma[1].split("\n");
        String[] logins     =separetedBycomma[2].split("\n");
        String[] passwords  =separetedBycomma[3].split("\n");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        for(int i=0;i<searchingName.length();i++){
            if(names[i].equals(searchingName)){
                System.out.println("Name of the storage  :\t"+names    [i]);
                System.out.println("Name of the login    :\t"+logins   [i]);
                System.out.println("Name of the password :\t"+passwords[i]);
                break;
            }
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");


        showMainMenu();
    }

    private static void createStorage() throws IOException {
        //
        System.out.print("Type the site or programm name:");  String name=userInput.next();
        //checking if site exists
        if(!emptyStorage()){
            if( isExists(name)) {
                System.out.println("Storage with this name already exits");
                showMainMenu();
            }
        }

        System.out.print("Type the login   :");               String login    =userInput.next();
        System.out.print("Type the password:");               String password =userInput.next();
        BufferedWriter baseFile=new BufferedWriter(new FileWriter("c:\\Users\\"+userName+"\\password keeper1.0\\baseFile.dat",true));
        Site newSite=new Site(password,login,name);     //creating an object
        baseFile.write(newSite.getId()+",");     baseFile.write(newSite.getName()+",");
        baseFile.write(newSite.getLogIn()+",");  baseFile.write(newSite.getPassword()+"\n");
        baseFile.close();

        System.out.println("Added succsessfully!!!");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        showMainMenu();

    }

    private static boolean emptyStorage() throws IOException {
        BufferedReader baseFile=null;
        try {
            baseFile=new BufferedReader(new FileReader("c:\\Users\\"+userName+"\\password keeper1.0\\baseFile.dat"));
        }catch (Exception e){
            System.out.println("Enable to create file");
        }
        String line="";
        byte linesAmount=0;
        while ((line=baseFile.readLine())!=null){
            linesAmount++;
        }

        if(linesAmount==1)  return true;
        else                return false;
    }

    private static boolean isExists(String name) throws IOException {

        boolean         siteExist =false;
        BufferedReader  baseFile  =null;
        try {
            baseFile=new BufferedReader(new FileReader("c:\\Users\\"+userName+"\\password keeper1.0\\baseFile.dat"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //separating line by commas
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
        System.out.println("Would you like to install password for this programm? [1] for yes and [0] for no:");
        short command = 2;  //default 2,same as 0
        try {
            command=userInput.nextByte();

        }catch (Exception e) {
            System.out.println("Please type numeric value");
            setPassword();
        }
        if(command>1)   showMainMenu();
        System.out.println("Type your password");
        String password=userInput.next();

        File passwordFile=new File("c:\\Users\\"+userName+"\\password keeper1.0\\sysdat.dat");
        try {
            passwordFile.createNewFile();
            BufferedWriter wPasswordFile=new BufferedWriter(new FileWriter(passwordFile));
            wPasswordFile.write(password);
            wPasswordFile.close();
        } catch (IOException e) {
            System.out.println("Something went wrong!!!");
            setPassword();
        }

        System.out.println("Password set successfully");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");


        showMainMenu();
    }



    private static void checkIfWindows() {
        String[] osName;
        String fullOsName=System.getProperty("os.name").toLowerCase();
        osName=fullOsName.split(" ");

        if(!osName[0].equals("windows")){
            System.exit(0);
        }
    }




    //this fuction should be called only once ever
    private static void installProgramm() {
        //creating base folder and files
        File baseFolder     =new File("c:\\Users\\"+userName+"\\password keeper1.0");
        File baseFile       =new File("c:\\Users\\"+userName+"\\password keeper1.0\\baseFile.dat");
        BufferedWriter wBaseFile=null;   //writing to the file
        baseFolder.mkdir();         //creating folder
        try{
           baseFile.createNewFile();
           wBaseFile=new BufferedWriter(new FileWriter("c:\\Users\\"+userName+"\\password keeper1.0\\baseFile.dat",true));
           wBaseFile.write("id,name,login,password\n");
           wBaseFile.close();

        } catch (IOException e) {
            System.out.println("something went wrong");;
        }
    }

    private static boolean isInstalled() {
        File baseFile = new File(  "c:\\Users\\"+userName+"\\password keeper1.0\\baseFile.dat");
        if (baseFile.exists()) {
            return true;
        } else return false;
    }

    private static boolean isPasswordInstalled(){
        File passwordFile=new File("c:\\Users\\"+userName+"\\password keeper1.0\\sysdat.dat");
        if(passwordFile.exists()){
           return true;
        }
        else return false;
    }

}
