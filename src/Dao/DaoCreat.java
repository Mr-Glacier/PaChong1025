package Dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DaoCreat extends DaoFather {

    public DaoCreat(int choseDB, int choseTable) {
        super(choseDB, choseTable);
    }

    public void MethodIUD(String str) {
        MethodCreateSomeObject();
        try {
            System.out.println(str);
            stmt.executeUpdate(str);
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //    创建表
    public void CreatTable() {
        MethodCreateSomeObject();
        Map<String, String> typeMap = new HashMap<>(); //根据数据类型进行补充
        typeMap.put("int", "int");
        typeMap.put("class java.lang.String", "nvarchar(200)");

        try {
            Field[] fields = Class.forName(beanName).getDeclaredFields(); //实体化对象并获取其私有共有字段
            ArrayList<Map<String, String>> columnList = new ArrayList<>();
            Map<String, String> infoMap;
            for (Field field : fields) {
                infoMap = new HashMap<String, String>();
                infoMap.put("type", field.getType().toString());
                infoMap.put("name", field.getName());
                columnList.add(infoMap);
            }//存入实体类的属性名称 以及属性数据类型

            StringBuilder sql = new StringBuilder();
            String name = "";
            String type = null;
            for (Map<String, String> stringStringMap : columnList) {
                name = stringStringMap.get("name");
                if (name.equals(this.primaryKey)) {
                    type = typeMap.get(stringStringMap.get("type")) + " not null primary key identity(1,1)";
                } else {
                    type = typeMap.get(stringStringMap.get("type"));
                }
                sql.append(name).append(' ').append(type).append(",\n");
            }
            String SelectSql = "USE " + DBName + "  select count(*)from sysobjects where name = '" + tableName + "'";
            ResultSet resultSet = stmt.executeQuery(SelectSql);
            int rowCount = 0;
            if(resultSet.next())
            {
                rowCount = resultSet.getInt(1);
            }
            System.out.println(rowCount);
            String CreatSql="create table "+tableName+"("+sql.substring(0, sql.length()-2)+")";
            String ClearSql= "drop table "+tableName;
            if (rowCount ==1){
                MethodIUD(ClearSql);
                MethodIUD(CreatSql);
            }else if (rowCount == 0){
                MethodIUD(CreatSql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    创建库
    public void CreatDatabase() {
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(connectionUrl, userName, userPass);
            stmt = conn.createStatement();
            String sql = "CREATE DATABASE " + DBName;
            stmt.execute(sql);
            System.out.println(DBName+"数据库创建成功！");
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DaoCreat daoCreat = new DaoCreat(1, 0);
//        daoCreat.CreatDatabase();
        daoCreat.CreatTable();
    }

}
