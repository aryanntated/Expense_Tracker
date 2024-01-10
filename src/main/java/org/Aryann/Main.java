package org.Atharv1;
import com.formdev.flatlaf.intellijthemes.FlatGradiantoMidnightBlueIJTheme;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatGradiantoMidnightBlueIJTheme());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginRegisterUI(); // Goto Login Page
            }
        });
        // Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        //     try {
        //         if (ConnectDB.connect() != null && !ConnectDB.connect().isClosed()) {
        //             ConnectDB.connect().close();
        //             System.out.println("closed!!");
        //         }
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        // }));
    }
}