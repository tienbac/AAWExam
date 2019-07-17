import entity.LeaveRecord;
import entity.Schedule;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConnectionHelper {
    public static Connection connection;
    public static Statement stmt;
    public static PreparedStatement ps;
    String DATABASE = "aawexam";
    String HOST_URL = "jdbc:mysql://localhost:3306/";
    String USER = "root";
    String PASSWORD = "";

    public boolean hasConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = HOST_URL;
            connection = DriverManager.getConnection(url, USER, PASSWORD);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnectionToDb(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(HOST_URL + DATABASE, USER, PASSWORD);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return connection;
        }
    }

    public Connection getConnectionToDbNotExists(){
        if (hasConnection()){
            try {
                ResultSet rs = connection.getMetaData().getCatalogs();
                List<Boolean> list = new ArrayList<>();
                while (rs.next()){
                    String catalogs = rs.getString(1);
                    if (DATABASE.equalsIgnoreCase(catalogs)){
                        list.add(true);
                    }else {
                        list.add(false);
                    }
                }
                if (list.indexOf(true) == -1){
                    stmt = connection.createStatement();
                    String sqlDb = "CREATE DATABASE " + DATABASE + " CHARACTER SET utf8 COLLATE utf8_general_ci";
                    stmt.executeUpdate(sqlDb);
                    connection = getConnectionToDb();
                }else {
                    connection = getConnectionToDb();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
        }
        return null;
    }

    public boolean getConnection(){
        connection = getConnectionToDbNotExists();
        if (connection != null){
            return true;
        }
        return false;
    }

    public String createSql(Object object){
        Class cObject = object.getClass();
        Field[] fields = cObject.getDeclaredFields();
        ArrayList<ObjSql> lists = new ArrayList<>();
        List<String> list = new ArrayList<>();
        for (Field field: fields
        ) {
            ObjSql objSql = new ObjSql();
            objSql.setName(field.getName());
            objSql.setType(field.getType().getSimpleName());
            list.add(field.getName());
            lists.add(objSql);
        }

        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE " + cObject.getSimpleName() + "s ( ");
        for (int i = 0; i < lists.size(); i++){
            ObjSql objSql = lists.get(i);
            if (objSql.getName().equalsIgnoreCase("id") || objSql.getType().equals("HashMap")){
                lists.remove(objSql);
            }
        }
        String sqlId = "id int NOT NULL AUTO_INCREMENT, ";
        builder.append(sqlId);
        for (ObjSql obj: lists
        ) {
            if (obj.getType().equalsIgnoreCase("String")){
                String sql = obj.getName() + " varchar(200) NOT NULL, ";
                builder.append(sql);
            }else if (obj.getType().equalsIgnoreCase("long")){
                String sql = obj.getName() + " bigint NOT NULL, ";
                builder.append(sql);
            }else {
                String sql = obj.getName() + " " + obj.getType() + " NOT NULL,";
                builder.append(sql);
            }
        }
        String sqlKey = "CONSTRAINT " + cObject.getSimpleName() + "_pk PRIMARY KEY (id)";
        builder.append(sqlKey);

        String sqlQr = builder.toString() + " );";
        return sqlQr;
    }

    public void createTable(List<Object> list){
        if (getConnection()){
            try {
                stmt = connection.createStatement();
                for (Object object: list
                ) {
                    String sqlTable = createSql(object);
                    stmt.addBatch(sqlTable);
                }
//            String sql = "ALTER TABLE User ADD CONSTRAINT User_Feedback FOREIGN KEY User_Feedback (userId)\n" +
//                    "    REFERENCES User (id);";
                int[] status = stmt.executeBatch();
                System.out.println(status[1]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addLeave(LeaveRecord leaveRecord){
        ConnectionHelper ch = new ConnectionHelper();
        if (ch.getConnection()){
            String sql = "INSERT INTO leaverecords(name, note, submitedAt, status) VALUE (?,?,?,?)";
            try {
                PreparedStatement ps =connection.prepareStatement(sql);
                ps.setString(1, leaveRecord.getName());
                ps.setString(2, leaveRecord.getNote());
                ps.setLong(3, leaveRecord.getSubmitedAt());
                ps.setInt(4, leaveRecord.getStatus());
                ps.executeUpdate();

            } catch (SQLException e) {
                System.out.println(new Date() + " - LOG : Sorry! Can't connect to database. Please try again!");
                e.printStackTrace();
            }
        }
    }

    public ArrayList<LeaveRecord> getallLeave(){
        ArrayList<LeaveRecord> listLeave = new ArrayList<>();
        if (getConnection()){
            try {
                Statement stmt = connection.createStatement();
                String sql = "SELECT * FROM leaverecords";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()){
                    LeaveRecord leaveRecord = new LeaveRecord();
                    leaveRecord.setId(rs.getInt("id"));
                    leaveRecord.setName(rs.getString("name"));
                    leaveRecord.setNote(rs.getString("note"));
                    leaveRecord.setSubmitedAt(rs.getLong("submitedAt"));
                    leaveRecord.setStatus(rs.getInt("status"));
                    listLeave.add(leaveRecord);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return listLeave;
        }
        return null;
    }

    public Schedule getSchedule(int id){
        
    }
}
class ObjSql{
    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
