package com.fullApp.myApp.jdbc;

import com.fullApp.myApp.models.Customer;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("John");
        when(resultSet.getString("email")).thenReturn("John@gmail.com");
        when(resultSet.getInt("age")).thenReturn(19);




        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        Customer expected = new Customer(
                1L,
                "John",
                "John@gmail.com",
                19
        );

        assertThat(actual).isEqualTo(expected);

    }

}