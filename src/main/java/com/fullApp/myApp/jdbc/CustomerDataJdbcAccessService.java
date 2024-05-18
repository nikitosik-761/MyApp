package com.fullApp.myApp.jdbc;

import com.fullApp.myApp.models.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

//Alt + Shift -> несколько кареток
//Ctr + Alt + O -> удалить импорты
// Ctr + Z -> вернуть обратно
// Ctr + P -> Что вставлять



@Repository("jdbc")
@RequiredArgsConstructor
public class CustomerDataJdbcAccessService implements CustomerDao{


    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;
    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT * FROM Customer;
                """;


        return  jdbcTemplate.query(sql, customerRowMapper);

    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql = """
                SELECT * FROM Customer WHERE id = ?;
                """;

        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
     var sql = """
             INSERT INTO Customer(name, email, password ,age, gender)
             VALUES (?, ?, ?, ?, ?);
             """;
     int result = jdbcTemplate.update(
             sql,
             customer.getName(),
             customer.getEmail(),
             customer.getPassword(),
             customer.getAge(),
             customer.getGender().name()
     );

        System.out.println(result);

    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql = """
                SELECT COUNT(id)
                FROM Customer
                WHERE email = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;

    }

    @Override
    public boolean existsPersonWithId(Long id) {
        var sql = """
                SELECT COUNT(id)
                FROM Customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;

    }

    @Override
    public void deleteCustomerById(Long id) {

        var sql = """
                DELETE 
                FROM Customer
                WHERE id = ?
                """;

        int result = jdbcTemplate.update(sql, id);
        System.out.println(result);

    }

    @Override
    public void updateCustomer(Customer update) {
        if (update.getName() != null){
            String sql = "UPDATE Customer SET name = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getName(),
                    update.getId()
            );
            System.out.println(result);
        }

        if (update.getEmail() != null){
            String sql = "UPDATE Customer SET email = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getEmail(),
                    update.getId()
            );
            System.out.println(result);
        }

        if (update.getAge() != null){
            String sql = "UPDATE Customer SET age = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getAge(),
                    update.getId()
            );
            System.out.println(result);
        }

    }
}
