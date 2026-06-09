package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.UserComment;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class UserCommentDao {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public UserCommentDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	public List<UserComment> select(Connection connection, int num) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ");
			//参照
			sql.append("    comments.id as id, ");
			//返信IDをas句によりidに別名変更
			sql.append("    comments.text as text, ");
			//返信をas句によりtextに別名変更
			sql.append("    comments.user_id as user_id, ");
			//ユーザIDをas句により	user_idに別名変更
			sql.append("    comments.message_id as message_id, ");
			//つぶやきIDをas句によりmessage_idに別名変更
			sql.append("    users.account as account, ");
			//アカウントをas句によりaccountに別名変更
			sql.append("    users.name as name, ");
			//名前をas句によりnameに別名変更
			sql.append("    comments.created_date as created_date ");
			//作成日をas句によりcreated_dateに別名変更
			sql.append("FROM comments ");
			//【comments】データベースから情報取得
			sql.append("INNER JOIN users ");
			//【users】データベースと【comments】データベースの
			// usersデータを抽出して結合
			sql.append("ON comments.user_id = users.id ");
			//comments.user_idとusers.idが一致した際に結合
			sql.append("ORDER BY created_date ASC limit " + num);
			//つぶやき作成日のデータのソート順を昇順にし、
			//取得するデータの件数を1000件に制限する

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();

			List<UserComment> comments = toUserComments(rs);

			return comments;
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<UserComment> toUserComments(ResultSet rs) throws SQLException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<UserComment> comments = new ArrayList<UserComment>();
		//beansから取得したArrayList<UserComment>をList<UserComment>型のcommentsに格納し作成
		try {
			while (rs.next()) {
				UserComment comment = new UserComment();
				comment.setId(rs.getInt("id"));
				//beans.UserCommentから取得したidをcommentに格納
				comment.setText(rs.getString("text"));
				//beans.UserCommentから取得したtextをcommentに格納
				comment.setUserId(rs.getInt("user_id"));
				//beans.UserCommentから取得したuser_idをcommentに格納
				comment.setAccount(rs.getString("account"));
				//beans.UserCommentから取得したaccountをcommentに格納
				comment.setName(rs.getString("name"));
				//beans.UserCommentから取得したnameをcommentに格納
				comment.setMessageId(rs.getInt("message_id"));
				//beans.UserCommentから取得したmessager_idをcommentに格納
				comment.setCreatedDate(rs.getTimestamp("created_date"));
				//beans.UserCommentから取得したcreated_dateをcommentに格納

				comments.add(comment);
			}
			return comments;
		} finally {
			close(rs);
		}
	}

}
