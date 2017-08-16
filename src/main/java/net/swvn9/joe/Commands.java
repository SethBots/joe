package net.swvn9.joe;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Commands {
    protected static Map<String,Command> list = new HashMap<>();
    //All loaded commands are here
    private static Map<String,Class> classes = new HashMap<>();
    //Commands class directory
    private static File dir = new File("commands"+File.separator);
    //Commands class file array
    private static File[] dirlist = dir.listFiles((dir, name) -> name.toLowerCase().endsWith(".groovy"));


    //Load any command classes
    static void load(){
        //new class loader
        GroovyClassLoader gcl = new GroovyClassLoader();
        for(File f:dirlist){
            try {
                //parse the class
                Class toLoad = gcl.parseClass(f);
                //check if the command is already registered
                if(classes.containsKey(toLoad.getName())){
                    classes.replace(toLoad.getName(),toLoad);
                    try{
                        list.replace(toLoad.getName(),(Command) toLoad.newInstance());
                    } catch (Exception e){ e.printStackTrace(); }
                    //System.out.println("Reloaded "+toLoad.getName());
                } else {
                    classes.put(toLoad.getName(),toLoad);
                    try{
                        list.put(toLoad.getName(),(Command) toLoad.newInstance());
                    } catch (Exception e){ e.printStackTrace(); }
                    //System.out.println("Loaded "+toLoad.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //Re-check the command class folder
    public static String reload(String s){
        Commands.dirlist = dir.listFiles((dir, name) -> name.toLowerCase().endsWith(".groovy"));
        GroovyClassLoader gcl = new GroovyClassLoader();
        for(File f:dirlist){
            try {
                //parse the class
                Class toLoad = gcl.parseClass(f);
                if(!toLoad.getName().equalsIgnoreCase(s)) continue;
                //check if the command is already registered
                if(classes.containsKey(toLoad.getName())){
                    classes.replace(toLoad.getName(),toLoad);
                    try{
                        list.replace(toLoad.getName(),(Command) toLoad.newInstance());
                    } catch (Exception e){ e.printStackTrace(); }
                    //System.out.println("Reloaded "+toLoad.getName());
                    return "Reloaded "+toLoad.getName();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static void reload(){
        Commands.dirlist = dir.listFiles((dir, name) -> name.toLowerCase().endsWith(".groovy"));
        classes = new HashMap<>();
        list = new HashMap<>();
        load();
        //print();
    }

    public static String show(){
        return "Commands: "+classes.keySet();
    }

    //print the map key set
    static void print(){
        if(!dir.exists()) dir.mkdir();
        System.out.println("Commands: "+classes.keySet());
    }
}
