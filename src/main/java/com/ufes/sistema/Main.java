/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufes.sistema;

import com.ufes.sistema.dao.DatabaseConnection;

/**
 *
 * @author Adler
 */
public class Main {

    public static void main(String[] args) {

        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        
        System.out.println(databaseConnection);

    }
}
