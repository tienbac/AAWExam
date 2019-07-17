import entity.Employee;
import entity.LeaveRecord;
import entity.Schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateConnection {
    public static void main(String[] args) {
        ConnectionHelper ch = new ConnectionHelper();
        Boolean status = ch.getConnection();
        System.out.println(status);

        List<Object> list = new ArrayList<>();
        list.add(new Employee());
        list.add(new LeaveRecord());
        list.add(new Schedule());
        ch.createTable(list);
    }

}
