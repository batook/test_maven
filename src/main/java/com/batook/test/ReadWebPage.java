package com.batook.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ReadWebPage {
    public static void main(String[] args) {
        try {
            URL google = new URL("http://www.google.com/");
            BufferedReader in = new BufferedReader(new InputStreamReader(google.openStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                // Process each line.
                System.out.println(inputLine);
            }
            in.close();

        } catch (MalformedURLException me) {
            System.out.println(me);

        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
