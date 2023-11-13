package github.evertonbrunosds.notepad.security.model;

import github.evertonbrunosds.notepad.util.Routes;

public class AllowedRoutes {

    public static String[] post() {
        return new String[] { Routes.SIGNUP, Routes.SIGNIN };
    }

    public static String[] put() {
        return new String[] {};
    }

    public static String[] get() {
        return new String[] {};
    }

    public static String[] delete() {
        return new String[] {};
    }

    public static String[] options() {
        return new String[] {};
    }

    public static String[] trace() {
        return new String[] {};
    }

    public static String[] patch() {
        return new String[] {};
    }

    public static String[] head() {
        return new String[] {};
    }

}
