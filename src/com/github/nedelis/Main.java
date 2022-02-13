package com.github.nedelis;

import com.github.nedelis.actions.parse.Parser;
import com.github.nedelis.actions.read.Reader;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        Parser.parseNCFL(Reader.readConfigFile(new File("src/com/github/nedelis/test.ncf")), true);
    }

}
