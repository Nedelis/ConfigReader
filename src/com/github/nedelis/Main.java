package com.github.nedelis;

import com.github.nedelis.actions.parse.Parser;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        Parser.parse(new File("src/com/github/nedelis/test.ncf"));
        System.out.println(Parser.getData());
    }

}
