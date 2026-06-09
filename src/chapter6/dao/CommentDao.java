package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Comment;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class CommentDao {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public CommentDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	public void insert(Connection connection, Comment comment) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO comments ( ");
			//【comments】データベースを参照している
			sql.append("    text, ");
			//返信をSQLに追加
			sql.append("    user_id, ");
			//ユーザIDをSQLに追加
			sql.append("    message_id, ");
			//つぶやきIDをSQLに追加
			sql.append("    created_date, ");
			//作成日をSQLに追加
			sql.append("    updated_date ");
			//更新日をSQLに追加
			sql.append(") VALUES ( ");
			sql.append("    ?, "); // text
			//テキストにバインド変数を使って値を後から格納
			sql.append("    ?, "); // user_id
			//ユーザIDにバインド変数を使って値を後から格納
			sql.append("    ?, "); // message_id
			//つぶやきIDにバインド変数を使って値を後から格納
			sql.append("    CURRENT_TIMESTAMP, "); // created_date
			//作成日の日付と時刻を取得
			sql.append("    CURRENT_TIMESTAMP "); // updated_date
			//更新日の日付と時刻を取得
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, comment.getText());
			ps.setInt(2, comment.getUserId());
			ps.setInt(3, comment.getMessageId());

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
}
