package org.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementSrategy {
    PreparedStatement makePreparedStatement(Connection connection) throws SQLException;
}
