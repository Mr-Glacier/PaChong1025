package Dao;

public class DaoYiChe extends DaoFather {

    public DaoYiChe(int choseDB, int choseTable) {
        super(choseDB, choseTable);
    }

    public String TableName = this.tableName;

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String TableName) {
        this.TableName = TableName;
    }

    //更新下载状态
    public void Method_ChangeState(int key,String TableName,String columnName){
        String sql = "update "+TableName+" set  "+columnName+" = '是' where "+primaryKey+" ="+key;
        MethodIUD(sql);
    }



}
