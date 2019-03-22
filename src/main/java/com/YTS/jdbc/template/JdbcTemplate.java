package com.YTS.jdbc.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.YTS.jdbc.raw.DaoException;

public class JdbcTemplate {

	DataSource dataSource;

	public JdbcTemplate(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public <T> List<T> queryForList(String query, Object[] params,
			RowMapper<T> rowMapper) throws DaoException {
		try (Connection con = dataSource.getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {
			// preparedStatement에 파라미터를 넣음
			setParams(ps, params);
			List<T> list = new ArrayList<>();
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				list.add(rowMapper.mapRow(rs));
			rs.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public <T> T queryForObject(String query, Object[] params,
			RowMapper<T> rowMapper) throws DaoException {
		try (Connection con = dataSource.getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {
			// preparedStatement에 파라미터를 넣음
			setParams(ps, params);
			ResultSet rs = ps.executeQuery();
			T t = null;
			if (rs.next())
				t = rowMapper.mapRow(rs);
			rs.close();
			return t;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}
	public int update(String query, Object... params) throws DaoException {
		try (Connection con = dataSource.getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {
			// preparedStatement에 파라미터를 넣음
			setParams(ps, params);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	private void setParams(PreparedStatement ps, Object[] params)
			throws SQLException {
		if (params != null) {
			for (int i = 0; i < params.length; i++)
				ps.setObject(i + 1, params[i]);
		}
	}
}