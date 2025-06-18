/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Nichie
 */

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Nichie
 */
public class ButtonsStyle {

    public static JButton ButtonStyle1(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(52, 152, 219)); // Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    public static JButton ButtonStyle2(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(46, 204, 113)); // Green
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    public static JButton DangerButtonStyle(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(231, 76, 60)); // Red
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }
}