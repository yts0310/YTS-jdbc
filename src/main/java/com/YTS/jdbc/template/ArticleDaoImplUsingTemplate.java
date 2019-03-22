package com.YTS.jdbc.template;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import com.YTS.jdbc.raw.Article;
import com.YTS.jdbc.raw.ArticleDao;
import org.mariadb.jdbc.MariaDbDataSource;


public class ArticleDaoImplUsingTemplate implements ArticleDao{
	static final String LIST_ARTICLES = "SELECT articleId, title, name, cdate FROM article LIMIT 20";
	static final String GET_ARTICLE = "SELECT articleId, title, content, name, cdate FROM article WHERE articleId=?";
	static final String ADD_ARTICLE = "INSERT INTO article(title, content, userId, name) VALUES (?, ?, ?, ?)";
	static final String UPDATE_ARTICLE = "UPDATE article SET title=?, content=? WHERE articleId=?";
	static final String DELETE_ARTICLE = "DELETE FROM article WHERE articleId=?";
	DataSource dataSource;
	JdbcTemplate jdbcTemplate;
	
	public ArticleDaoImplUsingTemplate() {
		dataSource = new MariaDbDataSource("jdbc:mariadb://localhost:3306/YTSdb?user=YTS&password=1056xotlr7");
		jdbcTemplate = new JdbcTemplate(dataSource);
		}
	
	@Override
	public List<Article> listArticles() {
		return jdbcTemplate.queryForList(LIST_ARTICLES, null,
				new RowMapper<>() {
					@Override
					public Article mapRow(ResultSet rs) throws SQLException {
						Article article = new Article();
						article.setArticleId(rs.getString("articleId"));
						article.setTitle(rs.getString("title"));
						article.setName(rs.getString("name"));
						article.setCdate(rs.getString("cdate"));
						return article;
					}
				});
	}

	/**
	 * 게시글 상세
	 */
	@Override
	public Article getArticle(String articleId) {
		return jdbcTemplate.queryForObject(GET_ARTICLE,
				new Object[] { articleId }, new RowMapper<>() {
					@Override
					public Article mapRow(ResultSet rs) throws SQLException {
						Article article = new Article();
						article.setArticleId(rs.getString("articleId"));
						article.setTitle(rs.getString("title"));
						article.setContent(rs.getString("content"));
						article.setName(rs.getString("name"));
						article.setCdate(rs.getString("cdate"));
						return article;
					}
				});
	}
	public void addArticle(Article article) {
		jdbcTemplate.update(ADD_ARTICLE, article.getTitle(),
				article.getContent(), article.getUserId(), article.getName());
	}
	public void updateArticle(Article article) {
		jdbcTemplate.update(UPDATE_ARTICLE, article.getTitle(),
				article.getContent(), article.getArticleId());
	}
	public void deleteArticle(String articleId) {
		jdbcTemplate.update(DELETE_ARTICLE, articleId);
		}
}
