package com.tajutechgh.swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main {

	private static Connection connect() {
        Connection conn = null;
        
        String url = "jdbc:mysql://localhost:3306/swing_crud_app"; // MySQL database URL
        String user = "root";  // Replace with your MySQL username
        String password = "1432aziz";  // Replace with your MySQL password

        try {
            // Load the MySQL JDBC driver (optional since JDBC 4.0)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL database successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

	public static void main(String[] args) {
		// GUI Setup
		JFrame frame = new JFrame("User CRUD Application");
		
		frame.setSize(1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Components
		JLabel nameLabel = new JLabel("Name:");
		JLabel emailLabel = new JLabel("Email:");
		JTextField nameField = new JTextField(20);
		JTextField emailField = new JTextField(20);
		JButton createButton = new JButton("Create");
		JButton readButton = new JButton("Read");
		JButton updateButton = new JButton("Update");
		JButton deleteButton = new JButton("Delete");

		JPanel panel = new JPanel();
		panel.add(nameLabel);
		panel.add(nameField);
		panel.add(emailLabel);
		panel.add(emailField);
		panel.add(createButton);
		panel.add(readButton);
		panel.add(updateButton);
		panel.add(deleteButton);

		frame.add(panel);
		frame.setVisible(true);

		// Create Operation
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
				String email = emailField.getText();
				if (!name.isEmpty() && !email.isEmpty()) {
					String sql = "INSERT INTO users(name, email) VALUES(?, ?)";

					try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
						pstmt.setString(1, name);
						pstmt.setString(2, email);
						pstmt.executeUpdate();
						JOptionPane.showMessageDialog(null, "User created successfully!");
					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
					}
				}
			}
		});

		// Read Operation
		readButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sql = "SELECT * FROM users";
				try (Connection conn = connect();
						Statement stmt = conn.createStatement();
						ResultSet rs = stmt.executeQuery(sql)) {
					StringBuilder userList = new StringBuilder();
					while (rs.next()) {
						userList.append("ID: ").append(rs.getInt("id")).append(", Name: ").append(rs.getString("name"))
								.append(", Email: ").append(rs.getString("email")).append("\n");
					}
					JOptionPane.showMessageDialog(null, userList.toString());
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
				}
			}
		});

		// Update Operation
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
				String email = emailField.getText();
				if (!name.isEmpty() && !email.isEmpty()) {
					String sql = "UPDATE users SET name = ? WHERE email = ?";
					try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
						pstmt.setString(1, name);
						pstmt.setString(2, email);
						int rowsAffected = pstmt.executeUpdate();
						if (rowsAffected > 0) {
							JOptionPane.showMessageDialog(null, "User updated successfully!");
						} else {
							JOptionPane.showMessageDialog(null, "No user found with that email.");
						}
					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
					}
				}
			}
		});

		// Delete Operation
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String email = emailField.getText();
				if (!email.isEmpty()) {
					String sql = "DELETE FROM users WHERE email = ?";
					try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
						pstmt.setString(1, email);
						int rowsAffected = pstmt.executeUpdate();
						if (rowsAffected > 0) {
							JOptionPane.showMessageDialog(null, "User deleted successfully!");
						} else {
							JOptionPane.showMessageDialog(null, "No user found with that email.");
						}
					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
					}
				}
			}
		});
	}
}