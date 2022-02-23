package com.github.nedelis;

import com.github.nedelis.actions.FullRead;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        FullRead.read(new File("src/com/github/nedelis/test.ncf"));

        System.out.println(FullRead.getReadFiles());

    }

}
