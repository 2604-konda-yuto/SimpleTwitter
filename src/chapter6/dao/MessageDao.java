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

import chapter6.beans.Message;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class MessageDao {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public MessageDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	public void insert(Connection connection, Message message) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO messages ( ");
			//【messages】データベースに登録している
			sql.append("    user_id, ");
			//ユーザIDをSQLに追加
			sql.append("    text, ");
			//つぶやきをSQLに追加
			sql.append("    created_date, ");
			//作成日をSQLに追加
			sql.append("    updated_date ");
			//更新日をSQLに追加
			sql.append(") VALUES ( ");
			sql.append("    ?, "); // user_id
			//ユーザIDをバインド変数を使って値を後から格納
			sql.append("    ?, "); // text
			//つぶやきをバインド変数を使って値を後から格納
			sql.append("    CURRENT_TIMESTAMP, "); // created_date
			//作成日の日付と時刻を取得
			sql.append("    CURRENT_TIMESTAMP "); // updated_date
			//更新日の日付と時刻を取得
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, message.getUserId());
			ps.setString(2, message.getText());

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void delete(Connection connection, int id) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			String sql = "DELETE FROM messages WHERE id = ?";
			//【messages】データベースのつぶやきIDをバインド変数に格納して値を後から格納し、
			// そのつぶやきIDと一致したつぶやきを削除する

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, id);

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public Message select(Connection connection, Integer id) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM messages WHERE id = ?";
			//【messages】データベースのつぶやきIDをバインド変数に格納して値を後から格納し、
			// そのつぶやきIDに一致したつぶやき編集画面を表示させる

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();

			List<Message> message = editUserMessages(rs);

			if (message.isEmpty()) {
				return null;
			} else {
				return message.get(0);
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void update(Connection connection, Message message) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			String sql = "UPDATE messages SET text = ?, created_date = CURRENT_TIMESTAMP WHERE id = ?";
			//【messages】データベースにつぶやき、つぶやきIDをバインド変数に格納して値を後から格納し、
			// 更新日を変更するために更新日に現在の日付と時刻を格納してつぶやき情報を更新

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, message.getText());
			ps.setInt(2, message.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<Message> editUserMessages(ResultSet rs) throws SQLException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<Message> messages = new ArrayList<Message>();
		//beansから取得したArrayList<Message>をList<Message>型のmessagesに格納し作成
		try {
			while (rs.next()) {
				Message message = new Message();
				message.setId(rs.getInt("id"));
				//beans.Messageから取得したidをmessageに格納
				message.setUserId(rs.getInt("user_id"));
				//beans.Messageから取得したuser_idをmessageに格納
				message.setText(rs.getString("text"));
				//beans.Messageから取得したtextをmessageに格納
				message.setCreatedDate(rs.getTimestamp("created_date"));
				//beans.Messageから取得したcreated_dateをmessageに格納
				message.setUpdatedDate(rs.getTimestamp("updated_date"));
				//beans.Messageから取得したupdated_dateをmessageに格納

				messages.add(message);
			}
			return messages;
		} finally {
			close(rs);
		}
	}
}