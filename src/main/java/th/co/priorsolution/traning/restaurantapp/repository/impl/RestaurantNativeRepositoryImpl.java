package th.co.priorsolution.traning.restaurantapp.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import th.co.priorsolution.traning.restaurantapp.model.*;
import th.co.priorsolution.traning.restaurantapp.repository.RestaurantNativeRepository;

import java.sql.*;
import java.util.*;

@Repository
public class RestaurantNativeRepositoryImpl implements RestaurantNativeRepository {
    private JdbcTemplate jdbcTemplate;

    public RestaurantNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> getMenuInfo(List<Integer> foodIds){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT menu_id, menu_quantity, menu_name, menu_type FROM menu_item WHERE menu_id IN (");
        StringJoiner placeholders = new StringJoiner(", ");
        for (int i = 0; i < foodIds.size(); i++) {
            placeholders.add("?");
        }
        sql.append(placeholders);
        sql.append(")");
        try {
            List<Object> paramList = new ArrayList<>();
            paramList.add(foodIds);
//            return this.jdbcTemplate.queryForMap(sql.toString(), foodId);
            return this.jdbcTemplate.query(sql.toString(), foodIds.toArray(), new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map<String, Object> result = new HashMap<>();
                    result.put("menu_id", rs.getInt("menu_id"));
                    result.put("menu_quantity", rs.getInt("menu_quantity"));
                    result.put("menu_name", rs.getString("menu_name"));
                    result.put("menu_type", rs.getString("menu_type"));
                    return result;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null; // Return null if no rows were found
        }
    }
    @Override
    public void updateStock(int quantity,int menuId){
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE menu_item SET menu_quantity = ?");
        sql.append(" WHERE menu_id = ?");
        this.jdbcTemplate.update(sql.toString(),quantity,menuId);
    }
    @Override
    public List<RestaurantOrderModel> receiveOrderStatus(RestaurantOrderCriteriaRequestModel restaurantOrderCriteriaRequestModel) {
        List<Object> paramList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ord_id, ord_date, ord_status");
        sql.append(" FROM orders");
        sql.append(" WHERE 1=1 AND ord_status <> 'Served' AND ord_status <> 'Cancel'");
        if(StringUtils.isNotEmpty(restaurantOrderCriteriaRequestModel.getOrdStatus())){
            sql.append(" AND ord_status = ?");
            paramList.add(restaurantOrderCriteriaRequestModel.getOrdStatus());
        }
        if(restaurantOrderCriteriaRequestModel.getOrdId() > 0){
            sql.append(" AND ord_id = ?");
            paramList.add(restaurantOrderCriteriaRequestModel.getOrdId());
        }
        if(restaurantOrderCriteriaRequestModel.getOrdDate() != null && restaurantOrderCriteriaRequestModel.getOrdDate2() == null){
            sql.append(" AND ord_date = ?");
            paramList.add(restaurantOrderCriteriaRequestModel.getOrdDate());
        }
        if(restaurantOrderCriteriaRequestModel.getOrdDate() != null && restaurantOrderCriteriaRequestModel.getOrdDate2() != null){
            sql.append(" AND ord_date between ? AND ?");
            paramList.add(restaurantOrderCriteriaRequestModel.getOrdDate());
            paramList.add(restaurantOrderCriteriaRequestModel.getOrdDate2());
        }
        List<RestaurantOrderModel> result =this.jdbcTemplate.query(sql.toString(), new RowMapper<RestaurantOrderModel>() {
            @Override
            public RestaurantOrderModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                RestaurantOrderModel x =new RestaurantOrderModel();
                int col = 1;
                x.setOrdId(rs.getInt(col++));
                x.setOrdDate(rs.getDate(col++).toLocalDate());
                x.setOrdStatus(rs.getString(col++));
                return x;
            }
        },paramList.toArray());
        return result;
    }
}
