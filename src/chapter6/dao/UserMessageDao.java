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

import chapter6.beans.UserMessage;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class UserMessageDao {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public UserMessageDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	public List<UserMessage> select(Connection connection, String startDate, String endDate, Integer id, int num) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ");
			//参照
			sql.append("    messages.id as id, ");
			//つぶやきIDをas句によりidに別名変更
			sql.append("    messages.text as text, ");
			//つぶやきをas句によりtextに別名変更
			sql.append("    messages.user_id as user_id, ");
			//ユーザIDをas句によりuser_idに別名変更
			sql.append("    users.account as account, ");
			//アカウントをas句によりaccountに別名変更
			sql.append("    users.name as name, ");
			//名前をas句によりnameに別名変更
			sql.append(" messages.created_date as created_date ");
			//作成日をas句によりcreated_dateに別名変更
			sql.append("FROM messages ");
			//【messages】データベースから情報取得
			sql.append("INNER JOIN users ");
			//【users】データベースと【messages】データベースの
			//usersデータを抽出して結合
			sql.append("ON messages.user_id = users.id ");
			//messages.user_idとusers.idが一致した際に結合
			sql.append(" WHERE messages.created_date BETWEEN ? AND ? ");
			//【messages】データベースの作成日の開始日、終了日をバインド変数を使って値を後から格納し、
			//BETWEENによりその値の範囲内を絞りこんでつぶやき絞込みを行う
			if (id != null) {
				sql.append(" AND user_id = ? ");
			}
			//ユーザIDの指定があった場合にif文の中へネストし、
			//ネストした場合【messages】データベースのユーザIDをバインド変数を使って値を後から格納し、
			//特定のユーザのつぶやきを表示する
			sql.append("ORDER BY created_date DESC limit " + num);
			//つぶやき作成日のデータのソート順を降順にし、
			//取得するデータの件数を1000件に制限する

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, startDate);
			ps.setString(2, endDate);
			if (id != null) {
				ps.setInt(3, id);
			}

			ResultSet rs = ps.executeQuery();

			List<UserMessage> messages = toUserMessages(rs);

			return messages;
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<UserMessage> toUserMessages(ResultSet rs) throws SQLException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<UserMessage> messages = new ArrayList<UserMessage>();
		try {
			while (rs.next()) {
				UserMessage message = new UserMessage();
				message.setId(rs.getInt("id"));
				message.setText(rs.getString("text"));
				message.setUserId(rs.getInt("user_id"));
				message.setAccount(rs.getString("account"));
				message.setName(rs.getString("name"));
				message.setCreatedDate(rs.getTimestamp("created_date"));

				messages.add(message);
			}
			return messages;
		} finally {
			close(rs);
		}
	}
}