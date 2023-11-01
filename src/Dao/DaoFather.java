package Dao;

import Until.ReadFileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;

public class DaoFather {
    //父类dao
    protected Connection conn = null;
    protected Statement stmt = null;
    //sql操作 所需参数
    protected String driverName;
    protected String connectionUrl;
    protected String userPass;
    protected String userName;
    protected String primaryKey;
    protected String tableName;
    protected String beanName;
    protected String DBName;
    //仅需要更改配置文件路径,以及使用时候的 数据库选择 表选择
    private String jsonpath = "F:\\ZKZD\\Java项目\\PaChong1025\\src\\Config\\";
    private String filename = "DBConfig.json";

    public DaoFather(int choseDB, int choseTable) {
        ReadFileUtil readFileUtil = new ReadFileUtil();
        String content = readFileUtil.MethodReadFile(this.jsonpath, this.filename);
        //System.out.println(content);
        JSONObject DBItems1 = JSON.parseObject(content);
        JSONArray DBArray = DBItems1.getJSONArray("Parameter");
        JSONObject DBChose = DBArray.getJSONObject(choseDB);
        this.driverName = DBChose.getString("DBDriver");
        this.DBName = DBChose.getString("DBName");
        this.connectionUrl = DBChose.getString("DBConnectionStr");
        this.userName = DBChose.getString("DBUserName");
        this.userPass = DBChose.getString("DBUserPass");
        String beanPath = DBChose.getString("EntityPath");
        JSONArray TableArray = DBChose.getJSONArray("EntityList");
        JSONObject TableChose = TableArray.getJSONObject(choseTable);
        this.beanName = beanPath + TableChose.getString("EntityName");//实体类地址
        this.primaryKey = TableChose.getString("PrimaryKey"); //主键
        this.tableName = TableChose.getString("TableName"); //表名
        System.out.println("本次调用Dao 参数情况如下:\n本次数据库名称: " + DBName + "\n" + "本次执行表名: " + this.tableName);
    }

    public void MethodCreateSomeObject() {
        try {
            Class.forName(this.driverName);//注册驱动
            if (null == conn || conn.isClosed()) {
                conn = DriverManager.getConnection(this.connectionUrl+"databaseName="+this.DBName, this.userName, this.userPass);//创建链接
            }
            if (null == stmt || stmt.isClosed()) {
                stmt = conn.createStatement();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void MethodIUD(String str) {
        MethodCreateSomeObject();
        try {
            System.out.println(str.replace("\n", "")
                    .replace("\t", "").replace("\r", ""));

            stmt.executeUpdate(str.replace("\n", "")
                    .replace("\t", "").replace("\r", ""));

            //替换插入语句中的特殊情况 例如换行等
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void MethodInsert(Object obj) {
        try {
            Class c = obj.getClass();
            Method[] methods = c.getDeclaredMethods();
            String columnList = "";
            String valueList = "";
            for (Method method : methods) {
                if (method.getName().equals("get" + this.primaryKey)) {
                    continue;
                }
                if (method.getName().startsWith("get")) {
                    String columnName = method.getName().replace("get", "");
                    columnList += columnName + ",";
                    String value = method.invoke(obj) == null ? "-" : method.invoke(obj).toString();
                    //如果为空则替换为-
                    if (method.getReturnType() == String.class) {
                        valueList += "'" + value + "',";
                    } else {
                        valueList += value + ",";
                    }
                }
            }
            columnList = columnList.substring(0, columnList.length() - 1);
            valueList = valueList.substring(0, valueList.length() - 1);
            String sql = "INSERT INTO " + this.tableName + "( " + columnList + " )values( " + valueList + " )";
            MethodIUD(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Object> MethodFind() {
        ArrayList<Object> results = new ArrayList<Object>();
        try {
            String query = "SELECT*FROM " + this.tableName;
            MethodCreateSomeObject();
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                Class F = Class.forName(this.beanName);
                //Class F = obj.getClass();
                //实例化
                Object Find = F.newInstance();
                //获取列名
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                //获取列数
                int lieNumber = resultSetMetaData.getColumnCount();
//                System.out.println(lieNumber);
                for (int i = 0; i < lieNumber; i++) {
                    //通过序号获取列名
                    String columnName = resultSetMetaData.getColumnName(i + 1);
                    //获取值
                    Object columnValue = resultSet.getObject(i + 1);
                    //根据列名获取属性.getDeclaredField,获取类中所有的声明字段
                    Field field = F.getDeclaredField(columnName);
                    //可以向私有属性中写值,将private变为public
                    field.setAccessible(true);
                    //写值
                    field.set(Find, columnValue);
                }
                results.add(Find);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return results;
    }

}
