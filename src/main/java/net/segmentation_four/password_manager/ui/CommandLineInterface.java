package net.segmentation_four.password_manager.ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class CommandLineInterface implements UserInterface {
    private PrintStream out;
    private Scanner in;

    public CommandLineInterface(PrintStream out, InputStream in) {
        this.out = out;
        this.in = new Scanner(in);
    }

    @Override
    public String getPassword() {
        return null;
    }
}