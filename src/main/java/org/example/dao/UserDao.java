package org.example.dao;


import org.example.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDao {

    private ConnectionMaker cm;
    private JdbcContext jdbcContext;
    public UserDao() {
        this.cm = new AwsConnectionMaker();
    }

    public UserDao(ConnectionMaker cm) {
        this.cm = cm;
    }

    public void deleteAll() throws SQLException {
        this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps=connection.prepareStatement("delete from users");
                return ps;
            }
        });
    }

//    public void deleteAll() throws SQLException {
//        Connection c = null;
//        PreparedStatement ps = null;
//
//        try {
//            c = cm.makeConnection();
//            //ps = c.prepareStatement("DELETE FROM users");
//            ps = new DeleteAllstrategy().makePreparedStatement(c);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw e;
//        } finally {
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (SQLException e) {
//                }
//            }
//            if (c != null) {
//                try {
//                    c.close();
//                } catch (SQLException e) {
//                }
//            }
//        }
//    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps =null;
        ResultSet rs = null;

        try{
            c = cm.makeConnection();
            ps = c.prepareStatement("SELECT count(*) FROM users");
            rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            return count;
        } catch (SQLException e){
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void add(User user) {
        try {
            Connection c = cm.makeConnection();

            // Query문 작성
            PreparedStatement pstmt = c.prepareStatement("INSERT INTO users(id, name, password) VALUES(?,?,?);");
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());

            // Query문 실행
            pstmt.executeUpdate();

            pstmt.close();
            c.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findById(String id) {
        Map<String, String> env = System.getenv();
        Connection c;
        try {
            // DB접속 (ex sql workbeanch실행)
            c = cm.makeConnection();

            // Query문 작성
            PreparedStatement pstmt = c.prepareStatement("SELECT * FROM users WHERE id = ?");
            pstmt.setString(1, id);

            // Query문 실행
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            User user = new User(rs.getString("id"), rs.getString("name"),
                    rs.getString("password"));

            rs.close();
            pstmt.close();
            c.close();

            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        try {
            Connection c = cm.makeConnection();

            Statement statement = c.createStatement();

            ResultSet rs = statement.executeQuery("select * from users");

            while (rs.next()) {
                User user = new User(rs.getString("id"),
                        rs.getString("name"), rs.getString("password"));
                userList.add(user);
            }

            rs.close();
            statement.close();
            c.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public static void main(String[] args) {

        UserDao userDao = new UserDao();
//        userDao.add();
        User user = userDao.findById("1");
        System.out.println(user.getName());

    }
}